package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @AllArgsConstructor @NoArgsConstructor
/* L'annotation PrimaryKeyJoinColumn est utilisée pour spécifier
la colonne de clé primaire qui est jointe à la table de la classe parente
dans une relation d'héritage JOINED.*/
@PrimaryKeyJoinColumn(name = "id_contributeur")
public class Contributeur extends Utilisateur {

    @Column(length = 45)
    private String nom; // Nom de famille du contributeur

    @Column(length = 30)
    private String prenom; // Prénom du contributeur

    @Column(length = 15)
    private String telephone;  // Numéro de téléphone du contributeur

    private int pointExp;  // Points d'expérience du contributeur

    private int totalCoin; // Solde de Coin du contributeur

    // Un contributeur peut participer à plusieurs projets.
    @OneToMany(mappedBy = "contributeur")
    private Set<Participant> participations = new HashSet<>();

    // Un contributeur peut débloquer plusieurs projets.
    @ManyToMany(mappedBy = "contributeurs")
    private Set<Projet> projetsDebloques = new HashSet<>();

}
