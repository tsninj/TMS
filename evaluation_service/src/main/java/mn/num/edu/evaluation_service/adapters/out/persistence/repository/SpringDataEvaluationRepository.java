package mn.num.edu.evaluation_service.adapters.out.persistence.repository;

import  mn.num.edu.evaluation_service.adapters.out.persistence.entity.EvaluationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface SpringDataEvaluationRepository extends ReactiveCrudRepository<EvaluationEntity, String> {

    Mono<Boolean> existsByThesisIdAndStageIdAndEvaluatorId(
            String thesisId,
            String stageId,
            String evaluatorId
    );

    Flux<EvaluationEntity> findByThesisId(String thesisId);

    Flux<EvaluationEntity> findByThesisIdAndStageId(String thesisId, String stageId);

    Flux<EvaluationEntity> findByStageId(String stageId);
}