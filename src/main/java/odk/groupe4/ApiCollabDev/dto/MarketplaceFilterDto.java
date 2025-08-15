package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.TypeQuiz;

@Data @NoArgsConstructor @AllArgsConstructor
public class MarketplaceFilterDto {
    private TypeQuiz type;
    private ProjectDomain domaine;
    private ProjectSector secteur;
    private String keyword;
    private String sortBy; // "popularite", "recent", "titre"
}
