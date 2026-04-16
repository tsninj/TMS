package mn.num.edu.workflow_service.adapters.out.persistence.adapter;

import mn.num.edu.workflow_service.adapters.out.persistence.entity.WorkflowStageEvaluatorRoleEntity;
import mn.num.edu.workflow_service.adapters.out.persistence.repository.SpringDataStageEvaluatorRoleRepository;
import mn.num.edu.workflow_service.application.port.out.EvaluatorRoleRepositoryPort;
import mn.num.edu.workflow_service.domain.model.EvaluatorRole;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

public class EvaluatorRolePersistenceAdapter implements EvaluatorRoleRepositoryPort {

    private final SpringDataStageEvaluatorRoleRepository repository;

    public EvaluatorRolePersistenceAdapter(SpringDataStageEvaluatorRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flux<EvaluatorRole> findRolesByStageId(String stageId) {
        return repository.findByStageId(stageId)
                .map(entity -> EvaluatorRole.valueOf(entity.getEvaluatorRole()));
    }

    @Override
    public Flux<EvaluatorRole> saveRoles(String stageId, Set<EvaluatorRole> roles) {
        return repository.saveAll(
                        roles.stream()
                                .map(role -> new WorkflowStageEvaluatorRoleEntity(
                                        UUID.randomUUID().toString(),
                                        stageId,
                                        role.name()
                                ))
                                .toList()
                )
                .map(entity -> EvaluatorRole.valueOf(entity.getEvaluatorRole()));
    }

    @Override
    public Mono<Void> deleteByStageId(String stageId) {
        return repository.deleteByStageId(stageId).then();
    }
}