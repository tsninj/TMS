package mn.num.edu.evaluation_service.adapters.out.persistence.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mn.num.edu.evaluation_service.adapters.out.persistence.entity.StageDefinitionEntity;
import mn.num.edu.evaluation_service.adapters.out.persistence.repository.SpringDataStageDefinitionRepository;
import mn.num.edu.evaluation_service.application.port.out.StageDefinitionRepositoryPort;
import mn.num.edu.evaluation_service.domain.model.StageDefinition;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StageDefinitionPersistenceAdapter implements StageDefinitionRepositoryPort {

    private final SpringDataStageDefinitionRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<StageDefinition> save(StageDefinition stageDefinition) {
        StageDefinitionEntity entity = new StageDefinitionEntity();
        entity.setStageId(stageDefinition.getStageId());
        entity.setWorkflowId(stageDefinition.getWorkflowId());
        entity.setDepartmentId(stageDefinition.getDepartmentId());
        entity.setStageName(stageDefinition.getStageName());
        entity.setStartDate(stageDefinition.getStartDate());
        entity.setEndDate(stageDefinition.getEndDate());
        entity.setWeight(stageDefinition.getWeight());

        try {
            entity.setAllowedEvaluatorRolesJson(
                    objectMapper.writeValueAsString(stageDefinition.getAllowedEvaluatorRoles()));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }

        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<StageDefinition> findByStageId(String stageId) {
        return repository.findById(stageId).map(this::toDomain);
    }

    private StageDefinition toDomain(StageDefinitionEntity entity) {
        List<String> roles;
        try {
            roles = entity.getAllowedEvaluatorRolesJson() == null
                    ? Collections.emptyList()
                    : objectMapper.readValue(
                            entity.getAllowedEvaluatorRolesJson(),
                            new TypeReference<List<String>>() {
                            });
        } catch (Exception e) {
            roles = Collections.emptyList();
        }

        return new StageDefinition(
                entity.getStageId(),
                entity.getWorkflowId(),
                entity.getDepartmentId(),
                entity.getStageName(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getWeight(),
                roles);
    }
}