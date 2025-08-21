package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Contribution;

@Data @AllArgsConstructor @NoArgsConstructor
public class NotificationDto {
    @NotBlank @Size(max = 50)
    private String sujet;
    @NotBlank @Size(max = 50)
    private String contenu;
    @NotNull
    private Contribution contribution;
    @NotNull
    private Contributeur contributeur;
}
