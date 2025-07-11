package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributeurResponseDto {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private int pointExp;
    private int totalCoin;
    private boolean actif;
}
