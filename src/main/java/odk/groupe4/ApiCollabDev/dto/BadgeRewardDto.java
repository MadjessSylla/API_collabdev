package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class BadgeRewardDto {
    private int idBadge; // Identifiant de la récompense de badge
    private TypeBadge typeBadge; // Type de badge (par exemple, "Bronze", "Argent", "Or")
    private String description; // Description du badge
    private int nombreContribution; // Nombre de contributions nécessaires pour obtenir le badge
    private int coinRecompense; // Nombre de coins récompensés pour l'obtention du badge
    private LocalDate dateAcquisition; // Date d'acquisition du badge
}
