package mn.num.edu.workflow_service.adapters.out.persistence.repository;

import mn.num.edu.workflow_service.adapters.out.persistence.entity.WorkflowStageEvaluatorRoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface SpringDataStageEvaluatorRoleRepository
        extends ReactiveCrudRepository<WorkflowStageEvaluatorRoleEntity, String> {

    Flux<WorkflowStageEvaluatorRoleEntity> findByStageId(String stageId);

    Flux<WorkflowStageEvaluatorRoleEntity> deleteByStageId(String stageId);
}