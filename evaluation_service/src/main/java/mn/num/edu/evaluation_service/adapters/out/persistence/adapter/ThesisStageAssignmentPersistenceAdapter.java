package mn.num.edu.evaluation_service.adapters.out.persistence.adapter;

import lombok.RequiredArgsConstructor;
import mn.num.edu.evaluation_service.adapters.out.persistence.entity.ThesisStageAssignmentEntity;
import mn.num.edu.evaluation_service.adapters.out.persistence.repository.SpringDataThesisStageAssignmentRepository;
import mn.num.edu.evaluation_service.application.port.out.ThesisStageAssignmentRepositoryPort;
import mn.num.edu.evaluation_service.domain.model.ThesisStageAssignment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ThesisStageAssignmentPersistenceAdapter implements ThesisStageAssignmentRepositoryPort {

    private final SpringDataThesisStageAssignmentRepository repository;

    @Override
    public Mono<ThesisStageAssignment> save(ThesisStageAssignment assignment) {
        ThesisStageAssignmentEntity entity = new ThesisStageAssignmentEntity();
        entity.setAssignmentId(assignment.getAssignmentId());
        entity.setThesisId(assignment.getThesisId());
        entity.setStudentId(assignment.getStudentId());
        entity.setDepartmentId(assignment.getDepartmentId());
        entity.setCommitteeId(assignment.getCommitteeId());
        entity.setThesisApproved(assignment.isThesisApproved());

        return repository.save(entity).map(this::toDomain);
    }

    @Override
    public Mono<ThesisStageAssignment> findByThesisId(String thesisId) {
        return repository.findByThesisId(thesisId).map(this::toDomain);
    }

    @Override
    public Flux<ThesisStageAssignment> findAllByDepartmentId(String departmentId) {
        return repository.findAllByDepartmentId(departmentId).map(this::toDomain);
    }

    private ThesisStageAssignment toDomain(ThesisStageAssignmentEntity entity) {
        return new ThesisStageAssignment(
                entity.getAssignmentId(),
                entity.getThesisId(),
                entity.getStudentId(),
                entity.getDepartmentId(),
                entity.getCommitteeId(),
                Boolean.TRUE.equals(entity.getThesisApproved())
        );
    }
}