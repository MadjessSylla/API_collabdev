package odk.groupe4.ApiCollabDev.models.enums;

public enum Profil {
    DEVELOPPEUR("Développeur"),
    DESIGNER("Designer"),
    TESTEUR("Testeur"),
    GESTIONNAIRE("Gestionnaire de projet"),
    ANALYSTE("Analyste"),
    ARCHITECTE("Architecte logiciel"),
    ADMINISTRATEUR("Administrateur système"),
    AUTRE("Autre");

    private final String description;

    Profil(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
