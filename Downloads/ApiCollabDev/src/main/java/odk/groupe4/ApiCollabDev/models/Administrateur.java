package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity @PrimaryKeyJoinColumn(name = "id_administrateur")
public class Administrateur extends Utilisateur{
}
