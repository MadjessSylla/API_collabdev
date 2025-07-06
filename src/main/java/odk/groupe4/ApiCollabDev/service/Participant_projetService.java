package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dao.Participant_projetDao;
import odk.groupe4.ApiCollabDev.dto.BadgeRewardDto;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.dto.HistAcquisitionDto;
import odk.groupe4.ApiCollabDev.models.Badge_participant;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Participant_projetService {
    private final Participant_projetDao participantProjetDao;
    private final ContributionDao contributionDao;

    @Autowired
    public Participant_projetService(Participant_projetDao participantProjetDao, ContributionDao contributionDao) {
        this.participantProjetDao = participantProjetDao;
        this.contributionDao = contributionDao;
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
