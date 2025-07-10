package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private ParticipantProfil profil;
    private String scoreQuiz;
}
