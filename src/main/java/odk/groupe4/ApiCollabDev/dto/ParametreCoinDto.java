package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ParametreCoinDto {
    @NotBlank @Size(max = 30)
    private String nom;
    @NotBlank @Size (max = 100)
    private String description;
    @NotBlank @Size(max = 30)
    private String typeEvenementLien;
    @NotNull @Positive
    private int valeur;

}
