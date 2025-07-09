package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.util.ArrayList;
import java.util.List;

@Entity @PrimaryKeyJoinColumn(name = "id_administrateur")
public class Administrateur extends Utilisateur{
    @OneToMany(mappedBy = "administrateur",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Projet> projets = new ArrayList<>();
}
