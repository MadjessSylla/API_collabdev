package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.RolePorteurProjet;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProjetDto {
    @NotBlank @Size(max = 50)
    private String titre;
    @NotBlank @Size(max = 100)
    private String description;
    @NotNull
    private ProjectDomain domaine;
    @NotNull
    private ProjectSector secteur;
    @NotBlank
    private String urlCahierDeCharge;

    private RolePorteurProjet role;
}
