package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProjetDto {
    @NotBlank @Size(max = 50)
    private String titre;
    @NotBlank @Size(max = 100)
    private String description;
    @NotNull @Size(max = 50)
    private ProjectDomain domaine;
    @NotNull @Size(max = 50)
    private ProjectSector secteur;
    @NotBlank
    private String urlCahierDeCharge;
}
