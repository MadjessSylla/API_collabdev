package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureStatusDto {
    private boolean aCandidature;
    private ParticipantStatus statut;
    private String message;
    private Integer participantId;

    // Constructeur pour le cas où il n'y a pas de candidature
    public CandidatureStatusDto(boolean aCandidature, String message) {
        this.aCandidature = aCandidature;
        this.message = message;
        this.statut = null;
        this.participantId = null;
    }
}
