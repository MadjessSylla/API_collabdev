package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Le titre du projet est obligatoire.")
    private String titre;
    @NotBlank(message = "Le contenu est obligatoire.")
    private String contenu;
    private StatusFeatures statusFeatures;
    private Projet projet;
}
