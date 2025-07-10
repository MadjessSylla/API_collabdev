package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ContributionSoumiseDto {
    private String lienUrl; // Lien vers la contribution
    private String fileUrl; // Lien vers un fichier de contribution (par exemple, un fichier de code, une image, un document, etc.)
}
