package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class FonctionnaliteNewDto {
    @NotBlank @Size(max = 50)
    private String titre;
    @NotBlank @Size(max = 50)
    private String contenu;
}
