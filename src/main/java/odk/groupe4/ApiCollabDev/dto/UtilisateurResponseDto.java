package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponseDto {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private boolean actif;
    private String type;
    private Integer pointExp;
    private Integer totalCoin;
    private String biographie;
    private String photoProfilUrl;
}
