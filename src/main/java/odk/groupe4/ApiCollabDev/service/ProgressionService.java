package odk.groupe4.ApiCollabDev.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import odk.groupe4.ApiCollabDev.dao.BadgeContributeurDao;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.ProgressionBadgeDto;
import odk.groupe4.ApiCollabDev.dto.ProgressionContributeurDto;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.BadgeContributeur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Tag(name = "Progression Contributeur", description = "Gestion de la progression des contributeurs vers les badges")
public class ProgressionService {

    private final ContributeurDao contributeurDao;
    private final BadgeDao badgeDao;
    private final BadgeContributeurDao badgeContributeurDao;
    private final ContributionDao contributionDao;

    @Autowired
    public ProgressionService(ContributeurDao contributeurDao,
                              BadgeDao badgeDao,
                              BadgeContributeurDao badgeContributeurDao,
                              ContributionDao contributionDao) {
        this.contributeurDao = contributeurDao;
        this.badgeDao = badgeDao;
        this.badgeContributeurDao = badgeContributeurDao;
        this.contributionDao = contributionDao;
    }

    @Operation(summary = "Obtenir la progression d'un contributeur",
            description = "Récupère la progression complète d'un contributeur vers tous les badges disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Progression récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Contributeur non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ProgressionContributeurDto obtenirProgressionContributeur(
            @Parameter(description = "ID du contributeur", required = true) int contributeurId) {

        // Récupérer le contributeur
        Contributeur contributeur = contributeurDao.findById(contributeurId)
                .orElseThrow(() -> new RuntimeException("Contributeur non trouvé avec l'ID: " + contributeurId));

        // Calculer le nombre total de contributions validées
        int totalContributionsValidees = calculerContributionsValidees(contributeur);

        // Récupérer tous les badges disponibles triés par seuil croissant
        List<Badge> tousLesBadges = badgeDao.findAllOrderByNombreContributionAsc();

        // Récupérer les badges déjà obtenus par le contributeur
        List<BadgeContributeur> badgesObtenus = badgeContributeurDao.findByContributeur(contributeur);
        Set<Integer> badgeIdsObtenus = badgesObtenus.stream()
                .map(bc -> bc.getBadge().getId())
                .collect(Collectors.toSet());

        // Calculer la progression pour chaque badge
        List<ProgressionBadgeDto> progressionBadges = new ArrayList<>();
        ProgressionBadgeDto prochainBadge = null;

        for (Badge badge : tousLesBadges) {
            boolean dejaObtenu = badgeIdsObtenus.contains(badge.getId());
            int contributionsRestantes = Math.max(0, badge.getNombreContribution() - totalContributionsValidees);
            double pourcentageProgression = Math.min(100.0,
                    (double) totalContributionsValidees / badge.getNombreContribution() * 100);

            ProgressionBadgeDto progressionBadge = new ProgressionBadgeDto(
                    badge.getId(),
                    badge.getType(),
                    badge.getDescription(),
                    badge.getNombreContribution(),
                    totalContributionsValidees,
                    contributionsRestantes,
                    pourcentageProgression,
                    badge.getCoin_recompense(),
                    dejaObtenu,
                    false // sera défini plus tard pour le prochain badge
            );

            progressionBadges.add(progressionBadge);

            // Identifier le prochain badge à débloquer
            if (!dejaObtenu && prochainBadge == null && contributionsRestantes > 0) {
                prochainBadge = progressionBadge;
                prochainBadge.setProchainBadge(true);
            }
        }

        // Calculer le total des coins gagnés via les badges
        int totalCoinsGagnes = badgesObtenus.stream()
                .mapToInt(bc -> bc.getBadge().getCoin_recompense())
                .sum();

        return new ProgressionContributeurDto(
                contributeur.getId(),
                contributeur.getNom(),
                contributeur.getPrenom(),
                totalContributionsValidees,
                badgesObtenus.size(),
                totalCoinsGagnes,
                prochainBadge,
                progressionBadges
        );
    }

    @Operation(summary = "Obtenir le prochain badge à débloquer",
            description = "Récupère uniquement les informations du prochain badge que le contributeur peut débloquer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prochain badge récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Contributeur non trouvé ou tous les badges déjà obtenus"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ProgressionBadgeDto obtenirProchainBadge(
            @Parameter(description = "ID du contributeur", required = true) int contributeurId) {

        ProgressionContributeurDto progression = obtenirProgressionContributeur(contributeurId);

        if (progression.getProchainBadge() == null) {
            throw new RuntimeException("Aucun badge restant à débloquer pour ce contributeur");
        }

        return progression.getProchainBadge();
    }

    @Operation(summary = "Obtenir les badges par seuil",
            description = "Récupère tous les badges triés par seuil de contributions requis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Badges récupérés avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<ProgressionBadgeDto> obtenirBadgesParSeuil(
            @Parameter(description = "ID du contributeur pour calculer la progression", required = true) int contributeurId) {

        ProgressionContributeurDto progression = obtenirProgressionContributeur(contributeurId);
        return progression.getTousLesBadges();
    }

    /**
     * Calcule le nombre total de contributions validées d'un contributeur
     */
    private int calculerContributionsValidees(Contributeur contributeur) {
        int total = 0;
        for (Participant participation : contributeur.getParticipations()) {
            total += contributionDao.findByParticipantIdAndStatus(
                    participation.getId(),
                    ContributionStatus.VALIDE
            ).size();
        }
        return total;
    }
}
