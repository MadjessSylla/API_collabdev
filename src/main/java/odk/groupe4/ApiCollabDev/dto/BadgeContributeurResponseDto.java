package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class BadgeContributeurResponseDto {
    private int id;
    private LocalDate dateAcquisition;
    private int badgeId;
    private TypeBadge badgeType;
    private String badgeDescription;
    private int coinRecompense;
    private int contributeurId;
    private String contributeurNom;
    private String contributeurPrenom;
}
