package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ContributionDto {
    private String lienUrl;
    private String fileUrl;
    private StatusContribution status;
    private LocalDate dateCreation;
    private Participant participant;
    private Participant gestionnaire;
    private Fonctionnalite fonctionnalite;

}
