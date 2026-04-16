package mn.num.edu.evaluation_service.application.port.out;

import mn.num.edu.evaluation_service.domain.model.EvaluationSlot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface EvaluationSlotRepositoryPort {
    Mono<EvaluationSlot> save(EvaluationSlot slot);
    Mono<EvaluationSlot> findByThesisIdAndStageId(String thesisId, String stageId);
    Mono<Boolean> existsByThesisIdAndStageId(String thesisId, String stageId);
    Flux<EvaluationSlot> findAllByStageId(String stageId);
    Flux<EvaluationSlot> findAllByThesisId(String thesisId);
}