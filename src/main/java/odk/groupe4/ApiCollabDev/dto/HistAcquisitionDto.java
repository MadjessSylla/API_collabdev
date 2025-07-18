package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class HistAcquisitionDto {
    @NotNull @Size(max = 50)
    private int idParticipant; // Identifiant du participant
    @NotNull
    private List<ContributionDto> contributionValidees; // Liste des contributions validées
    @NotNull
    private List<BadgeRewardDto> badgesAcquis; // Liste des badges acquis
}
