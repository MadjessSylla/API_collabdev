package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import odk.groupe4.ApiCollabDev.dto.NotificationResponseDto;
import odk.groupe4.ApiCollabDev.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notifications", description = "API pour la gestion des notifications utilisateur - Affichage, lecture, suppression et statistiques des notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Lister toutes les notifications d'un utilisateur",
            description = "Récupère la liste complète des notifications d'un utilisateur spécifique, triées par date de création décroissante (plus récentes en premier)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des notifications récupérée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Exemple de réponse",
                                    value = """
                                    [
                                        {
                                            "id": 1,
                                            "sujet": "Contribution validée",
                                            "message": "Votre contribution pour la fonctionnalité 'Login System' a été validée.",
                                            "dateCreation": "2024-01-15T10:30:00",
                                            "lu": false,
                                            "utilisateurNom": "Diallo",
                                            "utilisateurPrenom": "Bakary",
                                            "utilisateurEmail": "bouba.diallo@email.com"
                                        }
                                    ]
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<List<NotificationResponseDto>> getNotificationsByUtilisateur(
            @Parameter(description = "ID de l'utilisateur", required = true, example = "1")
            @PathVariable int utilisateurId) {

        List<NotificationResponseDto> notifications = notificationService.getNotificationsByUtilisateur(utilisateurId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "Lister les notifications avec pagination",
            description = "Récupère les notifications d'un utilisateur avec pagination pour optimiser les performances sur de gros volumes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page de notifications récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "400", description = "Paramètres de pagination invalides")
    })
    @GetMapping("/utilisateur/{utilisateurId}/paginated")
    public ResponseEntity<Page<NotificationResponseDto>> getNotificationsWithPagination(
            @Parameter(description = "ID de l'utilisateur", required = true)
            @PathVariable int utilisateurId,
            @Parameter(description = "Numéro de la page (commence à 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<NotificationResponseDto> notifications = notificationService.getNotificationsByUtilisateurWithPagination(utilisateurId, page, size);
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "Lister les notifications non lues",
            description = "Récupère uniquement les notifications non lues d'un utilisateur pour afficher les alertes importantes"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications non lues récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/utilisateur/{utilisateurId}/unread")
    public ResponseEntity<List<NotificationResponseDto>> getUnreadNotifications(
            @Parameter(description = "ID de l'utilisateur", required = true)
            @PathVariable int utilisateurId) {

        List<NotificationResponseDto> notifications = notificationService.getUnreadNotifications(utilisateurId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(
            summary = "Compter les notifications non lues",
            description = "Retourne le nombre de notifications non lues pour afficher un badge de notification dans l'interface utilisateur"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre de notifications non lues récupéré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemple de réponse",
                                    value = """
                                    {
                                        "count": 5
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/utilisateur/{utilisateurId}/unread/count")
    public ResponseEntity<Map<String, Long>> countUnreadNotifications(
            @Parameter(description = "ID de l'utilisateur", required = true)
            @PathVariable int utilisateurId) {

        long count = notificationService.countUnreadNotifications(utilisateurId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @Operation(
            summary = "Marquer une notification comme lue",
            description = "Change le statut d'une notification spécifique de 'non lue' à 'lue'"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marquée comme lue avec succès"),
            @ApiResponse(responseCode = "404", description = "Notification non trouvée")
    })
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(
            @Parameter(description = "ID de la notification", required = true)
            @PathVariable int notificationId) {

        NotificationResponseDto notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    @Operation(
            summary = "Marquer toutes les notifications comme lues",
            description = "Change le statut de toutes les notifications non lues d'un utilisateur à 'lue' en une seule opération"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Toutes les notifications marquées comme lues avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PutMapping("/utilisateur/{utilisateurId}/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(
            @Parameter(description = "ID de l'utilisateur", required = true)
            @PathVariable int utilisateurId) {

        notificationService.markAllAsRead(utilisateurId);
        return ResponseEntity.ok(Map.of("message", "Toutes les notifications ont été marquées comme lues"));
    }

    @Operation(
            summary = "Supprimer une notification spécifique",
            description = "Supprime définitivement une notification de la base de données. Cette action est irréversible."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Notification non trouvée"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la suppression")
    })
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Map<String, String>> deleteNotification(
            @Parameter(description = "ID de la notification à supprimer", required = true)
            @PathVariable int notificationId) {

        notificationService.deleteNotification(notificationId);
        return ResponseEntity.ok(Map.of("message", "Notification supprimée avec succès"));
    }

    @Operation(
            summary = "Supprimer toutes les notifications d'un utilisateur",
            description = "Supprime définitivement toutes les notifications d'un utilisateur. Cette action est irréversible et vide complètement la boîte de notifications."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Toutes les notifications supprimées avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur lors de la suppression")
    })
    @DeleteMapping("/utilisateur/{utilisateurId}/all")
    public ResponseEntity<Map<String, String>> deleteAllNotifications(
            @Parameter(description = "ID de l'utilisateur dont supprimer toutes les notifications", required = true)
            @PathVariable int utilisateurId) {

        notificationService.deleteAllNotifications(utilisateurId);
        return ResponseEntity.ok(Map.of("message", "Toutes les notifications ont été supprimées avec succès"));
    }
}
