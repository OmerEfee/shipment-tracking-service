package shipmenttrackingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shipment Tracking Service API")
                        .version("1.0.0")
                        .description("State Pattern, Spring Data JPA ve MS SQL Server ile geliştirilmiş kurumsal kargo takip sistemi REST API dokümantasyonu.")
                        .contact(new Contact()
                                .name("LogiTrack Destek Ekibi")
                                .email("support@logitrack.com")));
    }
}