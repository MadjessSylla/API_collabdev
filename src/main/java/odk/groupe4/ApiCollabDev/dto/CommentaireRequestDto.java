package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de création d'un commentaire (ou d'une réponse si parentId est fourni).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentaireRequestDto {

    @NotBlank(message = "Le contenu du commentaire est requis.")
    private String contenu;

    /**
     * Identifiant du commentaire parent si c'est une réponse (nullable).
     */
    private Integer parentId;
}
