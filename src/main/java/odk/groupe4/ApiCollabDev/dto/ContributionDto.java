package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ContributionDto {
    private Integer id;

    private String titre;
    private String description;
    private String lienUrl;
    private String fileUrl;
    private ContributionStatus status; // enum sans @Size
    private LocalDate dateSoumission;
    @NotNull(message = "La fonctionnalité est obligatoire")
    private Integer fonctionnaliteId;
    @NotNull(message = "Le participant est obligatoire")
    private Integer participantId;
    private Integer gestionnaireId;
}
