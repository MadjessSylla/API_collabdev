package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;

import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class QuestionnaireTemplateResponseDto {
    private int id;
    private String titre;
    private String description;
    private TypeQuiz type;
    private ProjectDomain domaine;
    private ProjectSector secteur;
    private int dureeEstimee;
    private LocalDate dateCreation;
    private int nombreQuestions;
    private int nombreUtilisations;
    private boolean estActif;

    // Informations sur le créateur
    private int createurId;
    private String createurNom;
    private String createurPrenom;
    private String createurType;

    // Métadonnées
    private String tags;
    private String objectifsPedagogiques;

    // Questions complètes (optionnel selon le besoin)
    private List<QuestionTemplateDto> questions;
}
