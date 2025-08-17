package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;
import odk.groupe4.ApiCollabDev.models.enums.RolePorteurProjet;

import java.time.LocalDate;

/**
 * DTO de création/édition pour un Projet.
 * Note: aucune @Size sur les enums.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjetDto {
    @NotBlank(message = "Le titre est obligatoire")
    private String titre;
    @NotBlank(message = "La description est obligatoire")
    private String description;
    @NotNull(message = "Le domaine est obligatoire")
    private ProjectDomain domaine;
    @NotNull(message = "Le secteur est obligatoire")
    private ProjectSector secteur;
    private ProjectLevel niveau;
    private LocalDate dateEcheance;
    private Integer createurId;
    private String urlCahierDeCharge;
    // Ajout pour aligner avec le service (détermine le profil du participant créateur)
    private RolePorteurProjet role;
}
