package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;



@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProjetDto {
    private String titre;
    private String description;
    private String domaine;
    private String urlCahierDeCharge;
    private StatusProject status;
}
