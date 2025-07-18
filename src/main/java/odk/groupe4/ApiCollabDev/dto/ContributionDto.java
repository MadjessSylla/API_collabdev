package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class ContributionDto {
    @NotNull
    private int idContribution; // Identifiant de la contribution
    @NotBlank
    private String lienUrl; // Lien vers la contribution
    @NotBlank
    private String fileUrl; // Lien vers un fichier de contribution (par exemple, un fichier de code, une image, un document, etc.)
    @NotNull
    private ContributionStatus status; // Statut de la contribution (En attente, Acceptée, Rejetée)
    @NotNull
    private LocalDate dateSoumission; // Date de soumission de la contribution
    @NotNull
    private int fonctionnaliteId; // Identifiant de la fonctionnalité à laquelle la contribution est associée
    @NotNull
    private int participantId; // Identifiant du participant qui a soumis la contribution
    @NotNull
    private int gestionnaireId; // Identifiant du participant Gestionnaire qui a validé la contribution
}
