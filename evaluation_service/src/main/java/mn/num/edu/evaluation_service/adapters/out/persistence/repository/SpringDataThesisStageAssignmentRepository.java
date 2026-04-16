package mn.num.edu.evaluation_service.adapters.out.persistence.repository;

import mn.num.edu.evaluation_service.adapters.out.persistence.entity.ThesisStageAssignmentEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpringDataThesisStageAssignmentRepository
        extends ReactiveCrudRepository<ThesisStageAssignmentEntity, String> {

    Mono<ThesisStageAssignmentEntity> findByThesisId(String thesisId);

    Flux<ThesisStageAssignmentEntity> findAllByDepartmentId(String departmentId);

    Flux<ThesisStageAssignmentEntity> findAllByCommitteeId(String committeeId);

    Flux<ThesisStageAssignmentEntity> findAllByDepartmentIdAndThesisApproved(String departmentId, Boolean thesisApproved);
}