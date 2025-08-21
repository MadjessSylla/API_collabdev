package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ResultatQuizDto {
    private int score;
    private int totalQuestions;
    private double pourcentage;
    private String niveau;
    private String commentaire;
}
