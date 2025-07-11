package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjetResponseDto {
    private int id;
    private String titre;
    private String description;
    private ProjectDomain domaine;
    private ProjectSector secteur;
    private String urlCahierDeCharge;
    private ProjectStatus status;
    private ProjectLevel niveau;
    private LocalDate dateCreation;
    private String createurNom;
    private String createurPrenom;
    private String validateurEmail;
    private int nombreParticipants;
    private int nombreFonctionnalites;
}
