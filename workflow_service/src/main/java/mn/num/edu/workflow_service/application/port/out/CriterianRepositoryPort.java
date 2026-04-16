package mn.num.edu.workflow_service.application.port.out;

import mn.num.edu.workflow_service.domain.model.StageCriterion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CriterianRepositoryPort {
    Mono<StageCriterion> save(StageCriterion criterion);

    Flux<StageCriterion> saveAll(List<StageCriterion> criteria);

    Flux<StageCriterion> findByStageId(String stageId);

    Mono<Void> deleteByStageId(String stageId);
}
