package odk.groupe4.ApiCollabDev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private int pointExp;

    private int totalCoin;

    // Un contributeur peut participer à plusieurs projets.
    @OneToMany(mappedBy = "contributeur")
    @JsonIgnore
    private Set<Participant> participations = new HashSet<>();

    // Un contributeur peut débloquer plusieurs projets.
    @ManyToMany(mappedBy = "contributeurs")
    @JsonIgnore
    private Set<Projet> projetsDebloques = new HashSet<>();
}
