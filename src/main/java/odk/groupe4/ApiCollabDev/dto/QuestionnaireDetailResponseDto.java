package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;

import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class QuestionnaireDetailResponseDto {
    private int id;
    private String titre;
    private String description;
    private TypeQuiz type;
    private int dureeEstimee;
    private LocalDate dateCreation;
    private int nombreQuestions;

    // Informations sur le créateur
    private int createurId;
    private String createurNom;
    private String createurPrenom;
    private String createurEmail;
    private String createurType; // "CONTRIBUTEUR" ou "ADMINISTRATEUR"

    // Informations sur le projet
    private int projetId;
    private String projetTitre;
    private String projetDescription;

    // Liste complète des questions avec détails
    private List<QuestionDetailDto> questions;
}
