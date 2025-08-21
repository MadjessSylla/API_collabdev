package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import odk.groupe4.ApiCollabDev.dto.ProgressionBadgeDto;
import odk.groupe4.ApiCollabDev.dto.ProgressionContributeurDto;
import odk.groupe4.ApiCollabDev.service.ProgressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/progression")
@Tag(
        name = "Progression Contributeur",
        description = "API complète pour suivre la progression des contributeurs vers les badges. " +
                "Permet de connaître le nombre de contributions validées, les badges obtenus, " +
                "et la progression vers les prochains badges à débloquer."
)
public class ProgressionController {

    private final ProgressionService progressionService;

    @Autowired
    public ProgressionController(ProgressionService progressionService) {
        this.progressionService = progressionService;
    }

    @Operation(
            summary = "Obtenir la progression complète d'un contributeur",
            description = "Récupère la progression détaillée d'un contributeur vers tous les badges disponibles. " +
                    "Inclut le nombre de contributions validées, les badges déjà obtenus, " +
                    "le prochain badge à débloquer, et la progression vers chaque badge.",
            tags = {"Progression", "Badge", "Contributeur"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Progression récupérée avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProgressionContributeurDto.class),
                            examples = @ExampleObject(
                                    name = "Progression complète",
                                    value = """
                    {
                        "contributeurId": 5,
                        "contributeurNom": "Dupont",
                        "contributeurPrenom": "Jean",
                        "totalContributionsValidees": 7,
                        "totalBadgesObtenus": 2,
                        "totalCoinsGagnes": 150,
                        "prochainBadge": {
                            "badgeId": 3,
                            "typeBadge": "ARGENT",
                            "description": "Badge pour 10 contributions validées",
                            "seuilRequis": 10,
                            "contributionsValidees": 7,
                            "contributionsRestantes": 3,
                            "pourcentageProgression": 70.0,
                            "coinRecompense": 100,
                            "dejaObtenu": false,
                            "prochainBadge": true
                        },
                        "tousLesBadges": [...]
                    }
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
    public ResponseEntity<ProgressionContributeurDto> obtenirProgressionContributeur(
            @Parameter(
                    description = "Identifiant unique du contributeur dont on veut connaître la progression",
                    required = true,
                    example = "5",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int contributeurId) {

        ProgressionContributeurDto progression = progressionService.obtenirProgressionContributeur(contributeurId);
        return ResponseEntity.ok(progression);
    }

    @Operation(
            summary = "Obtenir le prochain badge à débloquer",
            description = "Récupère uniquement les informations du prochain badge que le contributeur peut débloquer. " +
                    "Indique combien de contributions supplémentaires sont nécessaires et le pourcentage de progression.",
            tags = {"Progression", "Badge", "Objectif"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Prochain badge récupéré avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProgressionBadgeDto.class),
                            examples = @ExampleObject(
                                    name = "Prochain badge",
                                    value = """
                    {
                        "badgeId": 3,
                        "typeBadge": "ARGENT",
                        "description": "Badge pour 10 contributions validées",
                        "seuilRequis": 10,
                        "contributionsValidees": 7,
                        "contributionsRestantes": 3,
                        "pourcentageProgression": 70.0,
                        "coinRecompense": 100,
                        "dejaObtenu": false,
                        "prochainBadge": true
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contributeur non trouvé ou tous les badges déjà obtenus",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Aucun badge restant",
                                    value = """
                    {
                        "error": "Not Found",
                        "message": "Aucun badge restant à débloquer pour ce contributeur",
                        "timestamp": "2024-01-15T10:30:00Z"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/contributeur/{contributeurId}/prochain-badge")
    public ResponseEntity<ProgressionBadgeDto> obtenirProchainBadge(
            @Parameter(
                    description = "Identifiant unique du contributeur",
                    required = true,
                    example = "5",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int contributeurId) {

        ProgressionBadgeDto prochainBadge = progressionService.obtenirProchainBadge(contributeurId);
        return ResponseEntity.ok(prochainBadge);
    }

    @Operation(
            summary = "Obtenir tous les badges avec progression",
            description = "Récupère la liste de tous les badges disponibles avec la progression du contributeur " +
                    "pour chacun d'eux. Les badges sont triés par seuil de contributions requis (croissant).",
            tags = {"Progression", "Badge", "Liste"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des badges avec progression récupérée avec succès",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProgressionBadgeDto.class),
                            examples = @ExampleObject(
                                    name = "Liste des badges avec progression",
                                    value = """
                    [
                        {
                            "badgeId": 1,
                            "typeBadge": "BRONZE",
                            "description": "Premier badge - 1 contribution",
                            "seuilRequis": 1,
                            "contributionsValidees": 7,
                            "contributionsRestantes": 0,
                            "pourcentageProgression": 100.0,
                            "coinRecompense": 50,
                            "dejaObtenu": true,
                            "prochainBadge": false
                        },
                        {
                            "badgeId": 3,
                            "typeBadge": "ARGENT",
                            "description": "Badge pour 10 contributions",
                            "seuilRequis": 10,
                            "contributionsValidees": 7,
                            "contributionsRestantes": 3,
                            "pourcentageProgression": 70.0,
                            "coinRecompense": 100,
                            "dejaObtenu": false,
                            "prochainBadge": true
                        }
                    ]
                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/contributeur/{contributeurId}/badges")
    public ResponseEntity<List<ProgressionBadgeDto>> obtenirBadgesAvecProgression(
            @Parameter(
                    description = "Identifiant unique du contributeur",
                    required = true,
                    example = "5",
                    schema = @Schema(type = "integer", minimum = "1")
            )
            @PathVariable int contributeurId) {

        List<ProgressionBadgeDto> badges = progressionService.obtenirBadgesParSeuil(contributeurId);
        return ResponseEntity.ok(badges);
    }
}
