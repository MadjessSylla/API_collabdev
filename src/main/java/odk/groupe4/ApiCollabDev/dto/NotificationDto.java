package odk.groupe4.ApiCollabDev.dto;

import lombok.*;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Contribution;

@Data @AllArgsConstructor @NoArgsConstructor
public class NotificationDto {
    private String sujet;
    private String contenu;
    private Contribution contribution;
    private Contributeur contributeur;
}
