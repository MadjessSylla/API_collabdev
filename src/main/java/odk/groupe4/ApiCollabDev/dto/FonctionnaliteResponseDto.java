package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;

@Data @NoArgsConstructor @AllArgsConstructor
public class FonctionnaliteResponseDto {

    private int id;

    @NotBlank @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    private String titre;

    @NotBlank @Size(max = 500, message = "Le contenu ne doit pas dépasser 500 caractères")
    private String contenu;

    @NotNull
    private FeaturesStatus statusFeatures;

    @NotBlank @Size(max = 100, message = "Le titre du projet ne doit pas dépasser 100 caractères")
    private String projetTitre;

    @NotBlank
    private String participantNomComplet;

    @NotBlank
    private String participantEmail;
}
