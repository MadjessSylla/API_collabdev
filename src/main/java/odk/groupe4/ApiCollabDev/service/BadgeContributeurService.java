package odk.groupe4.ApiCollabDev.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import odk.groupe4.ApiCollabDev.dao.BadgeContributeurDao;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dto.BadgeContributeurDto;
import odk.groupe4.ApiCollabDev.dto.BadgeContributeurResponseDto;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.BadgeContributeur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Tag(name = "Badge Contributeur", description = "Gestion des badges des contributeurs")
public class BadgeContributeurService {

    private final BadgeContributeurDao badgeContributeurDao;
    private final BadgeDao badgeDao;
    private final ContributeurDao contributeurDao;

    @Autowired
    public BadgeContributeurService(BadgeContributeurDao badgeContributeurDao,
                                    BadgeDao badgeDao,
                                    ContributeurDao contributeurDao) {
        this.badgeContributeurDao = badgeContributeurDao;
        this.badgeDao = badgeDao;
        this.contributeurDao = contributeurDao;
    }

    @Operation(summary = "Afficher tous les badges des contributeurs",
            description = "Récupère la liste de tous les badges attribués aux contributeurs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des badges récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<BadgeContributeurResponseDto> afficherTousLesBadgesContributeurs() {
        return badgeContributeurDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Afficher les badges d'un contributeur",
            description = "Récupère tous les badges d'un contributeur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Badges du contributeur récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<BadgeContributeurResponseDto> afficherBadgesParContributeur(
            @Parameter(description = "ID du contributeur", required = true) int contributeurId) {

        Contributeur contributeur = contributeurDao.findById(contributeurId)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + contributeurId));

        return badgeContributeurDao.findByContributeur(contributeur).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Attribuer un badge à un contributeur",
            description = "Attribue un badge spécifique à un contributeur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Badge attribué avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou badge déjà attribué"),
            @ApiResponse(responseCode = "404", description = "Badge ou contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public BadgeContributeurResponseDto attribuerBadge(BadgeContributeurDto badgeContributeurDto) {
        return attribuerBadge(badgeContributeurDto.getContributeur().getId(),
                badgeContributeurDto.getBadge().getId());
    }

    @Operation(summary = "Attribuer un badge à un contributeur par IDs",
            description = "Attribue un badge à un contributeur en utilisant leurs identifiants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Badge attribué avec succès"),
            @ApiResponse(responseCode = "400", description = "Badge déjà attribué au contributeur"),
            @ApiResponse(responseCode = "404", description = "Badge ou contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public BadgeContributeurResponseDto attribuerBadge(
            @Parameter(description = "ID du contributeur", required = true) int idContributeur,
            @Parameter(description = "ID du badge", required = true) int idBadge) {

        // Vérifier si le contributeur a déjà ce badge
        if (badgeContributeurDao.findByContributeurIdAndBadgeId(idContributeur, idBadge).isPresent()) {
            throw new RuntimeException("Le contributeur a déjà ce badge");
        }

        // Récupération des informations du badge et du contributeur
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new IllegalArgumentException("Badge non trouvé avec l'ID: " + idBadge));

        Contributeur contributeur = contributeurDao.findById(idContributeur)
                .orElseThrow(() -> new IllegalArgumentException("Contributeur non trouvé avec l'ID: " + idContributeur));

        // Création d'un nouvel objet BadgeContributeur
        BadgeContributeur badgeContributeur = new BadgeContributeur();
        badgeContributeur.setBadge(badge);
        badgeContributeur.setContributeur(contributeur);
        badgeContributeur.setDateAcquisition(LocalDate.now());

        // Enregistrement dans la base de données
        BadgeContributeur savedBadgeContributeur = badgeContributeurDao.save(badgeContributeur);

        return mapToResponseDto(savedBadgeContributeur);
    }

    @Operation(summary = "Supprimer un badge d'un contributeur",
            description = "Retire un badge spécifique d'un contributeur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Badge supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Badge contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public void supprimerBadgeContributeur(
            @Parameter(description = "ID de l'association badge-contributeur", required = true) int id) {

        BadgeContributeur badgeContributeur = badgeContributeurDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge contributeur non trouvé avec l'ID: " + id));

        badgeContributeurDao.delete(badgeContributeur);
    }

    @Operation(summary = "Compter les badges d'un contributeur",
            description = "Retourne le nombre total de badges d'un contributeur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nombre de badges récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public long compterBadgesContributeur(
            @Parameter(description = "ID du contributeur", required = true) int contributeurId) {

        if (!contributeurDao.existsById(contributeurId)) {
            throw new RuntimeException("Contributeur non trouvé avec l'ID: " + contributeurId);
        }

        return badgeContributeurDao.countByContributeurId(contributeurId);
    }

    private BadgeContributeurResponseDto mapToResponseDto(BadgeContributeur badgeContributeur) {
        return new BadgeContributeurResponseDto(
                badgeContributeur.getId(),
                badgeContributeur.getDateAcquisition(),
                badgeContributeur.getBadge().getId(),
                badgeContributeur.getBadge().getType(),
                badgeContributeur.getBadge().getDescription(),
                badgeContributeur.getBadge().getCoin_recompense(),
                badgeContributeur.getContributeur().getId(),
                badgeContributeur.getContributeur().getNom(),
                badgeContributeur.getContributeur().getPrenom()
        );
    }
}
