package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProgressionBadgeDto {
    private int badgeId;
    private TypeBadge typeBadge;
    private String description;
    private int seuilRequis;
    private int contributionsValidees;
    private int contributionsRestantes;
    private double pourcentageProgression;
    private int coinRecompense;
    private boolean dejaObtenu;
    private boolean prochainBadge;
}
