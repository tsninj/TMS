package mn.num.edu.evaluation_service.adapters.out.persistence.repository;

import mn.num.edu.evaluation_service.adapters.out.persistence.entity.EvaluationSlotEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpringDataEvaluationSlotRepository
        extends ReactiveCrudRepository<EvaluationSlotEntity, String> {

    Mono<EvaluationSlotEntity> findByThesisIdAndStageId(String thesisId, String stageId);

    Mono<Boolean> existsByThesisIdAndStageId(String thesisId, String stageId);

    Flux<EvaluationSlotEntity> findAllByStageId(String stageId);

    Flux<EvaluationSlotEntity> findAllByThesisId(String thesisId);

    Flux<EvaluationSlotEntity> findAllByDepartmentId(String departmentId);

    Flux<EvaluationSlotEntity> findAllByCommitteeId(String committeeId);
}