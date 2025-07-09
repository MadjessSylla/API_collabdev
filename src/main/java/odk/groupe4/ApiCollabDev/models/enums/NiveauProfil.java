package odk.groupe4.ApiCollabDev.models.enums;

public enum NiveauProfil {
    DEBUTANT("Débutant"),
    INTERMEDIAIRE("Intermédiaire"),
    AVANCE("Avancé"),
    EXPERT("Expert");

    private final String niveau;

    NiveauProfil(String niveau) {
        this.niveau = niveau;
    }

    public String getNiveau() {
        return niveau;
    }
}
