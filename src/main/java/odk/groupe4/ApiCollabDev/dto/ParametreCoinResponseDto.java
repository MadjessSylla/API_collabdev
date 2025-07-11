package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametreCoinResponseDto {
    private int id;
    private String nom;
    private String description;
    private String typeEvenementLien;
    private int valeur;
    private String createurEmail;
}
