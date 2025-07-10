package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class ContributionDto {
    private int idContribution; // Identifiant de la contribution
    private String lienUrl; // Lien vers la contribution
    private String fileUrl; // Lien vers un fichier de contribution (par exemple, un fichier de code, une image, un document, etc.)
    private ContributionStatus status; // Statut de la contribution (En attente, Acceptée, Rejetée)
    private LocalDate dateSoumission; // Date de soumission de la contribution
    private int fonctionnaliteId; // Identifiant de la fonctionnalité à laquelle la contribution est associée
    private int participantId; // Identifiant du participant qui a soumis la contribution
    private int gestionnaireId; // Identifiant du participant Gestionnaire qui a validé la contribution
}
