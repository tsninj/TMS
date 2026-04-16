package mn.num.edu.evaluation_service.adapters.out.persistence.repository;

import mn.num.edu.evaluation_service.adapters.out.persistence.entity.StageDefinitionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SpringDataStageDefinitionRepository
        extends ReactiveCrudRepository<StageDefinitionEntity, String> {

    Flux<StageDefinitionEntity> findAllByDepartmentId(String departmentId);
}

