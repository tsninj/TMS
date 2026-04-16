package mn.num.edu.evaluation_service.adapters.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import mn.num.edu.evaluation_service.application.dto.SubmitEvaluationCommand;
import mn.num.edu.evaluation_service.application.port.in.SubmitEvaluationUseCase;
import mn.num.edu.evaluation_service.application.dto.UpdateEvaluationCommand;
import mn.num.edu.evaluation_service.application.port.in.UpdateEvaluationUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Evaluation API", description = "Evaluation management endpoints")
@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final SubmitEvaluationUseCase submitEvaluationUseCase;
    private final UpdateEvaluationUseCase updateEvaluationUseCase;

    public EvaluationController(
            SubmitEvaluationUseCase submitEvaluationUseCase,
            UpdateEvaluationUseCase updateEvaluationUseCase
    ) {
        this.submitEvaluationUseCase = submitEvaluationUseCase;
        this.updateEvaluationUseCase = updateEvaluationUseCase;
    }

    @Operation(
            summary = "Submit evaluation",
            description = "Submit evaluation for a thesis based on criteria and scores"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluation submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/submit")
    public Mono<ResponseEntity<Void>> submit(
            @Parameter(description = "Evaluation data")
            @RequestBody SubmitEvaluationCommand command
    ) {
        return submitEvaluationUseCase.submit(command)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Operation(
            summary = "Update evaluation",
            description = "Update existing evaluation scores"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evaluation updated successfully"),
            @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @PutMapping("/update")
    public Mono<ResponseEntity<Void>> update(
            @Parameter(description = "Updated evaluation data")
            @RequestBody UpdateEvaluationCommand command
    ) {
        return updateEvaluationUseCase.update(command)
                .thenReturn(ResponseEntity.ok().build());
    }
}