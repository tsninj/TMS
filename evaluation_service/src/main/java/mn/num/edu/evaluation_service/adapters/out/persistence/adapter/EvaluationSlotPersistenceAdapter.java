package mn.num.edu.evaluation_service.adapters.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import mn.num.edu.evaluation_service.adapters.out.persistence.entity.EvaluationSlotEntity;
import mn.num.edu.evaluation_service.adapters.out.persistence.repository.SpringDataEvaluationSlotRepository;
import mn.num.edu.evaluation_service.application.port.out.EvaluationSlotRepositoryPort;
import mn.num.edu.evaluation_service.domain.model.EvaluationSlot;
import mn.num.edu.evaluation_service.domain.model.StageStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EvaluationSlotPersistenceAdapter implements EvaluationSlotRepositoryPort {

    private final SpringDataEvaluationSlotRepository repository;

    @Override
    public Mono<EvaluationSlot> save(EvaluationSlot slot) {
        EvaluationSlotEntity entity = new EvaluationSlotEntity();
        entity.setSlotId(slot.getSlotId());
        entity.setThesisId(slot.getThesisId());
        entity.setStageId(slot.getStageId());
        entity.setDepartmentId(slot.getDepartmentId());
        entity.setCommitteeId(slot.getCommitteeId());
        entity.setStatus(slot.getStatus().name());
        entity.setOpenedAt(slot.getOpenedAt());
        entity.setClosedAt(slot.getClosedAt());

        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<EvaluationSlot> findByThesisIdAndStageId(String thesisId, String stageId) {
        return repository.findByThesisIdAndStageId(thesisId, stageId).map(this::toDomain);
    }

    @Override
    public Mono<Boolean> existsByThesisIdAndStageId(String thesisId, String stageId) {
        return repository.existsByThesisIdAndStageId(thesisId, stageId);
    }

    @Override
    public Flux<EvaluationSlot> findAllByStageId(String stageId) {
        return repository.findAllByStageId(stageId).map(this::toDomain);
    }

    @Override
    public Flux<EvaluationSlot> findAllByThesisId(String thesisId) {
        return repository.findAllByThesisId(thesisId).map(this::toDomain);
    }

    private EvaluationSlot toDomain(EvaluationSlotEntity entity) {
        return new EvaluationSlot(
                entity.getSlotId(),
                entity.getThesisId(),
                entity.getStageId(),
                entity.getDepartmentId(),
                entity.getCommitteeId(),
                StageStatus.valueOf(entity.getStatus()),
                entity.getOpenedAt(),
                entity.getClosedAt()
        );
    }
}