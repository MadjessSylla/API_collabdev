package odk.groupe4.ApiCollabDev.service;


import jakarta.transaction.Transactional;
import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.Profil;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusFeatures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContributionService {
    private final ParametreCoinDao parametreCoinDao;
    private final Participant_projetDao participantDao;
    private final ContributeurDao contributeurDao;
    private final ContributionDao contributionDao;
    private final FonctionnaliteDao fonctionnaliteDao;
    private final BadgeDao badgeDao;
    private final Badge_participantDao badgeParticipantDao;

    @Autowired
    public ContributionService(ParametreCoinDao parametreCoinDao,
                                 Participant_projetDao participantDao,
                                 ContributeurDao contributeurDao,
                                 ContributionDao contributionDao,
                                 FonctionnaliteDao fonctionnaliteDao,
                                 BadgeDao badgeDao,
                                 Badge_participantDao badgeParticipantDao) {
        this.parametreCoinDao = parametreCoinDao;
        this.participantDao = participantDao;
        this.contributeurDao = contributeurDao;
        this.contributionDao = contributionDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.badgeDao = badgeDao;
        this.badgeParticipantDao = badgeParticipantDao;
    }

    @Transactional
    public Contribution MiseAJourStatutContribution(int contributionId, StatusContribution newStatus, int gestionnaireId) {
        // Récupérer la contribution et valider
        Contribution contribution = contributionDao.findById(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Contribution avec ID " + contributionId + " non trouvée"));

        Participant gestionnaire = participantDao.findById(gestionnaireId)
                .orElseThrow(() -> new IllegalArgumentException("Gestionnaire avec ID " + gestionnaireId + " non trouvé"));

        if (!gestionnaire.getProfil().equals(Profil.GESTIONNAIRE)) {
            throw new IllegalArgumentException("Seul un gestionnaire peut mettre à jour le statut d'une contribution");
        }

        // Mettre à jour le statut de la contribution
        contribution.setStatus(newStatus);
        contribution.setParticipantGestionnaire(gestionnaire);

        // Si la contribution est validée, récompenser les coins et mettre à jour le statut de la fonctionnalité
        if (newStatus == StatusContribution.VALIDER) {
            recompenseCoins(contribution.getParticipant());
            MiseAJourStatutFonctionnalite(contribution.getFonctionnalite());
            assignerBadges(contribution.getParticipant());
        }

        return contributionDao.save(contribution);
    }

    private void recompenseCoins(Participant participant) {
        // Recupération de la configuration des coins pour la contribution validée
        ParametreCoin coinConfig = parametreCoinDao.findByTypeEvenementLien("CONTRIBUTION_VALIDEE")
                .orElseThrow(() -> new IllegalStateException("Coin configuration pour CONTRIBUTION_VALIDEE non trouvée"));

        // Mise à jour des coins du participant
        Contributeur contributeur = participant.getContributeur();
        contributeur.setTotalCoin(contributeur.getTotalCoin() + coinConfig.getValeur());
        contributeurDao.save(contributeur);
    }

    private void MiseAJourStatutFonctionnalite(Fonctionnalite fonctionnalite) {
        if (fonctionnalite != null) {
            fonctionnalite.setStatusFeatures(StatusFeatures.TERMINE);
            fonctionnaliteDao.save(fonctionnalite);
        }
    }

    private void assignerBadges(Participant participant) {
        // Compter le nombre de contributions validées
        int NombreValidation = contributionDao.findByParticipantIdAndStatus(participant.getId(), StatusContribution.VALIDER).size();

        // Définition des seuils de badge (par exemple, 5, 10, 20, 50)
        int[] plages = {5, 10, 20, 50};

        for (int plage : plages) {
            if (NombreValidation >= plage) {
                // Vérifie si le badge existe pour ce seuil
                List<Badge> badges = badgeDao.findByNombreContribution(plage);
                if (!badges.isEmpty()) {
                    Badge badge = badges.get(0); // Supposons un badge par seuil
                    // Vérifie si le participant a déjà ce badge
                    boolean hasBadge = badgeParticipantDao.findByParticipantIdAndBadgeId(participant.getId(), badge.getId()).isPresent();
                    if (!hasBadge) {
                        // Attribution du badge au participant
                        Badge_participant badgeParticipant = new Badge_participant();
                        badgeParticipant.setBadge(badge);
                        badgeParticipant.setParticipant(participant);
                        badgeParticipant.setDateAcquisition(LocalDate.now());
                        badgeParticipantDao.save(badgeParticipant);
                    }
                }
            }
        }
    }
}
