package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProgressionContributeurDto {
    private int contributeurId;
    private String contributeurNom;
    private String contributeurPrenom;
    private int totalContributionsValidees;
    private int totalBadgesObtenus;
    private int totalCoinsGagnes;
    private ProgressionBadgeDto prochainBadge;
    private List<ProgressionBadgeDto> tousLesBadges;
}
