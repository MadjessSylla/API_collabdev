package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;

@Data @NoArgsConstructor @AllArgsConstructor
public class TemplateProjetDto {
    @NotBlank @Size(max = 50)
    private String nom;

    @NotBlank @Size(max = 255)
    private String description;

    @NotNull
    private ProjectDomain domaine;

    @NotNull
    private ProjectSector secteur;

    @NotNull
    private ProjectLevel niveauRecommande;

    @NotBlank
    private String structureProjet;
}
