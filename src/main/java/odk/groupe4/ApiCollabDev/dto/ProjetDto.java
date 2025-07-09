package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.enums.NiveauProfil;
import odk.groupe4.ApiCollabDev.models.enums.StatusProject;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ProjetDto {
    @NotBlank(message = "Le titre du projet est obligatoire.")
    private String titre;
    @NotBlank(message = "La description du projet est obligatoire.")
    private String description;
    private String domaine;
    private String urlCahierDeCharge;
    private StatusProject status;
    private NiveauProfil niveauProfil;
    private LocalDate date;
    private Contributeur createur;
    private Administrateur administrateur;
}
