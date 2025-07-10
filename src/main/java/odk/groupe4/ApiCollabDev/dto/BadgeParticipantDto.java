package odk.groupe4.ApiCollabDev.dto;

import lombok.*;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.Participant;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class BadgeParticipantDto {
    LocalDate dateAcquisition;
    Badge badge;
    Participant participant;
}
