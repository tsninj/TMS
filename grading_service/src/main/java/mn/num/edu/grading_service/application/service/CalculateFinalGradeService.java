package mn.num.edu.grading_service.application.service;

import mn.num.edu.grading_service.application.port.in.CalculateFinalGradeUseCase;
import mn.num.edu.grading_service.application.port.out.EventPublisherPort;
import mn.num.edu.grading_service.application.port.out.GradeRepositoryPort;
import mn.num.edu.grading_service.domain.event.FinalGradeCalculatedEvent;
import mn.num.edu.grading_service.domain.model.Grade;
import mn.num.edu.grading_service.domain.model.GradeStatus;
import mn.num.edu.grading_service.domain.service.GradeDomainService;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public class CalculateFinalGradeService implements CalculateFinalGradeUseCase {

    private final EvaluationQueryPort evaluationQueryPort;
    private final GradeRepositoryPort gradeRepositoryPort;
    private final EventPublisherPort eventPublisherPort;
    private final GradeDomainService gradeDomainService;

    public CalculateFinalGradeService(GradeRepositoryPort gradeRepositoryPort,
                                      EventPublisherPort eventPublisherPort,
                                      GradeDomainService gradeDomainService) {
        this.evaluationQueryPort = evaluationQueryPort;
        this.gradeRepositoryPort = gradeRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
        this.gradeDomainService = gradeDomainService;
    }

    @Override
    public Mono<Void> calculateForThesis( String thesisId,  String studentId,  String workflowId) {
        return evaluationQueryPort.findStageScoresByThesisId(thesisId)
                .reduce(0.0, Double::sum)
                .flatMap(sum -> {
                    double totalScore = gradeDomainService.calculateTotalScore(sum);
                    GradeStatus status = gradeDomainService.determineStatus(totalScore);

                    Grade grade = new Grade(
                            UUID.randomUUID().toString(),
                            thesisId,
                            studentId,
                            workflowId,
                            null,
                            totalScore,
                            status,
                            LocalDateTime.now()
                    );

                    return gradeRepositoryPort.save(grade)
                            .flatMap(saved -> eventPublisherPort.publish(
                                    "final-grade-calculated",
                                    new FinalGradeCalculatedEvent(
                                            saved.getThesisId(),
                                            saved.getStudentId(),
                                            saved.getWorkflowId(),
                                            saved.getTotalScore(),
                                            saved.getStatus(),
                                            saved.getCalculatedAt()
                                    )
                            ));
                });
    }
}