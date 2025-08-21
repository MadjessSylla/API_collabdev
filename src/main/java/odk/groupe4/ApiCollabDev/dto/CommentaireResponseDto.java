package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de retour d'un commentaire avec son thread (réponses récursives).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentaireResponseDto {
    private int id;
    private String contenu;
    private String creationDate;

    private int auteurId;
    private String auteurNomComplet;
    private String auteurPhotoProfilUrl; // pratique côté UI si disponible

    private Integer parentId;

    private List<CommentaireResponseDto> reponses = new ArrayList<>();
}
