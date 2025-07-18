package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

@Data @AllArgsConstructor @NoArgsConstructor

public class BadgeRequestDto {
    @NotNull
    private TypeBadge type ;
    @NotBlank @Size(min = 50, max = 100)
    private String description;
    @NotNull @Positive
    private int nombreContribution;
    @NotNull @Positive
    private int coin_recompense;
}
