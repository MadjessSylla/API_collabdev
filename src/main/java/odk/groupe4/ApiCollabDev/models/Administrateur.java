package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
/* L'annotation PrimaryKeyJoinColumn est utilisée pour spécifier
la colonne de clé primaire qui est jointe à la table de la classe parente
dans une relation d'héritage JOINED.*/
@PrimaryKeyJoinColumn(name = "id_administrateur")
public class Administrateur extends Utilisateur{
}
