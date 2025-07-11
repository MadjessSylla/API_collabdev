package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private int id;
    private String email;
    private String type; // "CONTRIBUTEUR" ou "ADMINISTRATEUR"
    private String nom;
    private String prenom;
    private boolean actif;
    private String message;
}
