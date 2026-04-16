package mn.num.edu.notification_service.domain.event;
import java.util.UUID;

public record FinalGradeCalculatedEvent(
        UUID thesisId,
        UUID studentId,
        Double finalScore,
        String letterGrade
) {}