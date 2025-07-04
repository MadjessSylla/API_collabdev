package odk.groupe4.ApiCollabDev.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.Profil;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Participant_projetDto {
    private Profil profil;
    private Projet projet;
    private Contributeur contributeur;

}
