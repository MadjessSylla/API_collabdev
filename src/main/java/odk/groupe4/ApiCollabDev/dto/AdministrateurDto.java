package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;

@Getter @Setter // Permet d’éviter d’écrire manuellement getEmail(), setEmail()...
public class AdministrateurDto extends UtilisateurDto{

    @NotBlank(message = "L’email est obligatoire.")
    private String email; // L’email de l’administrateur

    @Size(min = 6, max = 20, message = "Le mot de passe doit contenir entre 6 et 20 caractères.")
    private String password; // Son mot de passe
}
