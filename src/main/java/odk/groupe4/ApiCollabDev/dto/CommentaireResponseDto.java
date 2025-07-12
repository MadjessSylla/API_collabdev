package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentaireResponseDto {
    @NotBlank
    private String auteur; // Prenom et nom de l'auteur du commentaire
    @NotBlank
    private String contenu; // Contenu du commentaire
    @NotNull
    private String date; // Date de création du commentaire
}
