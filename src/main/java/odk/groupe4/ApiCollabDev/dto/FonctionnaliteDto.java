package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.StatusFeatures;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FonctionnaliteDto {

    private String titre;
    private String contenu;
    private StatusFeatures statusFeatures;
    private Projet projet;
}
