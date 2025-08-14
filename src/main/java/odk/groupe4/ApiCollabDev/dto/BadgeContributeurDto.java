package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.Contributeur;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class BadgeContributeurDto {
    @NotNull(message = "La date d'acquisition est obligatoire")
    private LocalDate dateAcquisition;

    @NotNull(message = "Le badge est obligatoire")
    private Badge badge;

    @NotNull(message = "Le contributeur est obligatoire")
    private Contributeur contributeur;
}
