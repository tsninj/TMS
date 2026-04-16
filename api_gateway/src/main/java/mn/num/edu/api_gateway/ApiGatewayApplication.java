package mn.num.edu.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway - TMS системийн нэгдсэн орох цэг.
 * Бүх client хүсэлтийг хүлээн авч, тохирох microservice рүү дамжуулна.
 * Port: 8080
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
