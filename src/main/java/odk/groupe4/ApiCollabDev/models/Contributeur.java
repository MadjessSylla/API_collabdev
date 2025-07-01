package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @AllArgsConstructor @NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id_contributeur")
public class Contributeur extends Utilisateur {

    @Column(length = 45)
    private String nom;

    @Column(length = 30)
    private String prenom;

    @Column(length = 15)
    private String telephone;

    private int totalCoin;

    // Clé de réference pour l'association plusieurs à plusieurs vers la table Participants
    @OneToMany(mappedBy = "contributeur")
    private Set<Participant> participations = new HashSet<>();
}
