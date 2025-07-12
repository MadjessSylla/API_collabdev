package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class ContributeurRequestDto {
    @NotBlank(message = "Nom obligatoire.")
    private String nom;
    @NotBlank(message = "Prenom obligatoire.")
    private String prenom;
    @NotBlank(message = "Le num de Telephone obligatoire.")
    @Size(min = 6, max = 20, message = "Le Numero de telephone  doit contenir entre 8 et 25 caractères.")
    private String telephone;
    @NotBlank(message = "L’email est obligatoire.") @Email
    private String email;
    @Size(min = 6, max = 20, message = "Le mot de passe doit contenir entre 6 et 20 caractères.")
    private String password;

}
