package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class QuestionnaireResponseDto {
    private int id;
    private String titre;
    private String description;
    private LocalDate dateCreation;
    private String createurNom;
    private String createurEmail;
    private String projetTitre;
    private String templateNom;
    private int nombreQuestions;
}
