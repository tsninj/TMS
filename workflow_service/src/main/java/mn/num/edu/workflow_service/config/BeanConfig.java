package mn.num.edu.workflow_service.config;

import mn.num.edu.workflow_service.adapters.out.kafka.WorkflowKafkaProducer;
import mn.num.edu.workflow_service.adapters.out.persistence.adapter.CriterionPersistenceAdapter;
import mn.num.edu.workflow_service.adapters.out.persistence.adapter.EvaluatorRolePersistenceAdapter;
import mn.num.edu.workflow_service.adapters.out.persistence.adapter.WorkflowPersistenceAdapter;
import mn.num.edu.workflow_service.adapters.out.persistence.repository.SpringDataStageCriterionRepository;
import mn.num.edu.workflow_service.adapters.out.persistence.repository.SpringDataStageEvaluatorRoleRepository;
import mn.num.edu.workflow_service.adapters.out.persistence.repository.SpringDataWorkflowRepository;
import mn.num.edu.workflow_service.adapters.out.persistence.repository.SpringDataWorkflowStageRepository;
import mn.num.edu.workflow_service.application.service.WorkflowApplicationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class BeanConfig {

    @Bean
    public WorkflowPersistenceAdapter workflowPersistenceAdapter(
            SpringDataWorkflowRepository workflowRepository,
            SpringDataWorkflowStageRepository stageRepository
    ) {
        return new WorkflowPersistenceAdapter(workflowRepository, stageRepository);
    }

    @Bean
    public WorkflowKafkaProducer workflowKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        return new WorkflowKafkaProducer(kafkaTemplate);
    }

    @Bean
    public CriterionPersistenceAdapter criterionPersistenceAdapter(
            SpringDataStageCriterionRepository repository
    ) {
        return new CriterionPersistenceAdapter(repository);
    }

    @Bean
    public EvaluatorRolePersistenceAdapter evaluatorRolePersistenceAdapter(
            SpringDataStageEvaluatorRoleRepository repository
    ) {
        return new EvaluatorRolePersistenceAdapter(repository);
    }

    @Bean
    public WorkflowApplicationService workflowApplicationService(
            WorkflowPersistenceAdapter workflowPersistenceAdapter,
            WorkflowKafkaProducer workflowKafkaProducer,
            CriterionPersistenceAdapter criterionPersistenceAdapter,
            EvaluatorRolePersistenceAdapter evaluatorRolePersistenceAdapter
    ) {
        return new WorkflowApplicationService(
                workflowPersistenceAdapter,
                workflowPersistenceAdapter,
                workflowKafkaProducer,
                criterionPersistenceAdapter,
                evaluatorRolePersistenceAdapter
        );
    }
}