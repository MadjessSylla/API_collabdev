package odk.groupe4.ApiCollabDev.service;

import jakarta.transaction.Transactional;
import odk.groupe4.ApiCollabDev.dao.*;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.ContributionResponseDto;
import odk.groupe4.ApiCollabDev.dto.ContributionSoumiseDto;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContributionService {

    // Déclaration des DAO et services nécessaires pour gérer les contributions, participants, fonctionnalités, badges et notifications
    private final ContributionDao contributionDao;
    private final ParticipantDao participantDao;
    private final FonctionnaliteDao fonctionnaliteDao;
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final BadgeDao badgeDao;
    private final BadgeParticipantDao badgeParticipantDao;
    private final NotificationService notificationService;

    // Injection des dépendances via constructeur
    @Autowired
    public ContributionService(ContributionDao contributionDao,
                               ParticipantDao participantDao,
                               FonctionnaliteDao fonctionnaliteDao,
                               ContributeurDao contributeurDao,
                               ParametreCoinDao parametreCoinDao,
                               BadgeDao badgeDao,
                               BadgeParticipantDao badgeParticipantDao,
                               NotificationService notificationService) {
        this.contributionDao = contributionDao;
        this.participantDao = participantDao;
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.contributeurDao = contributeurDao;
        this.parametreCoinDao = parametreCoinDao;
        this.badgeDao = badgeDao;
        this.badgeParticipantDao = badgeParticipantDao;
        this.notificationService = notificationService;
    }

    /**
     * Récupère la liste des contributions filtrées par statut.
     * Si le statut est null, retourne toutes les contributions.
     *
     * @param status Le statut des contributions à filtrer (optionnel)
     * @return Liste de DTO des contributions correspondantes
     */
    public List<ContributionDto> afficherLaListeDesContribution(ContributionStatus status) {
        List<Contribution> contributions;
        if (status != null) {
            contributions = contributionDao.findByStatus(status);
        } else {
            contributions = contributionDao.findAll();
        }
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    public List<ContributionDto> afficherContributionsParProjetEtStatus(int projetId, ContributionStatus status) {
        // Récupérer la liste des participants associés au projet donné
        List<Participant> participantsDuProjet = participantDao.findByProjetId(projetId);

        // Récupérer toutes les contributions dont le participant fait partie de ce projet
        List<Contribution> contributions;

        if (status != null) {
            // Filtrer par statut en plus
            contributions = contributionDao.findByParticipantInAndStatus(participantsDuProjet, status);
        } else {
            // Sans filtre de statut
            contributions = contributionDao.findByParticipantIn(participantsDuProjet);
        }

        // Transformer en DTO
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une contribution par son ID.
     *
     * @param id ID de la contribution
     * @return DTO de la contribution correspondante
     * @throws RuntimeException si la contribution n'existe pas
     */
    public ContributionResponseDto getContributionById(int id) {
        Contribution contribution = contributionDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contribution non trouvée avec l'ID: " + id));
        return mapToResponseDto(contribution);
    }

    /**
     * Soumet une nouvelle contribution associée à une fonctionnalité et un participant.
     * Initialise la contribution avec le statut ENVOYE et la date de soumission actuelle.
     *
     * @param idFonctionnalite ID de la fonctionnalité concernée
     * @param idParticipant ID du participant soumettant la contribution
     * @param contribution Données de la contribution soumise
     * @return DTO de la contribution enregistrée
     * @throws IllegalArgumentException si le participant ou la fonctionnalité n'existe pas
     */
    public ContributionResponseDto soumettreContribution(int idFonctionnalite, int idParticipant, ContributionSoumiseDto contribution) {
        Participant participant = participantDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));

        Fonctionnalite fonctionnalite = fonctionnaliteDao.findById(idFonctionnalite)
                .orElseThrow(() -> new IllegalArgumentException("Fonctionnalité non trouvée"));

        Contribution newContribution = new Contribution();
        newContribution.setLienUrl(contribution.getLienUrl());
        newContribution.setFileUrl(contribution.getFileUrl());
        newContribution.setStatus(ContributionStatus.ENVOYE);
        newContribution.setDateSoumission(LocalDate.now());
        newContribution.setFonctionnalite(fonctionnalite);
        newContribution.setParticipant(participant);

        Contribution savedContribution = contributionDao.save(newContribution);
        return mapToResponseDto(savedContribution);
    }

    /**
     * Récupère toutes les contributions d'un participant donné.
     *
     * @param participantId ID du participant
     * @return Liste des contributions sous forme de DTO
     * @throws IllegalArgumentException si le participant n'existe pas
     */
    public List<ContributionDto> getContributionsByParticipant(int participantId) {
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec ID " + participantId + " non trouvé"));

        List<Contribution> contributions = contributionDao.findByParticipant(participant);
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les contributions associées à une fonctionnalité spécifique.
     *
     * @param fonctionnaliteId ID de la fonctionnalité
     * @return Liste des contributions sous forme de DTO
     * @throws RuntimeException si la fonctionnalité n'existe pas
     */
    public List<ContributionDto> getContributionsByFonctionnalite(int fonctionnaliteId) {
        if (!fonctionnaliteDao.existsById(fonctionnaliteId)) {
            throw new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + fonctionnaliteId);
        }

        List<Contribution> contributions = contributionDao.findByFonctionnaliteId(fonctionnaliteId);
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    /**
     * Valide ou rejette une contribution selon la décision d'un gestionnaire.
     * Seuls les participants ayant le profil GESTIONNAIRE peuvent effectuer cette opération.
     * En cas de validation, récompense le participant avec des coins, met à jour le statut de la fonctionnalité,
     * et assigne des badges si les critères sont remplis.
     * Envoie également une notification au participant concernant la décision.
     *
     * Cette méthode est transactionnelle pour garantir la cohérence des données.
     *
     * @param contributionId ID de la contribution à valider ou rejeter
     * @param newStatus Nouveau statut (VALIDE ou REJETE)
     * @param gestionnaireId ID du gestionnaire effectuant la validation
     * @return DTO de la contribution mise à jour
     * @throws IllegalArgumentException si la contribution, gestionnaire n'existent pas ou si le profil est invalide
     */
    @Transactional
    public ContributionResponseDto validateOrRejetContribution(int contributionId, ContributionStatus newStatus, int gestionnaireId) {
        Contribution contribution = contributionDao.findById(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Contribution avec ID " + contributionId + " non trouvée"));

        Participant gestionnaire = participantDao.findById(gestionnaireId)
                .orElseThrow(() -> new IllegalArgumentException("Gestionnaire avec ID " + gestionnaireId + " non trouvé"));

        if (!gestionnaire.getProfil().equals(ParticipantProfil.GESTIONNAIRE)) {
            throw new IllegalArgumentException("Seul un gestionnaire peut mettre à jour le statut d'une contribution");
        }

        contribution.setStatus(newStatus);
        contribution.setGestionnaire(gestionnaire);

        if (newStatus == ContributionStatus.VALIDE) {
            recompenseCoins(contribution.getParticipant());  // Attribution des coins au participant
            MiseAJourStatutFonctionnalite(contribution.getFonctionnalite()); // Passage de la fonctionnalité au statut TERMINÉ
            assignerBadges(contribution.getParticipant()); // Attribution éventuelle de badges
        }

        // Envoi de notification au participant selon la décision prise
        if (newStatus == ContributionStatus.VALIDE || newStatus == ContributionStatus.REJETE) {
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

        Contribution savedContribution = contributionDao.save(contribution);
        return mapToResponseDto(savedContribution);
    }

    /**
     * Ajoute des coins de récompense au contributeur associé à un participant donné,
     * selon la configuration de coins pour l'événement "CONTRIBUTION_VALIDEE".
     *
     * @param participant Participant qui reçoit les coins
     */
    private void recompenseCoins(Participant participant) {
        ParametreCoin coinConfig = parametreCoinDao.findByTypeEvenementLien("CONTRIBUTION_VALIDEE")
                .orElseThrow(() -> new IllegalStateException("Coin configuration pour CONTRIBUTION_VALIDEE non trouvée"));

        Contributeur contributeur = participant.getContributeur();
        contributeur.setTotalCoin(contributeur.getTotalCoin() + coinConfig.getValeur());
        contributeurDao.save(contributeur);
    }

    /**
     * Met à jour le statut d'une fonctionnalité en le passant à TERMINÉ.
     *
     * @param fonctionnalite La fonctionnalité à mettre à jour
     */
    private void MiseAJourStatutFonctionnalite(Fonctionnalite fonctionnalite) {
        if (fonctionnalite != null) {
            fonctionnalite.setStatusFeatures(FeaturesStatus.TERMINE);
            fonctionnaliteDao.save(fonctionnalite);
        }
    }

    /**
     * Attribue des badges à un participant en fonction du nombre de contributions validées.
     * Si le participant atteint le seuil pour un badge non encore attribué,
     * le badge est attribué, les coins de récompense associés sont ajoutés,
     * et une notification est envoyée.
     *
     * @param participant Participant à qui attribuer des badges
     */
    private void assignerBadges(Participant participant) {
        // Nombre total de contributions validées par le participant
        int nombreValidation = contributionDao.findByParticipantIdAndStatus(participant.getId(), ContributionStatus.VALIDE).size();

        // Liste des badges disponibles triée par nombre de contributions requises
        List<Badge> badgesDisponibles = badgeDao.findAllOrderByNombreContributionAsc();

        for (Badge badge : badgesDisponibles) {
            if (nombreValidation >= badge.getNombreContribution()) {
                // Vérifie si le participant possède déjà ce badge
                boolean hasBadge = badgeParticipantDao.findByParticipantIdAndBadgeId(participant.getId(), badge.getId()).isPresent();

                if (!hasBadge) {
                    // Attribution du badge
                    BadgeParticipant badgeParticipant = new BadgeParticipant();
                    badgeParticipant.setBadge(badge);
                    badgeParticipant.setParticipant(participant);
                    badgeParticipant.setDateAcquisition(LocalDate.now());
                    badgeParticipantDao.save(badgeParticipant);

                    // Attribution des coins de récompense au contributeur
                    Contributeur contributeur = participant.getContributeur();
                    contributeur.setTotalCoin(contributeur.getTotalCoin() + badge.getCoin_recompense());
                    contributeurDao.save(contributeur);

                    // Notification au participant
                    notificationService.createNotification(
                            contributeur,
                            "Nouveau badge obtenu !",
                            "Félicitations ! Vous avez obtenu le badge " + badge.getType() +
                                    " pour avoir atteint " + badge.getNombreContribution() + " contributions validées. " +
                                    "Vous recevez " + badge.getCoin_recompense() + " coins en récompense !"
                    );

                    System.out.println("Badge " + badge.getType() + " attribué au participant " + participant.getId());
                }
            }
        }
    }

    /**
     * Convertit une entité Contribution en DTO simple.
     *
     * @param contribution Entité Contribution à convertir
     * @return DTO correspondant
     */
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setId(contribution.getId());
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateSoumission(contribution.getDateSoumission());
        contributionDto.setFonctionnaliteId(contribution.getFonctionnalite().getId());
        contributionDto.setParticipantId(contribution.getParticipant().getId());
        if (contribution.getGestionnaire() != null) {
            contributionDto.setGestionnaireId(contribution.getGestionnaire().getId());
        }
        return contributionDto;
    }

    /**
     * Convertit une entité Contribution en DTO détaillé pour la réponse API.
     *
     * @param contribution Entité Contribution à convertir
     * @return DTO détaillé avec informations sur la fonctionnalité, participant et gestionnaire
     */
    private ContributionResponseDto mapToResponseDto(Contribution contribution) {
        return new ContributionResponseDto(
                contribution.getId(),
                contribution.getLienUrl(),
                contribution.getFileUrl(),
                contribution.getStatus(),
                contribution.getDateSoumission(),
                contribution.getFonctionnalite().getTitre(),
                contribution.getParticipant().getContributeur().getNom() + " " + contribution.getParticipant().getContributeur().getPrenom(),
                contribution.getGestionnaire() != null ?
                        contribution.getGestionnaire().getContributeur().getNom() + " " + contribution.getGestionnaire().getContributeur().getPrenom() :
                        null
        );
    }
}
