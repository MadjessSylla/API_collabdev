package odk.groupe4.ApiCollabDev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import odk.groupe4.ApiCollabDev.dto.*;
import odk.groupe4.ApiCollabDev.exception.GlobalExceptionHandler;
import odk.groupe4.ApiCollabDev.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/participants")
@Tag(name = "Participants", description = "API de gestion des participants aux projets")
public class ParticipantController {

    private final ParticipantService participantService;

    @Autowired
    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    // ======================= CRUD ===========================

    @Operation(
            summary = "Récupérer tous les participants",
            description = "Retourne la liste complète de tous les participants"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste des participants récupérée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ParticipantResponseDto.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<ParticipantResponseDto>> getAllParticipants() {
        List<ParticipantResponseDto> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    @Operation(
            summary = "Récupérer un participant par ID",
            description = "Retourne les détails d'un participant spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Participant trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantResponseDto> getParticipantById(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        ParticipantResponseDto participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @Operation(
            summary = "Récupérer les participations d'un contributeur",
            description = "Retourne toutes les participations d'un contributeur spécifique"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des participations du contributeur récupérée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Contributeur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/contributeur/{idContributeur}")
    public ResponseEntity<List<ParticipantResponseDto>> getParticipantsByContributeur(
            @Parameter(description = "ID du contributeur", required = true, example = "1")
            @PathVariable int idContributeur) {
        List<ParticipantResponseDto> participants = participantService.getParticipantsByContributeur(idContributeur);
        return ResponseEntity.ok(participants);
    }

    @Operation(
            summary = "Mettre à jour un participant",
            description = "Met à jour les informations d'un participant"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Participant mis à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ParticipantResponseDto> mettreAJourParticipant(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "Nouvelles données du participant", required = true)
            @Valid @RequestBody ParticipantDto participantDto) {
        ParticipantResponseDto participant = participantService.mettreAJourParticipant(id, participantDto);
        return ResponseEntity.ok(participant);
    }

    @Operation(
            summary = "Supprimer un participant",
            description = "Supprime un participant d'un projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Participant supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerParticipant(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        participantService.supprimerParticipant(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Participant =====================

    @Operation(
            summary = "Envoyer une demande de participation",
            description = "Permet à un contributeur d'envoyer une demande pour participer à un projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Demande de participation envoyée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données de participation invalides ou demande déjà envoyée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet ou contributeur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/projet/{idProjet}/contributeur/{idContributeur}")
    public ResponseEntity<ParticipantResponseDto> envoyerDemandeParticipation(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet,
            @Parameter(description = "ID du contributeur", required = true, example = "1")
            @PathVariable int idContributeur,
            @Parameter(description = "Données de la demande de participation", required = true)
            @Valid @RequestBody ParticipantDto demandeDto) {
        ParticipantResponseDto participant = participantService.envoyerDemande(idProjet, idContributeur, demandeDto);
        return new ResponseEntity<>(participant, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Débloquer l'accès à un projet",
            description = "Permet à un participant d'utiliser ses coins pour débloquer l'accès à un projet selon son niveau de complexité"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Accès débloqué avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solde insuffisant, accès déjà débloqué ou demande non acceptée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}/projet/{idProjet}/unlock")
    public ResponseEntity<ParticipantResponseDto> debloquerAcces(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id,
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet) {
        ParticipantResponseDto participant = participantService.debloquerAcces(id, idProjet);
        return ResponseEntity.ok(participant);
    }

    @Operation(
            summary = "Réserver une fonctionnalité",
            description = "Permet à un participant de réserver une fonctionnalité disponible d'un projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fonctionnalité réservée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FonctionnaliteDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant ou fonctionnalité non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Fonctionnalité déjà réservée ou participant a déjà une fonctionnalité",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{idParticipant}/fonctionnalite/{idFonctionnalite}/reserve")
    public ResponseEntity<FonctionnaliteDto> reserverFonctionnalite(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int idParticipant,
            @Parameter(description = "ID de la fonctionnalité", required = true, example = "1")
            @PathVariable int idFonctionnalite) {
        FonctionnaliteDto fonctionnalite = participantService.reserverFonctionnalite(idParticipant, idFonctionnalite);
        return ResponseEntity.ok(fonctionnalite);
    }

    @Operation(
            summary = "Récupérer l'historique d'acquisition d'un participant",
            description = "Retourne l'historique des contributions validées et des badges acquis par un participant"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Historique récupéré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = HistAcquisitionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}/historique")
    public ResponseEntity<HistAcquisitionDto> getHistoriqueAcquisition(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        HistAcquisitionDto historique = participantService.getHistAcquisition(id);
        return ResponseEntity.ok(historique);
    }

    @Operation(
            summary = "Récupérer les badges gagnés par un participant",
            description = "Retourne la liste des badges qu’un participant a obtenus, éventuellement filtrés par projet."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des badges récupérée avec succès",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BadgeRewardDto.class)))),
            @ApiResponse(responseCode = "404", description = "Participant ou projet introuvable"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @GetMapping("/{idParticipant}/badges")
    public ResponseEntity<List<BadgeRewardDto>> getBadgesParticipant(
            @Parameter(description = "ID du participant", required = true, example = "12")
            @PathVariable int idParticipant,

            @Parameter(description = "ID du projet (facultatif pour filtrer)", example = "5")
            @RequestParam(required = false) Integer idProjet
    ) {
        List<BadgeRewardDto> badges = participantService.getBadgesGagnes(idParticipant, idProjet);
        return ResponseEntity.ok(badges);
    }

    @Operation(
            summary = "Récupérer les contributions d'un participant",
            description = "Retourne toutes les contributions soumises par un participant spécifique"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Contributions récupérées avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ContributionDto.class)
            )
    )
    @GetMapping("/{id}/contributions")
    public ResponseEntity<List<ContributionDto>> getContributionsParticipant(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        List<ContributionDto> contributions = participantService.afficherContributionsParticipant(id);
        return ResponseEntity.ok(contributions);
    }

    @Operation(
            summary = "Récupérer la progression des badges d'un participant",
            description = "Retourne tous les badges disponibles et indique lesquels ont été atteints par le participant"
    )
    @GetMapping("/{id}/badges/progression")
    public ResponseEntity<List<BadgeSeuilDto>> getProgressionBadges(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        List<BadgeSeuilDto> progression = participantService.getProgressionBadges(id);
        return ResponseEntity.ok(progression);
    }

    // ==================== Gestionnaire =====================

    @Operation(
            summary = "Vérifier le statut de candidature",
            description = "Vérifie si un contributeur a déjà envoyé une demande de candidature à un projet et retourne le statut de cette candidature"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Statut de candidature récupéré avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CandidatureStatusDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet ou contributeur non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/projet/{idProjet}/contributeur/{idContributeur}/candidature/status")
    public ResponseEntity<CandidatureStatusDto> verifierCandidature(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet,
            @Parameter(description = "ID du contributeur", required = true, example = "1")
            @PathVariable int idContributeur) {
        CandidatureStatusDto status = participantService.verifierCandidature(idProjet, idContributeur);
        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Définir un participant comme gestionnaire du projet",
            description = "Permet de désigner un participant accepté comme gestionnaire du projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Gestionnaire défini avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Participant non accepté ou ne fait pas partie du projet",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Projet ou participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{idParticipant}/projet/{idProjet}/definir-gestionnaire")
    public ResponseEntity<ProjetResponseDto> definirGestionnaire(
            @Parameter(description = "ID du participant à désigner comme gestionnaire", required = true, example = "1")
            @PathVariable int idParticipant,
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet) {
        ProjetResponseDto projet = participantService.definirGestionnaire(idProjet, idParticipant);
        return ResponseEntity.ok(projet);
    }

    @Operation(
            summary = "Accepter une demande de participation",
            description = "Permet d'accepter la demande de participation d'un contributeur à un projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Demande acceptée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La demande a déjà été traitée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}/accept")
    public ResponseEntity<ParticipantResponseDto> accepterDemande(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        ParticipantResponseDto participant = participantService.accepterDemande(id);
        return ResponseEntity.ok(participant);
    }

    @Operation(
            summary = "Refuser une demande de participation",
            description = "Permet de refuser la demande de participation d'un contributeur à un projet"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Demande refusée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ParticipantResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participant non trouvé",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La demande a déjà été traitée",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}/reject")
    public ResponseEntity<ParticipantResponseDto> refuserDemande(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int id) {
        ParticipantResponseDto participant = participantService.refuserDemande(id);
        return ResponseEntity.ok(participant);
    }

    @Operation(
            summary = "Attribuer une tâche à un participant",
            description = "Permet d'attribuer une fonctionnalité spécifique à un participant (action de gestion)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tâche attribuée avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = FonctionnaliteDto.class)
            )
    )
    @PatchMapping("/{idParticipant}/fonctionnalite/{idFonctionnalite}/assign")
    public ResponseEntity<FonctionnaliteDto> attribuerTache(
            @Parameter(description = "ID du participant", required = true, example = "1")
            @PathVariable int idParticipant,
            @Parameter(description = "ID de la fonctionnalité", required = true, example = "1")
            @PathVariable int idFonctionnalite) {
        FonctionnaliteDto fonctionnalite = participantService.attribuerTache(idParticipant, idFonctionnalite);
        return ResponseEntity.ok(fonctionnalite);
    }

    @Operation(
            summary = "Récupérer tous les participants d'un projet",
            description = "Retourne la liste de tous les participants d'un projet spécifique"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Participants récupérés avec succès",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ParticipantResponseDto.class)
            )
    )
    @GetMapping("/projet/{idProjet}")
    public ResponseEntity<List<ParticipantResponseDto>> getParticipantsByProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable int idProjet) {
        List<ParticipantResponseDto> participants = participantService.getParticipantsByProjet(idProjet);
        return ResponseEntity.ok(participants);
    }
}
