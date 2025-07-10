package odk.groupe4.ApiCollabDev.service;

import jakarta.transaction.Transactional;
import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.ContributionSoumiseDto;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContributionService {
    private final ParametreCoinDao parametreCoinDao;
    private final ParticipantDao participantDao;
    private final ContributeurDao contributeurDao;
    private final ContributionDao contributionDao;
    private final FonctionnaliteDao fonctionnaliteDao;
    private final BadgeDao badgeDao;
    private final BadgeParticipantDao badgeParticipantDao;
    private final NotificationService notificationService;

    @Autowired
    public ContributionService(ParametreCoinDao parametreCoinDao,
                                 ParticipantDao participantDao,
                                 ContributeurDao contributeurDao,
                                 ContributionDao contributionDao,
                                 FonctionnaliteDao fonctionnaliteDao,
                                 BadgeDao badgeDao,
                                 BadgeParticipantDao badgeParticipantDao,
                                 NotificationService notificationService) {
        this.parametreCoinDao = parametreCoinDao;
        this.participantDao = participantDao;
        this.contributeurDao = contributeurDao;
        this.contributionDao = contributionDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.badgeDao = badgeDao;
        this.badgeParticipantDao = badgeParticipantDao;
        this.notificationService = notificationService;
    }

    /**
     * Méthode permettant de soumettre une contribution à une fonctionnalité.
     * @param idFonctionnalite Identifiant de la fonctionnalité à laquelle la contribution est soumise.
     * @param idParticipant Identifiant du participant qui soumet la contribution.
     * @param contribution Détails de la contribution soumise.
     * @return La contribution enregistrée.
     */
    public Contribution soumettreContribution(int idFonctionnalite, int idParticipant, ContributionSoumiseDto contribution) {
        // Vérifier si le participant existe
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        // Récupérer la fonctionnalité associée
        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new IllegalArgumentException("Fonctionnalité non trouvée"));

        // Créer une nouvelle contribution
        Contribution newContribution = new Contribution();
        // Assigner les valeurs de la contribution soumise
        newContribution.setLienUrl(contribution.getLienUrl());
        newContribution.setFileUrl(contribution.getFileUrl());
        newContribution.setStatus(ContributionStatus.ENVOYE);
        newContribution.setDateSoumission(LocalDate.now());
        newContribution.setFonctionnalite(fonctionnalite);
        newContribution.setParticipant(participant);

        // Notifier le gestionnaire de la soumission de la contribution

        // Enregistrer la contribution
        return contributionDao.save(newContribution);

    }

    /**
     * Récupérer toutes les contributions d'un participant spécifique.
     * @param participantId Identifiant du participant dont on veut récupérer les contributions.
     * @return Liste des contributions du participant sous forme de ContributionDto.
     */
    public List<ContributionDto> getContributionsByParticipant(int participantId) {
        // Récupérer le participant
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec ID " + participantId + " non trouvé"));

        // Récupérer les contributions du participant
        List<Contribution> contributions = contributionDao.findByParticipant(participant);

        // Convertir les contributions en ContributionDto
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour le statut d'une contribution par un gestionnaire.
     * @param contributionId Identifiant de la contribution à mettre à jour.
     * @param newStatus Nouveau statut de la contribution (VALIDE ou REJETE).
     * @param gestionnaireId Identifiant du gestionnaire qui met à jour le statut.
     * @return La contribution mise à jour.
     */
    @Transactional
    public Contribution MiseAJourStatutContribution(int contributionId, ContributionStatus newStatus, int gestionnaireId) {
        // Récupérer la contribution et valider
        Contribution contribution = contributionDao.findById(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Contribution avec ID " + contributionId + " non trouvée"));

        Participant gestionnaire = participantDao.findById(gestionnaireId)
                .orElseThrow(() -> new IllegalArgumentException("Gestionnaire avec ID " + gestionnaireId + " non trouvé"));

        // Vérifier que le participant est un gestionnaire
        if (!gestionnaire.getProfil().equals(ParticipantProfil.GESTIONNAIRE)) {
            throw new IllegalArgumentException("Seul un gestionnaire peut mettre à jour le statut d'une contribution");
        }

        // Mettre à jour le statut de la contribution
        contribution.setStatus(newStatus);
        contribution.setGestionnaire(gestionnaire);

        // Si la contribution est validée, récompenser les coins et mettre à jour le statut de la fonctionnalité
        if (newStatus == ContributionStatus.VALIDE) {
            recompenseCoins(contribution.getParticipant());
            MiseAJourStatutFonctionnalite(contribution.getFonctionnalite());
            assignerBadges(contribution.getParticipant());
        }
        // Vérifier si le statut change vers VALIDER ou REJETER
        if (newStatus == ContributionStatus.VALIDE || newStatus == ContributionStatus.REJETE) {

            // Notifier le participant soumetteur
            Participant participant = contribution.getParticipant();
            String sujet = newStatus == ContributionStatus.VALIDE
                    ? "Contribution validée"
                    : "Contribution rejetée";
            String message = newStatus == ContributionStatus.VALIDE
                    ? "Votre contribution pour la fonctionnalité '" + contribution.getFonctionnalite().getTitre() + "' a été validée."
                    : "Votre contribution pour la fonctionnalité '" + contribution.getFonctionnalite().getTitre() + "' a été rejetée.";

            notificationService.createNotification(
                    participant.getContributeur(),
                    sujet,
                    message
            );
        }

        return contributionDao.save(contribution);
    }

    /**
     * Méthode pour récompenser les coins à un participant lorsque sa contribution est validée.
     * @param participant Le participant dont la contribution a été validée.
     */
    private void recompenseCoins(Participant participant) {
        // Recupération de la configuration des coins pour la contribution validée
        ParametreCoin coinConfig = parametreCoinDao.findByTypeEvenementLien("CONTRIBUTION_VALIDEE")
                .orElseThrow(() -> new IllegalStateException("Coin configuration pour CONTRIBUTION_VALIDEE non trouvée"));

        // Mise à jour des coins du participant
        Contributeur contributeur = participant.getContributeur();
        contributeur.setTotalCoin(contributeur.getTotalCoin() + coinConfig.getValeur());
        contributeurDao.save(contributeur);
    }

    /**
     * Met à jour le statut d'une fonctionnalité lorsque toutes les contributions sont validées.
     * @param fonctionnalite La fonctionnalité dont le statut doit être mis à jour.
     */
    private void MiseAJourStatutFonctionnalite(Fonctionnalite fonctionnalite) {
        if (fonctionnalite != null) {
            fonctionnalite.setStatusFeatures(FeaturesStatus.TERMINE);
            fonctionnaliteDao.save(fonctionnalite);
        }
    }

    /**
     * Méthode pour attribuer des badges aux participants en fonction de leurs contributions validées.
     * @param participant Le participant à qui les badges doivent être attribués.
     */
    private void assignerBadges(Participant participant) {
        // Compter le nombre de contributions validées
        int NombreValidation = contributionDao.findByParticipantIdAndStatus(participant.getId(), ContributionStatus.VALIDE).size();

        // Définition des seuils de badge (par exemple, 5, 10, 20, 50)
        int[] plages = {1, 5, 10, 20, 50};

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
                        BadgeParticipant badgeParticipant = new BadgeParticipant();
                        badgeParticipant.setBadge(badge);
                        badgeParticipant.setParticipant(participant);
                        badgeParticipant.setDateAcquisition(LocalDate.now());
                        badgeParticipantDao.save(badgeParticipant);
                    }
                }
            }
        }
    }

    // Méthode utilitaire pour convertir une Contribution en ContributionDto
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        // Remplir le DTO avec les données de la contribution
        contributionDto.setIdContribution(contribution.getId());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        return contributionDto;
    }
}
