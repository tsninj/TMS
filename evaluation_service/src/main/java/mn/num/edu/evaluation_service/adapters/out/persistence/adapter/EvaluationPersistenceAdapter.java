package mn.num.edu.evaluation_service.adapters.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import mn.num.edu.evaluation_service.adapters.out.persistence.entity.CriterionAssessmentEntity;
import mn.num.edu.evaluation_service.adapters.out.persistence.entity.EvaluationEntity;
import mn.num.edu.evaluation_service.adapters.out.persistence.repository.SpringDataCriterionAssessmentRepository;
import mn.num.edu.evaluation_service.adapters.out.persistence.repository.SpringDataEvaluationRepository;
import mn.num.edu.evaluation_service.application.port.out.EvaluationRepositoryPort;
import mn.num.edu.evaluation_service.domain.model.CriterionAssessment;
import mn.num.edu.evaluation_service.domain.model.Evaluation;
import mn.num.edu.evaluation_service.domain.model.EvaluationStatus;
import mn.num.edu.evaluation_service.domain.model.EvaluatorRole;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EvaluationPersistenceAdapter implements EvaluationRepositoryPort {

    private final SpringDataEvaluationRepository evaluationRepository;
    private final SpringDataCriterionAssessmentRepository criterionRepository;

    @Override
    public Mono<Evaluation> save(Evaluation evaluation) {
        EvaluationEntity entity = new EvaluationEntity();
        entity.setEvaluationId(evaluation.getEvaluationId());
        entity.setThesisId(evaluation.getThesisId());
        entity.setStageId(evaluation.getStageId());
        entity.setCommitteeId(evaluation.getCommitteeId());
        entity.setEvaluatorId(evaluation.getEvaluatorId());
        entity.setEvaluatorRole(evaluation.getEvaluatorRole().name());
        entity.setTotalScore(evaluation.getTotalScore());
        entity.setStatus(evaluation.getStatus().name());
        entity.setSubmittedAt(evaluation.getSubmittedAt());

        return evaluationRepository.save(entity)
                .flatMap(saved ->
                        criterionRepository.deleteByEvaluationId(saved.getEvaluationId())
                                .then(Mono.just(saved))
                )
                .flatMap(saved ->
                        Flux.fromIterable(evaluation.getAssessments())
                                .flatMap(assessment -> {
                                    CriterionAssessmentEntity a = new CriterionAssessmentEntity();
                                    a.setId(UUID.randomUUID().toString());
                                    a.setEvaluationId(saved.getEvaluationId());
                                    a.setCriterionId(assessment.getCriterionId());
                                    a.setCriterionName(assessment.getCriterionName());
                                    a.setMaxScore(assessment.getMaxScore());
                                    a.setGivenScore(assessment.getGivenScore());
                                    a.setWeight(assessment.getWeight());
                                    a.setWeightedScore(assessment.getWeightedScore());
                                    a.setComment(assessment.getComment());
                                    return criterionRepository.save(a);
                                })
                                .then(Mono.just(saved))
                )
                .flatMap(saved -> findById(saved.getEvaluationId()));
    }

    @Override
    public Mono<Evaluation> findById(String evaluationId) {
        return evaluationRepository.findById(evaluationId)
                .flatMap(entity ->
                        criterionRepository.findByEvaluationId(entity.getEvaluationId())
                                .map(this::toAssessment)
                                .collectList()
                                .map(assessments -> toDomain(entity, assessments))
                );
    }

    @Override
    public Mono<Boolean> existsByThesisIdAndStageIdAndEvaluatorId(String thesisId, String stageId, String evaluatorId) {
        return evaluationRepository.existsByThesisIdAndStageIdAndEvaluatorId(thesisId, stageId, evaluatorId);
    }

    @Override
    public Flux<Evaluation> findByThesisId(String thesisId) {
        return evaluationRepository.findByThesisId(thesisId)
                .flatMap(entity ->
                        criterionRepository.findByEvaluationId(entity.getEvaluationId())
                                .map(this::toAssessment)
                                .collectList()
                                .map(assessments -> toDomain(entity, assessments))
                );
    }

    private CriterionAssessment toAssessment(CriterionAssessmentEntity entity) {
        return new CriterionAssessment(
                entity.getCriterionId(),
                entity.getCriterionName(),
                entity.getMaxScore(),
                entity.getGivenScore(),
                entity.getWeight(),
                entity.getWeightedScore(),
                entity.getComment()
        );
    }

    private Evaluation toDomain(EvaluationEntity entity, java.util.List<CriterionAssessment> assessments) {
        return new Evaluation(
                entity.getEvaluationId(),
                entity.getThesisId(),
                entity.getStageId(),
                entity.getCommitteeId(),
                entity.getEvaluatorId(),
                EvaluatorRole.valueOf(entity.getEvaluatorRole()),
                assessments,
                entity.getTotalScore(),
                EvaluationStatus.valueOf(entity.getStatus()),
                entity.getSubmittedAt()
        );
    }
}