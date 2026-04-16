package mn.num.edu.workflow_service.adapters.out.persistence.adapter;

import mn.num.edu.workflow_service.adapters.out.persistence.entity.StageCriterionEntity;
import mn.num.edu.workflow_service.adapters.out.persistence.repository.SpringDataStageCriterionRepository;
import mn.num.edu.workflow_service.application.port.out.CriterianRepositoryPort;
import mn.num.edu.workflow_service.domain.model.StageCriterion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class CriterionPersistenceAdapter implements CriterianRepositoryPort {

    private final SpringDataStageCriterionRepository repository;

    public CriterionPersistenceAdapter(SpringDataStageCriterionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<StageCriterion> save(StageCriterion criterion) {
        return repository.save(toEntity(criterion))
                .map(this::toDomain);
    }

    @Override
    public Flux<StageCriterion> saveAll(List<StageCriterion> criteria) {
        return repository.saveAll(criteria.stream().map(this::toEntity).toList())
                .map(this::toDomain);
    }

    @Override
    public Flux<StageCriterion> findByStageId(String stageId) {
        return repository.findByStageId(stageId)
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteByStageId(String stageId) {
        return repository.deleteByStageId(stageId).then();
    }

    private StageCriterionEntity toEntity(StageCriterion criterion) {
        return new StageCriterionEntity(
                criterion.getId(),
                criterion.getStageId(),
                criterion.getName(),
                criterion.getMaxScore(),
                criterion.getDescription()
        );
    }

    private StageCriterion toDomain(StageCriterionEntity entity) {
        return new StageCriterion(
                entity.getId(),
                entity.getStageId(),
                entity.getName(),
                entity.getMaxScore(),
                entity.getDescription()
        );
    }
}