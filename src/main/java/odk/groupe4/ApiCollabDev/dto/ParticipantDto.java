package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantStatus;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ParticipantDto {
    private Integer id;

    @NotNull
    private ParticipantProfil profil; // enum sans @Size

    @NotNull
    private ParticipantStatus statut; // enum sans @Size

    private String scoreQuiz;
    private boolean estDebloque;

    private LocalDate datePostulation;
    private String commentaireMotivation;
    private String commentaireExperience;

    private Integer projetId;
    private Integer contributeurId;
}
