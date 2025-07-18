package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class QuestionnaireDto {
    @NotBlank @Size(max = 100)
    private String titre;

    @NotBlank @Size(max = 255)
    private String description;
}
