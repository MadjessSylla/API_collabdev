package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProjetDto {
    private String titre;
    private String description;
    private ProjectDomain domaine;
    private ProjectSector secteur;
    private String urlCahierDeCharge;
}
