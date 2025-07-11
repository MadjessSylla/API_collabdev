package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadgeResponseDto {
    private int id;
    private TypeBadge type;
    private String description;
    private int nombreContribution;
    private int coin_recompense;
    private String createurEmail;
}
