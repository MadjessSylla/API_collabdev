package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class FonctionnaliteDto {
    @NotNull
    private int id;
    @NotNull
    private int idProjet;
    @NotBlank @Size(max = 50)
    private String titre;
    @NotBlank @Size(max = 50)
    private String contenu;
    @NotBlank @Size(max = 50)
    private String nom;
    @NotBlank @Size(max = 50)
    private String prenom;
    @NotBlank @Size(max = 50)
    private String email;
}
