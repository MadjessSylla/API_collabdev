package odk.groupe4.ApiCollabDev.models.enums;

public enum TypeBadge {
    BRONZE("Bronze"),
    ARGENT("Argent"),
    OR("Or"),
    PLATINE("Platine");

    private final String description;

    TypeBadge(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
