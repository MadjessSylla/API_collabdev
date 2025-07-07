package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class ParametreCoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametre_coin")
    private int id;
    private String nom;
    private String description;
    private String typeEvenementLien;
    private int valeur;

    @ManyToOne
    @JoinColumn(name = "id_administrateur")
    private Administrateur administrateur;
}
