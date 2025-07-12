package odk.groupe4.ApiCollabDev.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data @AllArgsConstructor @NoArgsConstructor
public class CommentaireRequestDto {
    @NotBlank @Size(max = 100)
    private String contenu; // Contenu du commentaire
    @NotNull
    private LocalDate date; // Date de création du commentaire
}
