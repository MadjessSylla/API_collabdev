package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestionnaireDto {
    private Integer id;
    @NotBlank
    private String titre;
    private String description;
    @NotNull
    private TypeQuiz type; // enum sans @Size
    @NotNull
    private Integer dureeEstimee;
    private Integer contributeurId;
    private Integer projetId;
    private List<QuestionDto> questions;
}
