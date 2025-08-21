package odk.groupe4.ApiCollabDev.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuration pour Swagger / OpenAPI.
 * Permet de personnaliser la documentation générée automatiquement
 * pour l'API CollabDev.
 */
@Configuration // Indique que cette classe contient des beans de configuration Spring
public class SwaggerConfig {

    /**
     * Déclare un bean OpenAPI personnalisé.
     * Ce bean configure le titre, la version, la description et les informations
     * de contact qui apparaîtront dans la documentation Swagger UI.
     *
     * @return un objet OpenAPI avec les métadonnées de l'API
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // Titre de la documentation
                        .title("API CollabDev - Plateforme de Collaboration")

                        // Version actuelle de l'API
                        .version("1.0.0")

                        // Description détaillée de l'API avec Markdown autorisé
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

                        // Informations de contact de l'équipe
                        .contact(new Contact()
                                .name("Équipe CollabDev - Groupe 4 ODK")
                                .email("contact@collabdev.com")
                                .url("https://github.com/MadjessSylla/API_collabdev"))
                );
    }
}
