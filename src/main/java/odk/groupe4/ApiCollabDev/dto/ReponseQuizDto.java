package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReponseQuizDto {
    private Map<Integer, List<Integer>> reponses; // questionId -> liste des indices de réponses sélectionnées
    private int participantId;
    private String commentaire; // Commentaire optionnel du participant
}
