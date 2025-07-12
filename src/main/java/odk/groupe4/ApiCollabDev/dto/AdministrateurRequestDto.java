package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class AdministrateurRequestDto {
    @NotBlank(message = "L’email est obligatoire.")
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Le mot de passe doit contenir entre 6 et 20 caractères.")
    private String password;
}
