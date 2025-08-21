package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantProfil;
import odk.groupe4.ApiCollabDev.models.enums.ParticipantStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantResponseDto {
    private int id;
    private ParticipantProfil profil;
    private ParticipantStatus statut;
    private String scoreQuiz;
    private boolean estDebloque;
    private String contributeurNom;
    private String contributeurPrenom;
    private String contributeurEmail;
    private String projetTitre;
}
