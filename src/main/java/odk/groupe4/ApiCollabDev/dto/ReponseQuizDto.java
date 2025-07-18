package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor
public class ReponseQuizDto {
    @NotNull
    private Map<Integer, Integer> reponses; // questionId -> indexReponse
}
