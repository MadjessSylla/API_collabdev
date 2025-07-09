package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id_administrateur")
public class Administrateur extends Utilisateur{
}
