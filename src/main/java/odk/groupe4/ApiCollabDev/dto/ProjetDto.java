package odk.groupe4.ApiCollabDev.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.DomaineProjet;
import odk.groupe4.ApiCollabDev.models.enums.SecteurProjet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProjetDto {
    private String titre;
    private String description;
    private DomaineProjet domaine;
    private SecteurProjet secteur;
    private String urlCahierDeCharge;
}
