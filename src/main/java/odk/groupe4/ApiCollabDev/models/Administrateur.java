package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;


@Entity @PrimaryKeyJoinColumn(name = "id_administrateur")
@Getter @Setter
public class Administrateur extends Utilisateur{
}
