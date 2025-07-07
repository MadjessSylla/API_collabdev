package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.BadgeRewardDto;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.HistAcquisitionDto;
import odk.groupe4.ApiCollabDev.models.*;
import odk.groupe4.ApiCollabDev.models.enums.Profil;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusParticipant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Participant_projetService {
    private final Participant_projetDao participantProjetDao;
    private final ProjetDao projetDao;
    private final ContributionDao contributionDao;
    private final NotificationService notificationService;

    @Autowired
   public Participant_projetService(
            Participant_projetDao participantProjetDao,
            ProjetDao projetDao,
            ContributionDao contributionDao,
            NotificationService notificationService
    ) {
        this.participantProjetDao = participantProjetDao;
        this.projetDao = projetDao;
        this.contributionDao = contributionDao;
        this.notificationService = notificationService;
    }

    // Méthode pour récupérer l'historique d'acquisition d'un participant
    public HistAcquisitionDto getHistAcquisition(int idParticipant) {
        // Vérification de l'existence du participant
        if (!participantProjetDao.existsById(idParticipant)) {
            throw new IllegalArgumentException("Participant avec l'ID " + idParticipant + " n'existe pas.");
        }
        // Récupération des contributions validées du participant
        List<Contribution> contributions = contributionDao.findByParticipantIdAndStatus(idParticipant, StatusContribution.VALIDER);
        // Conversion des contributions vers des DTOs
        List<ContributionDto> contributionDTOs = contributions.stream()
                .map(this::mapToContributionDTO)
                .collect(Collectors.toList());

        // Récupération des badges acquis par le participant
        List<Badge_participant> badgeParticipants = participantProjetDao.findById(idParticipant)
                .get().getBadgeParticipants().stream().toList();
        // Conversion des badges vers des DTOs
        List<BadgeRewardDto> badgeDTOs = badgeParticipants.stream()
                .map(this::mapToBadgeDTO)
                .collect(Collectors.toList());
        // Création et retour de l'objet HistAcquisitionDto
        return new HistAcquisitionDto(
                idParticipant,
                contributionDTOs,
                badgeDTOs
        );
    }
    // Méthode pour inscrire un participant dans un projet
    public Participant creerParticipant(Participant participant) {
        // Sauvegarde du participant
        Participant savedParticipant = participantProjetDao.save(participant);

        // Récupérer le projet associé
        Projet projet = savedParticipant.getProjet();

        // Récupérer tous les participants du projet avec le profil GESTIONNAIRE
        projet.getParticipants().stream()
                .filter(p -> p.getProfil() == Profil.GESTIONNAIRE)
                .forEach(gestionnaire -> {
                    // Envoyer une notification à l'utilisateur du contributeur du gestionnaire
                    notificationService.createNotification(
                            gestionnaire.getContributeur(),
                            "Nouvelle demande de participation",
                            "Un contributeur a demandé à participer au projet '" + projet.getTitre() + "'."
                    );
                });

        return savedParticipant;
    }
    // Méthode pour refuser une demande de participation
    public void updateParticipantStatus(int participantId, StatusParticipant newStatus) {
        Participant participant = participantProjetDao.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé"));
        participant.setStatut(newStatus);
        participantProjetDao.save(participant);

        if (newStatus == StatusParticipant.REFUSE) {
            notificationService.createNotification(
                    participant.getContributeur(),
                    "Participation refusée",
                    "Votre demande de participation au projet '" + participant.getProjet().getTitre() + "' a été refusée."
            );
        }
    }
    // Méthode pour mapper une Contribution vers un ContributionDto
    private ContributionDto mapToContributionDTO(Contribution contribution) {
        return new ContributionDto(
                contribution.getId(),
                contribution.getLienUrl(),
                contribution.getFileUrl(),
                contribution.getStatus(),
                contribution.getDateSoumission(),
                contribution.getFonctionnalite() != null ? contribution.getFonctionnalite().getId() : 0
        );
    }

    // Méthode pour mapper un Badge_participant vers un BadgeRewardDto
    private BadgeRewardDto mapToBadgeDTO(Badge_participant badgeParticipant) {
        return new BadgeRewardDto(
                badgeParticipant.getBadge().getId(),
                badgeParticipant.getBadge().getType(),
                badgeParticipant.getBadge().getDescription(),
                badgeParticipant.getBadge().getNombreContribution(),
                badgeParticipant.getBadge().getCoin_recompense(),
                badgeParticipant.getDateAcquisition()
        );
    }
}
