package mn.num.edu.evaluation_service.application.port.in;

import mn.num.edu.evaluation_service.application.dto.EvaluationResponse;
import mn.num.edu.evaluation_service.application.dto.SubmitEvaluationCommand;
import reactor.core.publisher.Mono;

public interface SubmitEvaluationUseCase {
    Mono<EvaluationResponse> submit(SubmitEvaluationCommand command);
}