package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class QuestionTemplateDto {
    private Integer id;

    @NotBlank(message = "La question est obligatoire")
    private String question;

    @NotNull(message = "Les options sont obligatoires")
    private List<String> options;

    @NotNull(message = "Les réponses correctes sont obligatoires")
    private List<Integer> indexReponse;

    @NotNull(message = "L'ordre est obligatoire")
    private Integer ordre;

    private String explication;

    private Integer questionnaireTemplateId;
}
