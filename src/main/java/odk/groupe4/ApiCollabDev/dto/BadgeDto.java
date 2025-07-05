package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

@Data @AllArgsConstructor @NoArgsConstructor
public class BadgeDto {
    private TypeBadge type;
    private String description;
    private int nombreContribution;
    private int coin_recompense;
}
