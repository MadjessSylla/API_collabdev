package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestionDto {
    private Integer id;

    @NotBlank
    private String question;

    private List<String> options;

    private List<Integer> indexReponse;
}
