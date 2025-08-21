package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

@Data @NoArgsConstructor @AllArgsConstructor
public class BadgeSeuilDto {
    private TypeBadge type;
    private int nombreContribution;
    private int coinRecompense;
    private String description;
    private boolean atteint;
}
