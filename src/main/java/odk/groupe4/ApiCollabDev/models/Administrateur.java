package odk.groupe4.ApiCollabDev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id_administrateur")
public class Administrateur extends Utilisateur{

    // Un administrateur peut créer plusieurs coins
    @OneToMany(mappedBy = "administrateur")
    @JsonIgnore
    private Set<ParametreCoin> parametresCoins;

    // Un administrateur peut créer plusieurs badges de récompense
    @OneToMany(mappedBy = "createur")
    @JsonIgnore
    private Set<Badge> badgesRecompense;

    // Un administrateur peut valider plusieurs projets
    @OneToMany(mappedBy = "validateur")
    @JsonIgnore
    private Set<Projet> projetsValides;
}
