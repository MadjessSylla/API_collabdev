package odk.groupe4.ApiCollabDev.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
=======
>>>>>>> Gestions_Projets
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FonctionnaliteDto {

    private int id;
    private int idProjet;
    private String titre;
    private String contenu;
    private String nom;
    private String prenom;
    private String email;
}
