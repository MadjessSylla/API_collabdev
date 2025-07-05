package odk.groupe4.ApiCollabDev.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.Participant;

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CommentaireDto {
    private String contenu;
    private Contribution contribution;
    private Participant participant;
}
