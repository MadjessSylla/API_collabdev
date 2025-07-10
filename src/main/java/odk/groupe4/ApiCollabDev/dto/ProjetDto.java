package odk.groupe4.ApiCollabDev.dto;

import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProjetDto {
    private String titre;
    private String description;
    private ProjectDomain domaine;
    private ProjectSector secteur;
    private String urlCahierDeCharge;
}
