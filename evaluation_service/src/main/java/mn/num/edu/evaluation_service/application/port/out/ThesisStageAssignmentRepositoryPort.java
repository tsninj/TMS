package mn.num.edu.evaluation_service.application.port.out;

import mn.num.edu.evaluation_service.domain.model.ThesisStageAssignment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ThesisStageAssignmentRepositoryPort {
    Mono<ThesisStageAssignment> save(ThesisStageAssignment assignment);
    Mono<ThesisStageAssignment> findByThesisId(String thesisId);
    Flux<ThesisStageAssignment> findAllByDepartmentId(String departmentId);
}