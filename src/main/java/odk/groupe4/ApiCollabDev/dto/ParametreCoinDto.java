package odk.groupe4.ApiCollabDev.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ParametreCoinDto {
    private String nom;
    private String description;
    private String typeEvenementLien;
    private int valeur;

}
