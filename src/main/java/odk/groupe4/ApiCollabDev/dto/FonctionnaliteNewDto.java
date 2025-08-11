package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import odk.groupe4.ApiCollabDev.models.enums.ProjectPriority;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FonctionnaliteNewDto {

    private Integer id;
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 100 caractères")
    private String titre;
    @Size(max = 500, message = "Le contenu ne doit pas dépasser 500 caractères")
    private String contenu;
    private FeaturesStatus statusFeatures; // Par défaut A_FAIRE dans le service
    private LocalDate dateEcheance;
    private List<String> exigences;
    private List<String> criteresAcceptation;
    private ProjectPriority importance;
    private List<String> motsCles;
    @NotNull(message = "L'identifiant du projet est obligatoire")
    private Integer projetId;
    private Integer participantId;
}
