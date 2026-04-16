package mn.num.edu.evaluation_service.config;

import  mn.num.edu.evaluation_service.application.port.in.SubmitEvaluationUseCase;
import  mn.num.edu.evaluation_service.application.port.in.UpdateEvaluationUseCase;
import mn.num.edu.evaluation_service.application.port.out.EvaluationEventPublisherPort;
import mn.num.edu.evaluation_service.application.port.out.EvaluationRepositoryPort;
import mn.num.edu.evaluation_service.application.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ScoreCalculator scoreCalculator() {
        return new ScoreCalculator();
    }

    @Bean
    public EvaluationEligibilityChecker evaluationEligibilityChecker() {
        return new EvaluationEligibilityChecker();
    }

    @Bean
    public EvaluationSlotFactory evaluationSlotFactory() {
        return new EvaluationSlotFactory();
    }
}