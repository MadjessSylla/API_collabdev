package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.Participant;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class BadgeParticipantDto {
    @NotNull
    LocalDate dateAcquisition;
    @NotNull
    Badge badge;
    @NotNull
    Participant participant;
}
