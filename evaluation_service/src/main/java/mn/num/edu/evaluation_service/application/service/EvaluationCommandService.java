package mn.num.edu.evaluation_service.application.service;

import mn.num.edu.evaluation_service.application.dto.EvaluationResponse;
import mn.num.edu.evaluation_service.application.dto.SubmitEvaluationCommand;
import mn.num.edu.evaluation_service.application.dto.UpdateEvaluationCommand;
import mn.num.edu.evaluation_service.application.port.in.SubmitEvaluationUseCase;
import mn.num.edu.evaluation_service.application.port.in.UpdateEvaluationUseCase;
import mn.num.edu.evaluation_service.application.port.out.EvaluationEventPublisherPort;
import mn.num.edu.evaluation_service.application.port.out.EvaluationRepositoryPort;
import mn.num.edu.evaluation_service.application.port.out.EvaluationSlotRepositoryPort;
import mn.num.edu.evaluation_service.application.port.out.StageDefinitionRepositoryPort;
import mn.num.edu.evaluation_service.domain.event.EvaluationSubmittedEvent;
import mn.num.edu.evaluation_service.domain.event.EvaluationUpdatedEvent;
import mn.num.edu.evaluation_service.domain.event.EvaluationCompletedEvent;
import mn.num.edu.evaluation_service.domain.event.DefenseEvaluatedEvent;
import mn.num.edu.evaluation_service.domain.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EvaluationCommandService implements SubmitEvaluationUseCase, UpdateEvaluationUseCase {

    private final EvaluationRepositoryPort evaluationRepositoryPort;
    private final EvaluationSlotRepositoryPort evaluationSlotRepositoryPort;
    private final StageDefinitionRepositoryPort stageDefinitionRepositoryPort;
    private final EvaluationEventPublisherPort eventPublisherPort;
    private final ScoreCalculator scoreCalculator;
    private final EvaluationEligibilityChecker eligibilityChecker;

    public EvaluationCommandService(EvaluationRepositoryPort evaluationRepositoryPort,
                                    EvaluationSlotRepositoryPort evaluationSlotRepositoryPort,
                                    StageDefinitionRepositoryPort stageDefinitionRepositoryPort,
                                    EvaluationEventPublisherPort eventPublisherPort,
                                    ScoreCalculator scoreCalculator,
                                    EvaluationEligibilityChecker eligibilityChecker) {
        this.evaluationRepositoryPort = evaluationRepositoryPort;
        this.evaluationSlotRepositoryPort = evaluationSlotRepositoryPort;
        this.stageDefinitionRepositoryPort = stageDefinitionRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
        this.scoreCalculator = scoreCalculator;
        this.eligibilityChecker = eligibilityChecker;
    }

    @Override
    public Mono<EvaluationResponse> submit(SubmitEvaluationCommand command) {
        Mono<StageDefinition> stageMono = stageDefinitionRepositoryPort.findByStageId(command.getStageId());
        Mono<EvaluationSlot> slotMono = evaluationSlotRepositoryPort.findByThesisIdAndStageId(
                command.getThesisId(),
                command.getStageId()
        );

        return Mono.zip(stageMono, slotMono)
                .flatMap(tuple -> {
                    StageDefinition stageDefinition = tuple.getT1();
                    EvaluationSlot slot = tuple.getT2();

                    eligibilityChecker.validate(stageDefinition, slot, command.getEvaluatorRole().name());

                    return evaluationRepositoryPort.existsByThesisIdAndStageIdAndEvaluatorId(
                                     command.getThesisId(),
                                     command.getStageId(),
                                     command.getEvaluatorId()
                            )
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new IllegalArgumentException(
                                            "This evaluator already submitted evaluation"
                                    ));
                                }

                                List<CriterionAssessment> updatedAssessments = command.getAssessments()
                                        .stream()
                                        .peek(a -> a.setWeightedScore(
                                                scoreCalculator.calculateWeighted(
                                                        a.getGivenScore(),
                                                        a.getMaxScore(),
                                                        a.getWeight()
                                                )
                                        ))
                                        .toList();

                                Evaluation evaluation = new Evaluation(
                                        UUID.randomUUID().toString(),
                                        command.getThesisId(),
                                        command.getStageId(),
                                        slot.getCommitteeId(),
                                        command.getEvaluatorId(),
                                        command.getEvaluatorRole(),
                                        updatedAssessments,
                                        scoreCalculator.calculateTotalScore(updatedAssessments),
                                        EvaluationStatus.SUBMITTED,
                                        Instant.now()
                                );

                                return evaluationRepositoryPort.save(evaluation)
                                        .flatMap(saved ->
                                                eventPublisherPort.publishSubmitted(
                                                        new EvaluationSubmittedEvent(
                                                                saved.getEvaluationId(),
                                                                saved.getThesisId(),
                                                                saved.getStageId(),
                                                                saved.getCommitteeId(),
                                                                saved.getEvaluatorId(),
                                                                saved.getTotalScore(),
                                                                saved.getSubmittedAt()
                                                        )
                                                ).thenReturn(saved)
                                        )
                                        .flatMap(saved ->
                                                eventPublisherPort.publishCompleted(
                                                        new EvaluationCompletedEvent(
                                                                saved.getEvaluationId(),
                                                                saved.getThesisId(),
                                                                saved.getStageId(),
                                                                saved.getCommitteeId(),
                                                                saved.getTotalScore(),
                                                                saved.getSubmittedAt()
                                                        )
                                                ).thenReturn(saved)
                                        )
                                        .flatMap(saved -> {
                                            if ("FINAL_DEFENSE".equalsIgnoreCase(stageDefinition.getStageName())) {
                                                return eventPublisherPort.publishDefenseEvaluated(
                                                        new DefenseEvaluatedEvent(
                                                                saved.getEvaluationId(),
                                                                saved.getThesisId(),
                                                                saved.getStageId(),
                                                                saved.getCommitteeId(),
                                                                saved.getTotalScore(),
                                                                saved.getSubmittedAt()
                                                        )
                                                ).thenReturn(saved);
                                            }
                                            return Mono.just(saved);
                                        })
                                        .map(saved -> new EvaluationResponse(
                                                saved.getEvaluationId(),
                                                saved.getThesisId(),
                                                saved.getStageId(),
                                                saved.getTotalScore(),
                                                saved.getStatus()
                                        ));
                            });
                });
    }

    @Override
    public Mono<EvaluationResponse> update(UpdateEvaluationCommand command) {
        return evaluationRepositoryPort.findById(command.getEvaluationId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Evaluation not found")))
                .flatMap(existing -> {
                    List<CriterionAssessment> updatedAssessments = command.getAssessments()
                            .stream()
                            .peek(a -> a.setWeightedScore(
                                    scoreCalculator.calculateWeighted(
                                            a.getGivenScore(),
                                            a.getMaxScore(),
                                            a.getWeight()
                                    )
                            ))
                            .toList();

                    existing.setAssessments(updatedAssessments);
                    existing.setTotalScore(scoreCalculator.calculateTotalScore(updatedAssessments));
                    existing.setStatus(EvaluationStatus.UPDATED);
                    existing.setSubmittedAt(Instant.now());

                    return evaluationRepositoryPort.save(existing)
                            .flatMap(saved ->
                                    eventPublisherPort.publishUpdated(
                                            new EvaluationUpdatedEvent(
                                                    saved.getEvaluationId(),
                                                    saved.getThesisId(),
                                                    saved.getStageId(),
                                                    saved.getCommitteeId(),
                                                    saved.getTotalScore(),
                                                    saved.getSubmittedAt()
                                            )
                                    ).thenReturn(saved)
                            )
                            .map(saved -> new EvaluationResponse(
                                    saved.getEvaluationId(),
                                    saved.getThesisId(),
                                    saved.getStageId(),
                                    saved.getTotalScore(),
                                    saved.getStatus()
                            ));
                });
    }
}