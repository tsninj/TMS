package mn.num.edu.workflow_service.application.service;

import mn.num.edu.workflow_service.application.dto.CreateStageCriterionCommand;
import mn.num.edu.workflow_service.application.dto.CreateWorkflowCommand;
import mn.num.edu.workflow_service.application.dto.CreateWorkflowStageCommand;
import mn.num.edu.workflow_service.application.dto.WorkflowResponse;
import mn.num.edu.workflow_service.application.port.in.ActivateStageUseCase;
import mn.num.edu.workflow_service.application.port.in.AddWorkflowStageUseCase;
import mn.num.edu.workflow_service.application.port.in.CloseStageUseCase;
import mn.num.edu.workflow_service.application.port.in.CreateWorkflowUseCase;
import mn.num.edu.workflow_service.application.port.out.CriterianRepositoryPort;
import mn.num.edu.workflow_service.application.port.out.EvaluatorRoleRepositoryPort;
import mn.num.edu.workflow_service.application.port.out.WorkflowEventPublisherPort;
import mn.num.edu.workflow_service.application.port.out.WorkflowRepositoryPort;
import mn.num.edu.workflow_service.application.port.out.WorkflowStageRepositoryPort;
import mn.num.edu.workflow_service.domain.event.WorkflowCompletedEvent;
import mn.num.edu.workflow_service.domain.event.WorkflowCreatedEvent;
import mn.num.edu.workflow_service.domain.event.WorkflowStageActivatedEvent;
import mn.num.edu.workflow_service.domain.event.WorkflowStageClosedEvent;
import mn.num.edu.workflow_service.domain.exception.WorkflowNotFoundException;
import mn.num.edu.workflow_service.domain.model.EvaluatorRole;
import mn.num.edu.workflow_service.domain.model.StageCriterion;
import mn.num.edu.workflow_service.domain.model.StageStatus;
import mn.num.edu.workflow_service.domain.model.Workflow;
import mn.num.edu.workflow_service.domain.model.WorkflowStage;
import mn.num.edu.workflow_service.domain.model.WorkflowStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class WorkflowApplicationService implements
        CreateWorkflowUseCase,
        AddWorkflowStageUseCase,
        ActivateStageUseCase,
        CloseStageUseCase
         {

    private final WorkflowRepositoryPort workflowRepositoryPort;
    private final WorkflowStageRepositoryPort stageRepositoryPort;
    private final WorkflowEventPublisherPort eventPublisherPort;
    private final CriterianRepositoryPort criterianRepositoryPort;
    private final EvaluatorRoleRepositoryPort evaluatorRoleRepositoryPort;

    public WorkflowApplicationService(
            WorkflowRepositoryPort workflowRepositoryPort,
            WorkflowStageRepositoryPort stageRepositoryPort,
            WorkflowEventPublisherPort eventPublisherPort,
            CriterianRepositoryPort criterianRepositoryPort,
            EvaluatorRoleRepositoryPort evaluatorRoleRepositoryPort
    ) {
        this.workflowRepositoryPort = workflowRepositoryPort;
        this.stageRepositoryPort = stageRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
        this.criterianRepositoryPort = criterianRepositoryPort;
        this.evaluatorRoleRepositoryPort = evaluatorRoleRepositoryPort;
    }

    @Override
    public Mono<WorkflowResponse> createWorkflow(CreateWorkflowCommand command) {
        Workflow workflow = new Workflow(
                UUID.randomUUID().toString(),
                command.departmentId(),
                command.title(),
                WorkflowStatus.DRAFT
        );

        return workflowRepositoryPort.saveWorkflow(workflow)
                .doOnSuccess(saved -> eventPublisherPort.publishWorkflowCreated(
                        new WorkflowCreatedEvent(
                                saved.getId(),
                                saved.getDepartmentId(),
                                saved.getTitle(),
                                Instant.now()
                        )
                ))
                .map(this::toResponseWithoutStages);
    }

    @Override
    public Mono<WorkflowResponse> addStage(String workflowId, CreateWorkflowStageCommand command) {
        return workflowRepositoryPort.findById(workflowId)
                .switchIfEmpty(Mono.error(new WorkflowNotFoundException("Workflow олдсонгүй")))
                .flatMap(workflow -> stageRepositoryPort.findByWorkflowId(workflowId)
                        .collectList()
                        .flatMap(existingStages -> {
                            existingStages.forEach(workflow::addStage);

                            WorkflowStage stage = new WorkflowStage(
                                    UUID.randomUUID().toString(),
                                    workflowId,
                                    command.name(),
                                    command.startDate(),
                                    command.endDate(),
                                    command.weightPercent(),
                                    StageStatus.PLANNED,
                                    command.stageOrder()
                            );

                            if (command.criteria() != null) {
                                for (CreateStageCriterionCommand c : command.criteria()) {
                                    StageCriterion criterion = new StageCriterion(
                                            UUID.randomUUID().toString(),
                                            stage.getId(),
                                            c.name(),
                                            c.maxScore(),
                                            c.description()
                                    );
                                    stage.addCriterion(criterion);
                                }
                            }

                            if (command.allowedEvaluatorRoles() != null) {
                                for (String roleName : command.allowedEvaluatorRoles()) {
                                    stage.addAllowedEvaluatorRole(EvaluatorRole.valueOf(roleName));
                                }
                            }

                            stage.validateCriteriaTotal();
                            stage.validateEvaluatorRoles();

                            workflow.addStage(stage);
                            workflow.validateTotalWeight();

                            return stageRepositoryPort.saveStage(stage)
                                    .thenMany(criterianRepositoryPort.saveAll(stage.getCriteria()))
                                    .thenMany(evaluatorRoleRepositoryPort.saveRoles(stage.getId(), stage.getAllowedEvaluatorRoles()))
                                    .then(loadResponse(workflow));
                        }));
    }

    @Override
    public Mono<WorkflowResponse> activateStage(String workflowId, String stageId) {
        return stageRepositoryPort.findStageById(stageId)
                .switchIfEmpty(Mono.error(new WorkflowNotFoundException("Stage олдсонгүй")))
                .flatMap(stage -> {
                    stage.activate();
                    return stageRepositoryPort.updateStageStatus(stageId, stage.getStatus().name())
                            .then(workflowRepositoryPort.findById(workflowId))
                            .switchIfEmpty(Mono.error(new WorkflowNotFoundException("Workflow олдсонгүй")))
                            .doOnSuccess(workflow -> eventPublisherPort.publishWorkflowStageActivated(
                                    new WorkflowStageActivatedEvent(
                                            workflowId,
                                            stage.getId(),
                                            stage.getName(),
                                            Instant.now()
                                    )
                            ));
                })
                .flatMap(this::loadResponse);
    }

    @Override
    public Mono<WorkflowResponse> closeStage(String workflowId, String stageId) {
        return stageRepositoryPort.findStageById(stageId)
                .switchIfEmpty(Mono.error(new WorkflowNotFoundException("Stage олдсонгүй")))
                .flatMap(stage -> {
                    stage.close();
                    return stageRepositoryPort.updateStageStatus(stageId, stage.getStatus().name())
                            .then(workflowRepositoryPort.findById(workflowId))
                            .switchIfEmpty(Mono.error(new WorkflowNotFoundException("Workflow олдсонгүй")))
                            .doOnSuccess(workflow -> eventPublisherPort.publishWorkflowStageClosed(
                                    new WorkflowStageClosedEvent(
                                            workflowId,
                                            stage.getId(),
                                            stage.getName(),
                                            Instant.now()
                                    )
                            ));
                })
                .flatMap(workflow ->
                        stageRepositoryPort.findByWorkflowId(workflow.getId())
                                .collectList()
                                .flatMap(stages -> {
                                    boolean allClosed = stages.stream()
                                            .allMatch(s -> s.getStatus() == StageStatus.CLOSED);

                                    if (allClosed) {
                                        workflow.complete();
                                        return workflowRepositoryPort.saveWorkflow(workflow)
                                                .doOnSuccess(saved -> eventPublisherPort.publishWorkflowCompleted(
                                                        new WorkflowCompletedEvent(
                                                                saved.getId(),
                                                                saved.getDepartmentId(),
                                                                Instant.now()
                                                        )
                                                ));
                                    }
                                    return Mono.just(workflow);
                                })
                )
                .flatMap(this::loadResponse);
    }

    private Mono<WorkflowResponse> loadResponse(Workflow workflow) {
        return stageRepositoryPort.findByWorkflowId(workflow.getId())
                .sort(Comparator.comparingInt(WorkflowStage::getStageOrder))
                .flatMap(this::toStageItem)
                .collectList()
                .map(stageItems -> new WorkflowResponse(
                        workflow.getId(),
                        workflow.getDepartmentId(),
                        workflow.getTitle(),
                        workflow.getStatus().name(),
                        stageItems
                ));
    }

    private Mono<WorkflowResponse.StageItem> toStageItem(WorkflowStage stage) {
        Mono<List<WorkflowResponse.CriterionItem>> criteriaMono =
                criterianRepositoryPort.findByStageId(stage.getId())
                        .map(criterion -> new WorkflowResponse.CriterionItem(
                                criterion.getId(),
                                criterion.getName(),
                                criterion.getMaxScore(),
                                criterion.getDescription()
                        ))
                        .collectList();

        Mono<List<String>> rolesMono =
                evaluatorRoleRepositoryPort.findRolesByStageId(stage.getId())
                        .map(Enum::name)
                        .collectList();

        return Mono.zip(criteriaMono, rolesMono)
                .map(tuple -> new WorkflowResponse.StageItem(
                        stage.getId(),
                        stage.getName(),
                        stage.getStartDate(),
                        stage.getEndDate(),
                        stage.getWeightPercent(),
                        stage.getStatus().name(),
                        tuple.getT2(),
                        tuple.getT1()
                ));
    }

    private WorkflowResponse toResponseWithoutStages(Workflow workflow) {
        return new WorkflowResponse(
                workflow.getId(),
                workflow.getDepartmentId(),
                workflow.getTitle(),
                workflow.getStatus().name(),
                List.of()
        );
    }
}