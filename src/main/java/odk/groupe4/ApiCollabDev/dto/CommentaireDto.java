package odk.groupe4.ApiCollabDev.dto;

import lombok.*;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentaireDto {
    private String contenu; // Contenu du commentaire
    private LocalDate date; // Date de création du commentaire
}
