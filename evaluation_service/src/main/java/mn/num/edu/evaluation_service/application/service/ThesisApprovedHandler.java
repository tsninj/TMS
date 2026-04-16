package mn.num.edu.evaluation_service.application.service;


import lombok.RequiredArgsConstructor;
import mn.num.edu.evaluation_service.application.port.in.HandleThesisApprovedUseCase;
import mn.num.edu.evaluation_service.application.port.out.ThesisStageAssignmentRepositoryPort;
import mn.num.edu.evaluation_service.domain.event.ThesisApprovedEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ThesisApprovedHandler implements HandleThesisApprovedUseCase {

    private final ThesisStageAssignmentRepositoryPort repositoryPort;

    @Override
    public Mono<Void> handle(ThesisApprovedEvent event) {
        return repositoryPort.findByThesisId(event.thesisId())
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Assignment not found for thesisId: " + event.thesisId())
                ))
                .flatMap(assignment -> {
                    assignment.setThesisApproved(true);
                    return repositoryPort.save(assignment);
                })
                .then();
    }
}