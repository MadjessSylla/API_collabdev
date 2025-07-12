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

    @Column(length = 45, nullable = false)
    private String nom;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(length = 45, nullable = false)
    private String typeEvenementLien;

    private int valeur;

    // Un paramètre de coin est créé par un administrateur
    @ManyToOne
    @JoinColumn(name = "id_administrateur")
    private Administrateur administrateur;
}
