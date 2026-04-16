package mn.num.edu.thesis_service.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mn.num.edu.thesis_service.application.dto.*;
import mn.num.edu.thesis_service.application.port.in.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Thesis API", description = "Thesis management endpoints")
@RestController
@RequestMapping("/api/theses")
public class ThesisController {

    private final CreateThesisUseCase createThesisUseCase;
    private final UpdateThesisUseCase updateThesisUseCase;
    private final ApproveThesisUseCase approveThesisUseCase;
    private final RejectThesisUseCase rejectThesisUseCase;
    private final ChangeThesisStatusUseCase changeThesisStatusUseCase;
    private final GetThesisUseCase getThesisUseCase;

    public ThesisController(
            CreateThesisUseCase createThesisUseCase,
            UpdateThesisUseCase updateThesisUseCase,
            ApproveThesisUseCase approveThesisUseCase,
            RejectThesisUseCase rejectThesisUseCase,
            ChangeThesisStatusUseCase changeThesisStatusUseCase,
            GetThesisUseCase getThesisUseCase
    ) {
        this.createThesisUseCase = createThesisUseCase;
        this.updateThesisUseCase = updateThesisUseCase;
        this.approveThesisUseCase = approveThesisUseCase;
        this.rejectThesisUseCase = rejectThesisUseCase;
        this.changeThesisStatusUseCase = changeThesisStatusUseCase;
        this.getThesisUseCase = getThesisUseCase;
    }

    @Operation(
            summary = "Create thesis",
            description = "Create a new thesis"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Thesis created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ThesisResponse> create(
            @Valid @RequestBody CreateThesisCommand command
    ) {
        return createThesisUseCase.create(command);
    }

    @Operation(
            summary = "Update thesis",
            description = "Update thesis information by thesis id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thesis updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Thesis not found")
    })
    @PutMapping("/{id}")
    public Mono<ThesisResponse> update(
            @Parameter(description = "Thesis id")
            @PathVariable String id,
            @Valid @RequestBody UpdateThesisCommand command
    ) {
        return updateThesisUseCase.update(id, command);
    }

    @Operation(
            summary = "Approve thesis",
            description = "Approve thesis by thesis id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thesis approved successfully"),
            @ApiResponse(responseCode = "404", description = "Thesis not found")
    })
    @PostMapping("/{id}/approve")
    public Mono<ThesisResponse> approve(
            @Parameter(description = "Thesis id")
            @PathVariable String id
    ) {
        return approveThesisUseCase.approve(id);
    }

    @Operation(
            summary = "Reject thesis",
            description = "Reject thesis by thesis id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thesis rejected successfully"),
            @ApiResponse(responseCode = "404", description = "Thesis not found")
    })
    @PostMapping("/{id}/reject")
    public Mono<ThesisResponse> reject(
            @Parameter(description = "Thesis id")
            @PathVariable String id
    ) {
        return rejectThesisUseCase.reject(id);
    }

    @Operation(
            summary = "Change thesis status",
            description = "Change thesis status by thesis id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thesis status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status request"),
            @ApiResponse(responseCode = "404", description = "Thesis not found")
    })
    @PatchMapping("/{id}/status")
    public Mono<ThesisResponse> changeStatus(
            @Parameter(description = "Thesis id")
            @PathVariable String id,
            @Valid @RequestBody ChangeThesisStatusCommand command
    ) {
        return changeThesisStatusUseCase.changeStatus(id, command);
    }

    @Operation(
            summary = "Get thesis by id",
            description = "Retrieve one thesis by its id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thesis found"),
            @ApiResponse(responseCode = "404", description = "Thesis not found")
    })
    @GetMapping("/{id}")
    public Mono<ThesisResponse> getById(
            @Parameter(description = "Thesis id")
            @PathVariable String id
    ) {
        return getThesisUseCase.getById(id);
    }

    @Operation(
            summary = "Get all theses",
            description = "Retrieve all theses"
    )
    @ApiResponse(responseCode = "200", description = "List of theses returned")
    @GetMapping
    public Flux<ThesisResponse> getAll() {
        return getThesisUseCase.getAll();
    }
}