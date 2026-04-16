package mn.num.edu.evaluation_service.application.port.out;

import mn.num.edu.evaluation_service.domain.model.Evaluation;
import reactor.core.publisher.*;

import java.util.UUID;

public interface EvaluationRepositoryPort {
    Mono<Evaluation> save(Evaluation evaluation);
    Mono<Evaluation> findById(String evaluationId);
    Mono<Boolean> existsByThesisIdAndStageIdAndEvaluatorId(String thesisId, String stageId, String evaluatorId);
    Flux<Evaluation> findByThesisId(String thesisId);
}