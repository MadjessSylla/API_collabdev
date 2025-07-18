package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ContributionSoumiseDto {
    @NotBlank @Size(max = 100)
    private String lienUrl; // Lien vers la contribution
    @NotBlank
    private String fileUrl; // Lien vers un fichier de contribution (par exemple, un fichier de code, une image, un document, etc.)
}
