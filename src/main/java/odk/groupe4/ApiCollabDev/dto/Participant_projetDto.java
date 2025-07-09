package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.enums.Profil;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor
public class Participant_projetDto {
    private Contributeur contributeur;
    private Profil profil;
    private String reponseQuiz;
}
