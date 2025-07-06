package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class HistAcquisitionDto {
    private int idParticipant; // Identifiant du participant
    private List<ContributionDto> contributionValidees; // Liste des contributions validées
    private List<BadgeRewardDto> badgesAcquis; // Liste des badges acquis
}
