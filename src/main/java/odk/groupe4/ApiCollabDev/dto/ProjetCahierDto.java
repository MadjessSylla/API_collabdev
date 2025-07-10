package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class ProjetCahierDto {
    @NotBlank
    private String urlCahierDeCharge;
}
