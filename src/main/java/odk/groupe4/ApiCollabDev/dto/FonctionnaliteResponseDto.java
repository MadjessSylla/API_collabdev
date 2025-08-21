package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import odk.groupe4.ApiCollabDev.models.enums.ProjectPriority;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FonctionnaliteResponseDto {

    private int id;
    @NotBlank
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    private String titre;
    @Size(max = 500, message = "Le contenu ne doit pas dépasser 500 caractères")
    private String contenu;
    @NotNull
    private FeaturesStatus statusFeatures;
    private LocalDate dateEcheance;
    private List<String> exigences;
    private List<String> criteresAcceptation;
    private ProjectPriority importance;
    private List<String> motsCles;
    @NotBlank  @Size(max = 100, message = "Le titre du projet ne doit pas dépasser 100 caractères")
    private String projetTitre;
    private String participantNomComplet;
    private String participantEmail;
}
