package odk.groupe4.ApiCollabDev.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API CollabDev - Plateforme de Collaboration")
                        .version("1.0.0")
                        .description("""
                                API REST complète pour la plateforme de collaboration de développement CollabDev.
                                
                                Cette API permet de gérer :
                                - Les utilisateurs (contributeurs et administrateurs)
                                - Les projets collaboratifs
                                - Les participations aux projets
                                - Les contributions et leur validation
                                - Le système de récompenses (coins et badges)
                                - Les fonctionnalités des projets
                                
                                ## Authentification
                                L'API utilise un système d'authentification basé sur les sessions.
                                
                                ## Codes de statut
                                - 200: Succès
                                - 201: Ressource créée
                                - 204: Succès sans contenu
                                - 400: Erreur de validation
                                - 401: Non authentifié
                                - 403: Non autorisé
                                - 404: Ressource non trouvée
                                - 409: Conflit (ressource déjà existante)
                                - 500: Erreur serveur
                                """)
                        .contact(new Contact()
                                .name("Équipe CollabDev - Groupe 4 ODK")
                                .email("contact@collabdev.com")
                                .url("https://github.com/MadjessSylla/API_collabdev"))
                        );
    }
}