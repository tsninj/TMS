package mn.num.edu.workflow_service.adapters.out.persistence.repository;

import mn.num.edu.workflow_service.adapters.out.persistence.entity.StageCriterionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface SpringDataStageCriterionRepository
        extends ReactiveCrudRepository<StageCriterionEntity, String> {

    Flux<StageCriterionEntity> findByStageId(String stageId);

    Flux<StageCriterionEntity> deleteByStageId(String stageId);
}