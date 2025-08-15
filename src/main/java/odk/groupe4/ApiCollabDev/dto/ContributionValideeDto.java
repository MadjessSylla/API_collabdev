package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContributionValideeDto {
    private Integer id;
    private String titre;
    private String description;
    private String lienUrl;
    private String fileUrl;
    private LocalDate dateSoumission;
    private String fonctionnaliteTitre;
    private String participantNom;
    private String participantPrenom;
}
