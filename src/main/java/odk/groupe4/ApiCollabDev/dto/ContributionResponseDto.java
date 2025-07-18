package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributionResponseDto {
    private int id;
    private String lienUrl;
    private String fileUrl;
    private ContributionStatus status;
    private LocalDate dateSoumission;
    private String fonctionnaliteTitre;
    private String participantNom;
    private String gestionnaireNom;
}
