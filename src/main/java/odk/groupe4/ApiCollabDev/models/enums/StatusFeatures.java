package odk.groupe4.ApiCollabDev.models.enums;

public enum StatusFeatures {
    A_FAIRE("À faire"),
    EN_COURS("En cours"),
    TERMINEE("Terminée");

    private final String description;

    StatusFeatures(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
