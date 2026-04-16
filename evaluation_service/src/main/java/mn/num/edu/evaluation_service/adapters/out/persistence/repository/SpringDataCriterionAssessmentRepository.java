package mn.num.edu.evaluation_service.adapters.out.persistence.repository;

import mn.num.edu.evaluation_service.adapters.out.persistence.entity.CriterionAssessmentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SpringDataCriterionAssessmentRepository extends ReactiveCrudRepository<CriterionAssessmentEntity, String> {
    Flux<CriterionAssessmentEntity> findByEvaluationId(String evaluationId);
    Flux<CriterionAssessmentEntity>  deleteByEvaluationId(String evaluationId);
}