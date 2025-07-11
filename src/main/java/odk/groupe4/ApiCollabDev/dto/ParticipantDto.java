package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;

@Data @NoArgsConstructor @AllArgsConstructor
public class ParticipantDto {
    @NotNull
    private ParticipantProfil profil;
    @NotBlank @Size(max = 4)
    private String scoreQuiz;
}
