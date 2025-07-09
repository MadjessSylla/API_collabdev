package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UtilisateurDto {
    @NotBlank(message = "L’email est obligatoire.")
    private String email;

    @Size(min = 6, max = 20, message = "Le mot de passe doit contenir entre 6 et 20 caractères.")
    private String password;

}
