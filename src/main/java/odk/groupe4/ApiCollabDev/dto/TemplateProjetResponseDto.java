package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class TemplateProjetResponseDto {
    private int id;
    private String nom;
    private String description;
    private ProjectDomain domaine;
    private ProjectSector secteur;
    private ProjectLevel niveauRecommande;
    private String structureProjet;
    private LocalDate dateCreation;
    private boolean actif;
    private String createurEmail;
    private int nombreQuestionnaires;
}
