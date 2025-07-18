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
    private final ContributionDao contributionDao;
    private final ParticipantDao participantDao;
    private final FonctionnaliteDao fonctionnaliteDao;
    private final ContributeurDao contributeurDao;
    private final ParametreCoinDao parametreCoinDao;
    private final BadgeDao badgeDao;
    private final BadgeParticipantDao badgeParticipantDao;
    private final NotificationService notificationService;

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

    public ContributionResponseDto getContributionById(int id) {
        Contribution contribution = contributionDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Contribution non trouvée avec l'ID: " + id));
        return mapToResponseDto(contribution);
    }

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

    public List<ContributionDto> getContributionsByParticipant(int participantId) {
        Participant participant = participantDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant avec ID " + participantId + " non trouvé"));

        List<Contribution> contributions = contributionDao.findByParticipant(participant);
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

    public List<ContributionDto> getContributionsByFonctionnalite(int fonctionnaliteId) {
        if (!fonctionnaliteDao.existsById(fonctionnaliteId)) {
            throw new RuntimeException("Fonctionnalité non trouvée avec l'ID: " + fonctionnaliteId);
        }

        List<Contribution> contributions = contributionDao.findByFonctionnaliteId(fonctionnaliteId);
        return contributions.stream()
                .map(this::ContributionDaoToContributionDto)
                .collect(Collectors.toList());
    }

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
            recompenseCoins(contribution.getParticipant());
            MiseAJourStatutFonctionnalite(contribution.getFonctionnalite());
            assignerBadges(contribution.getParticipant());
        }

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

    private void recompenseCoins(Participant participant) {
        ParametreCoin coinConfig = parametreCoinDao.findByTypeEvenementLien("CONTRIBUTION_VALIDEE")
                .orElseThrow(() -> new IllegalStateException("Coin configuration pour CONTRIBUTION_VALIDEE non trouvée"));

        Contributeur contributeur = participant.getContributeur();
        contributeur.setTotalCoin(contributeur.getTotalCoin() + coinConfig.getValeur());
        contributeurDao.save(contributeur);
    }

    private void MiseAJourStatutFonctionnalite(Fonctionnalite fonctionnalite) {
        if (fonctionnalite != null) {
            fonctionnalite.setStatusFeatures(FeaturesStatus.TERMINE);
            fonctionnaliteDao.save(fonctionnalite);
        }
    }

    private void assignerBadges(Participant participant) {
        // Compter le nombre total de contributions validées du participant
        int nombreValidation = contributionDao.findByParticipantIdAndStatus(participant.getId(), ContributionStatus.VALIDE).size();

        // Récupérer tous les badges triés par nombre de contributions croissant
        List<Badge> badgesDisponibles = badgeDao.findAllOrderByNombreContributionAsc();

        // Parcourir les badges et attribuer ceux pour lesquels le participant est éligible
        for (Badge badge : badgesDisponibles) {
            if (nombreValidation >= badge.getNombreContribution()) {
                // Vérifier si le participant a déjà ce badge
                boolean hasBadge = badgeParticipantDao.findByParticipantIdAndBadgeId(participant.getId(), badge.getId()).isPresent();

                if (!hasBadge) {
                    // Attribuer le badge
                    BadgeParticipant badgeParticipant = new BadgeParticipant();
                    badgeParticipant.setBadge(badge);
                    badgeParticipant.setParticipant(participant);
                    badgeParticipant.setDateAcquisition(LocalDate.now());
                    badgeParticipantDao.save(badgeParticipant);

                    // Attribuer les coins de récompense
                    Contributeur contributeur = participant.getContributeur();
                    contributeur.setTotalCoin(contributeur.getTotalCoin() + badge.getCoin_recompense());
                    contributeurDao.save(contributeur);

                    // Notifier le participant
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

    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setIdContribution(contribution.getId());
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
