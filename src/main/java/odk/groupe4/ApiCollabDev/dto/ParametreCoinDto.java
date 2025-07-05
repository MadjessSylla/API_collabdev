package odk.groupe4.ApiCollabDev.dto;

import jakarta.persistence.Entity;
import lombok.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
public class ParametreCoinDto {
    private String nom;
    private String description;
    private String typeEvenementLien;
    private int valeur;

}
