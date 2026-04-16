package mn.num.edu.workflow_service.adapters.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import mn.num.edu.workflow_service.application.dto.CreateWorkflowCommand;
import mn.num.edu.workflow_service.application.dto.CreateWorkflowStageCommand;
import mn.num.edu.workflow_service.application.dto.WorkflowResponse;
import mn.num.edu.workflow_service.application.port.in.AddWorkflowStageUseCase;
import mn.num.edu.workflow_service.application.port.in.ActivateStageUseCase;
import mn.num.edu.workflow_service.application.port.in.CloseStageUseCase;
import mn.num.edu.workflow_service.application.port.in.CreateWorkflowUseCase;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Workflow API", description = "Workflow and stage management endpoints")
@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final CreateWorkflowUseCase createWorkflowUseCase;
    private final AddWorkflowStageUseCase addWorkflowStageUseCase;
    private final ActivateStageUseCase activateStageUseCase;
    private final CloseStageUseCase closeStageUseCase;

    public WorkflowController(CreateWorkflowUseCase createWorkflowUseCase,
                              AddWorkflowStageUseCase addWorkflowStageUseCase,
                              ActivateStageUseCase activateStageUseCase,
                              CloseStageUseCase closeStageUseCase) {
        this.createWorkflowUseCase = createWorkflowUseCase;
        this.addWorkflowStageUseCase = addWorkflowStageUseCase;
        this.activateStageUseCase = activateStageUseCase;
        this.closeStageUseCase = closeStageUseCase;
    }

    @Operation(
            summary = "Create workflow",
            description = "Create a new workflow with multiple stages (e.g., Process1, Process2, Pre-defense, Final-defense)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workflow created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public Mono<WorkflowResponse> create(
            @RequestBody CreateWorkflowCommand command
    ) {
        return createWorkflowUseCase.createWorkflow(command);
    }

    @Operation(
            summary = "Add workflow stage",
            description = "Add a stage to workflow (with startDate, endDate, weight, criteria)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stage added successfully"),
            @ApiResponse(responseCode = "404", description = "Workflow not found")
    })
    @PostMapping("/{workflowId}/stages")
    public Mono<WorkflowResponse> addStage(
            @Parameter(description = "Workflow ID")
            @PathVariable String workflowId,

            @RequestBody CreateWorkflowStageCommand command
    ) {
        return addWorkflowStageUseCase.addStage(workflowId, command);
    }

    @Operation(
            summary = "Activate stage",
            description = "Activate workflow stage manually or via scheduler"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stage activated"),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @PatchMapping("/{workflowId}/stages/{stageId}/activate")
    public Mono<WorkflowResponse> activate(
            @Parameter(description = "Workflow ID")
            @PathVariable String workflowId,

            @Parameter(description = "Stage ID")
            @PathVariable String stageId
    ) {
        return activateStageUseCase.activateStage(workflowId, stageId);
    }

    @Operation(
            summary = "Close stage",
            description = "Close workflow stage when deadline reached or manually"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stage closed"),
            @ApiResponse(responseCode = "404", description = "Stage not found")
    })
    @PatchMapping("/{workflowId}/stages/{stageId}/close")
    public Mono<WorkflowResponse> close(
            @Parameter(description = "Workflow ID")
            @PathVariable String workflowId,

            @Parameter(description = "Stage ID")
            @PathVariable String stageId
    ) {
        return closeStageUseCase.closeStage(workflowId, stageId);
    }
}