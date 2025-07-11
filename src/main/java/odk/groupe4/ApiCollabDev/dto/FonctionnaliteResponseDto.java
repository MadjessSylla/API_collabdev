package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FonctionnaliteResponseDto {
    private int id;
    private String titre;
    private String contenu;
    private FeaturesStatus statusFeatures;
    private String projetTitre;
    private String participantNom;
    private String participantEmail;
}
