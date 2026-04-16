package mn.num.edu.evaluation_service.application.port.out;

import mn.num.edu.evaluation_service.domain.model.StageDefinition;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface StageDefinitionRepositoryPort {
    Mono<StageDefinition> save(StageDefinition stageDefinition);
    Mono<StageDefinition> findByStageId(String stageId);
}