package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParametreCoin {
    private int id;
    private String nom;
    private String description;
    private String typeEvenementLien;
    private int valeur;

    @ManyToOne
    @JoinColumn(name = "id_administrateur")
    private Administrateur administrateur;
}
