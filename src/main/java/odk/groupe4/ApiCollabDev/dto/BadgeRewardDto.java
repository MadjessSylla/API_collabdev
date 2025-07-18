package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class BadgeRewardDto {
    @NotNull
    private int idBadge; // Identifiant de la récompense de badge
    @NotNull
    private TypeBadge typeBadge; // Type de badge (par exemple, "Bronze", "Argent", "Or")
    @NotBlank @Size(max = 100)
    private String description; // Description du badge
    @NotNull @Positive
    private int nombreContribution; // Nombre de contributions nécessaires pour obtenir le badge
    @NotNull @Positive
    private int coinRecompense; // Nombre de coins récompensés pour l'obtention du badge
    @NotNull
    private LocalDate dateAcquisition; // Date d'acquisition du badge
}
