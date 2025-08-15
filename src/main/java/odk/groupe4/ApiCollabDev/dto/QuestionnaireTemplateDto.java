package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class QuestionnaireTemplateDto {
    private Integer id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    @NotNull(message = "Le type de quiz est obligatoire")
    private TypeQuiz type;

    private ProjectDomain domaine;

    private ProjectSector secteur;

    @NotNull(message = "La durée estimée est obligatoire")
    private Integer dureeEstimee;

    private Integer createurId;

    private List<QuestionTemplateDto> questions;

    private String tags;

    private String objectifsPedagogiques;

    private boolean estActif = true;
}
