package mn.num.edu.workflow_service.application.port.out;

import mn.num.edu.workflow_service.domain.model.EvaluatorRole;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

public interface EvaluatorRoleRepositoryPort {

    Flux<EvaluatorRole> findRolesByStageId(String stageId);

    Flux<EvaluatorRole> saveRoles(String stageId, Set<EvaluatorRole> roles);

    Mono<Void> deleteByStageId(String stageId);
}