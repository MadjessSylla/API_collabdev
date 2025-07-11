package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponseDto {
    private int id;
    private String email;
    private String type;
    private boolean actif;
    private String nom;
    private String prenom;
    private String telephone;
    private Integer pointExp;
    private Integer totalCoin;
}
