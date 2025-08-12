package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import odk.groupe4.ApiCollabDev.dto.BadgeContributeurDto;
import odk.groupe4.ApiCollabDev.dto.BadgeContributeurResponseDto;
import odk.groupe4.ApiCollabDev.service.BadgeContributeurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/badge-contributeurs")
@Tag(
        name = "Badge Contributeur",
        description = "API complète pour la gestion des badges des contributeurs. " +
                "Permet d'attribuer, consulter et gérer les badges obtenus par les contributeurs " +
                "en fonction de leurs performances et contributions aux projets."
)
public class BadgeContributeurController {

    private final BadgeContributeurService badgeContributeurService;

    @Autowired
    public BadgeContributeurController(BadgeContributeurService badgeContributeurService) {
        this.badgeContributeurService = badgeContributeurService;
    }

    @Operation(
            summary = "Lister tous les badges des contributeurs",
            description = "Récupère la liste complète de tous les badges attribués aux contributeurs. " +
                    "Cette endpoint permet d'obtenir une vue d'ensemble de toutes les récompenses " +
                    "distribuées dans le système, utile pour les statistiques et l'administration.",
            tags = {"Badge Contributeur", "Administration"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des badges récupérée avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadgeContributeurResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Liste de badges",
                                    value = """
                    [
                        {
                            "id": 1,
                            "contributeurId": 5,
                            "contributeurNom": "Dupont Jean",
                            "badgeId": 2,
                            "badgeType": "BRONZE",
                            "badgeDescription": "Premier badge obtenu",
                            "dateAcquisition": "2024-01-15",
                            "coinRecompense": 50
                        }
                    ]
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur interne du serveur lors de la récupération des badges",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Erreur serveur",
                                    value = """
                    {
                        "error": "Internal Server Error",
                        "message": "Une erreur inattendue s'est produite",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<BadgeContributeurResponseDto>> getAllBadgesContributeurs() {
        List<BadgeContributeurResponseDto> badges = badgeContributeurService.afficherTousLesBadgesContributeurs();
        return ResponseEntity.ok(badges);
    }

    @Operation(
            summary = "Lister les badges d'un contributeur spécifique",
            description = "Récupère tous les badges obtenus par un contributeur donné. " +
                    "Permet de consulter l'historique des récompenses d'un contributeur " +
                    "et de suivre sa progression dans le système de gamification.",
            tags = {"Badge Contributeur", "Profil Contributeur"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Badges du contributeur récupérés avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadgeContributeurResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Badges d'un contributeur",
                                    value = """
                    [
                        {
                            "id": 1,
                            "contributeurId": 5,
                            "contributeurNom": "Dupont Jean",
                            "badgeId": 2,
                            "badgeType": "BRONZE",
                            "badgeDescription": "Premier badge obtenu",
                            "dateAcquisition": "2024-01-15",
                            "coinRecompense": 50
                        },
                        {
                            "id": 2,
                            "contributeurId": 5,
                            "contributeurNom": "Dupont Jean",
                            "badgeId": 3,
                            "badgeType": "ARGENT",
                            "badgeDescription": "Badge pour 5 contributions",
                            "dateAcquisition": "2024-02-01",
                            "coinRecompense": 100
                        }
                    ]
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contributeur non trouvé avec l'ID spécifié",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Contributeur introuvable",
                                    value = """
                    {
                        "error": "Not Found",
                        "message": "Contributeur non trouvé avec l'ID: 999",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/contributeur/{contributeurId}")
    public ResponseEntity<List<BadgeContributeurResponseDto>> getBadgesByContributeur(
            @Parameter(
                    description = "Identifiant unique du contributeur dont on veut récupérer les badges",
                    required = true,
                    example = "5",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int contributeurId) {

        List<BadgeContributeurResponseDto> badges = badgeContributeurService.afficherBadgesParContributeur(contributeurId);
        return ResponseEntity.ok(badges);
    }

    @Operation(
            summary = "Attribuer un badge à un contributeur",
            description = "Attribue un nouveau badge à un contributeur en utilisant les données fournies. " +
                    "Cette opération vérifie que le badge n'est pas déjà attribué au contributeur " +
                    "et met à jour automatiquement le solde de coins du contributeur.",
            tags = {"Badge Contributeur", "Attribution"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Badge attribué avec succès au contributeur",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadgeContributeurResponseDto.class),
                            examples = @ExampleObject(
                                    name = "Badge attribué",
                                    value = """
                    {
                        "id": 15,
                        "contributeurId": 5,
                        "contributeurNom": "Dupont Jean",
                        "badgeId": 3,
                        "badgeType": "ARGENT",
                        "badgeDescription": "Badge pour 5 contributions validées",
                        "dateAcquisition": "2024-01-15",
                        "coinRecompense": 100
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides ou badge déjà attribué au contributeur",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Badge déjà attribué",
                                    value = """
                    {
                        "error": "Bad Request",
                        "message": "Le badge BRONZE est déjà attribué au contributeur ID: 5",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Badge ou contributeur non trouvé dans le système",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Ressource introuvable",
                                    value = """
                    {
                        "error": "Not Found",
                        "message": "Badge avec ID: 999 non trouvé",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping
    public ResponseEntity<BadgeContributeurResponseDto> attribuerBadge(
            @Parameter(
                    description = "Données complètes du badge à attribuer au contributeur",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadgeContributeurDto.class),
                            examples = @ExampleObject(
                                    name = "Attribution de badge",
                                    value = """
                        {
                            "contributeurId": 5,
                            "badgeId": 3,
                            "dateAcquisition": "2024-01-15"
                        }
                        """
                            )
                    )
            )
            @Valid @RequestBody BadgeContributeurDto badgeContributeurDto) {

        BadgeContributeurResponseDto response = badgeContributeurService.attribuerBadge(badgeContributeurDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Attribuer un badge par identifiants",
            description = "Attribue un badge à un contributeur en utilisant directement leurs identifiants. " +
                    "Cette méthode simplifiée est idéale pour les attributions automatiques " +
                    "ou les intégrations avec d'autres systèmes.",
            tags = {"Badge Contributeur", "Attribution", "Automatisation"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Badge attribué avec succès via les identifiants",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadgeContributeurResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Badge déjà attribué au contributeur spécifié",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Duplication de badge",
                                    value = """
                    {
                        "error": "Bad Request",
                        "message": "Le contributeur ID: 5 possède déjà le badge ID: 3",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Badge ou contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PostMapping("/contributeur/{contributeurId}/badge/{badgeId}")
    public ResponseEntity<BadgeContributeurResponseDto> attribuerBadgeParIds(
            @Parameter(
                    description = "Identifiant unique du contributeur qui recevra le badge",
                    required = true,
                    example = "5",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int contributeurId,
            @Parameter(
                    description = "Identifiant unique du badge à attribuer au contributeur",
                    required = true,
                    example = "3",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int badgeId) {

        BadgeContributeurResponseDto response = badgeContributeurService.attribuerBadge(contributeurId, badgeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Supprimer un badge d'un contributeur",
            description = "Retire définitivement un badge spécifique d'un contributeur. " +
                    "Cette opération est irréversible et peut être utilisée en cas d'erreur " +
                    "d'attribution ou de sanctions disciplinaires. " +
                    "Note: Les coins associés au badge ne sont pas automatiquement retirés.",
            tags = {"Badge Contributeur", "Suppression", "Administration"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Badge supprimé avec succès du contributeur"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Association badge-contributeur non trouvée avec l'ID spécifié",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Association introuvable",
                                    value = """
                    {
                        "error": "Not Found",
                        "message": "Association badge-contributeur non trouvée avec l'ID: 999",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur lors de la suppression")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerBadgeContributeur(
            @Parameter(
                    description = "Identifiant unique de l'association badge-contributeur à supprimer",
                    required = true,
                    example = "15",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int id) {

        badgeContributeurService.supprimerBadgeContributeur(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Compter les badges d'un contributeur",
            description = "Retourne le nombre total de badges obtenus par un contributeur spécifique. " +
                    "Cette métrique est utile pour évaluer rapidement le niveau d'engagement " +
                    "et les performances d'un contributeur sans récupérer la liste complète des badges.",
            tags = {"Badge Contributeur", "Statistiques", "Métriques"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Nombre de badges récupéré avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(type = "integer", example = "7"),
                            examples = @ExampleObject(
                                    name = "Nombre de badges",
                                    value = "7"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contributeur non trouvé avec l'ID spécifié",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Contributeur introuvable",
                                    value = """
                    {
                        "error": "Not Found",
                        "message": "Contributeur non trouvé avec l'ID: 999",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur lors du comptage")
    })
    @GetMapping("/contributeur/{contributeurId}/count")
    public ResponseEntity<Long> compterBadgesContributeur(
            @Parameter(
                    description = "Identifiant unique du contributeur dont on veut compter les badges",
                    required = true,
                    example = "5",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int contributeurId) {

        long count = badgeContributeurService.compterBadgesContributeur(contributeurId);
        return ResponseEntity.ok(count);
    }
}
