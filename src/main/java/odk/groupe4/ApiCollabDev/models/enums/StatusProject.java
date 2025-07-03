package odk.groupe4.ApiCollabDev.models.enums;

public enum StatusProject {
    EN_ATTENTE("En attente de validation"),
    OUVERT("Ouvert"),
    EN_COURS("En cours"),
    TERMINE("Terminé");

    private final String description;

    StatusProject(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
