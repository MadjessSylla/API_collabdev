package odk.groupe4.ApiCollabDev.models.enums;

public enum StatusContribution {
    ENVOYE("En attente de validation"),
    VALIDEE("Validée"),
    REJETEE("Rejetée");

    private final String description;

    StatusContribution(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
