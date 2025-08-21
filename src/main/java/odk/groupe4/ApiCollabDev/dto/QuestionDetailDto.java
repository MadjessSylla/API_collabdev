package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class QuestionDetailDto {
    private int id;
    private String question;
    private List<String> options;
    private List<Integer> indexReponse;
    private int ordre; // Position de la question dans le questionnaire
}
