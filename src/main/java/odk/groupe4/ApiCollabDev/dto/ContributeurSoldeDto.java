package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ContributeurSoldeDto {
    @NotNull @Positive
    private int totalCoin;
}
