package odk.groupe4.ApiCollabDev.dto;



import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectLevel;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import odk.groupe4.ApiCollabDev.models.enums.ProjectStatus;

import java.time.LocalDate;

public class ProjetResponseDto {
    private int id;
    private String titre;
    private String description;
    private ProjectDomain domaine;
    private ProjectSector secteur;
    private String urlCahierDeCharge;
    private ProjectStatus status;
    private ProjectLevel niveau;
    private LocalDate dateCreation;
    private String createurNom;
    private String createurPrenom;
    private String validateurEmail;
    private int nombreParticipants;
    private int nombreFonctionnalites;
    private LocalDate dateEcheance;
    private String gestionnaireNom;
    private String gestionnairePrenom;

    public ProjetResponseDto(int id, String titre, String description, ProjectDomain domaine,
                             ProjectSector secteur, String urlCahierDeCharge, ProjectStatus status,
                             ProjectLevel niveau, LocalDate dateCreation, String createurNom,
                             String createurPrenom, String validateurEmail, int nombreParticipants,
                             int nombreFonctionnalites, LocalDate dateEcheance,
                             String gestionnaireNom, String gestionnairePrenom) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.domaine = domaine;
        this.secteur = secteur;
        this.urlCahierDeCharge = urlCahierDeCharge;
        this.status = status;
        this.niveau = niveau;
        this.dateCreation = dateCreation;
        this.createurNom = createurNom;
        this.createurPrenom = createurPrenom;
        this.validateurEmail = validateurEmail;
        this.nombreParticipants = nombreParticipants;
        this.nombreFonctionnalites = nombreFonctionnalites;
        this.dateEcheance = dateEcheance;
        this.gestionnaireNom = gestionnaireNom;
        this.gestionnairePrenom = gestionnairePrenom;
    }

    // Getters and setters for each field can be added here if needed
}
