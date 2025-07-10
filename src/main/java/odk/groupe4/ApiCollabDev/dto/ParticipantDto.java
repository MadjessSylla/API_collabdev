package odk.groupe4.ApiCollabDev.dto;

import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;

@Data @NoArgsConstructor @AllArgsConstructor
public class ParticipantDto {
    private ParticipantProfil profil;
    private String scoreQuiz;
}
