package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;


@Entity @PrimaryKeyJoinColumn(name = "id_administrateur")
@Getter @Setter
public class Administrateur extends Utilisateur{

    // On ajoute ici le champ "actif" propre à l'administrateur
    @Column(nullable = false)
    private boolean actif = true; // actif par défaut
}
