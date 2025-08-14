package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class NotificationResponseDto {
    private int id;
    private String sujet;
    private String message;
    private LocalDateTime dateCreation;
    private boolean lu;
    private String utilisateurNom;
    private String utilisateurPrenom;
    private String utilisateurEmail;
}
