package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.Contributeur;
import odk.groupe4.ApiCollabDev.models.Contribution;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class NotificationDto {
    private String sujet;
    private String contenu;
    private Contribution contribution;
    private Contributeur contributeur;
}
