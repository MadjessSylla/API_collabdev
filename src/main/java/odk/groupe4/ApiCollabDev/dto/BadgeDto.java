package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BadgeDto {

    private int Id;
    private TypeBadge type;
    private String description;
    private int coin_recompense;
    private Administrateur administrateur;
    // Administrateur administrateur;

}
