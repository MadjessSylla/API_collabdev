package odk.groupe4.ApiCollabDev.dto;


import lombok.*;
import odk.groupe4.ApiCollabDev.models.Participant;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentaireDto {
    private String contenu; // Contenu du commentaire
    private LocalDate date; // Date de création du commentaire
}
