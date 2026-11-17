package ma.credit360.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI credit360OpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Crédit 360° - Banque Populaire - Backend API")
                        .description("Référentiel complet et contrat d'API REST pour l'application Crédit 360°. " +
                                "Couvre le cycle de vie complet du dossier de crédit bancaire, de l'instruction à l'octroi.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe Projet Crédit 360°")
                                .email("support-dev@credit360.ma"))
                        .license(new License().name("Banque Populaire - Usage Interne")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
