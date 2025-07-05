package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
/* L'annotation Inheritance permet de spécifier la stratégie d'héritage
pour les entités JPA.

Ici, nous utilisons la stratégie JOINED, qui crée une table pour la classe de base
et des tables séparées pour les sous-classes.

Les sous-classes auront une clé étrangère qui référence la clé primaire de la classe de base.*/
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {

    @Id // Permet de spécifier que cette propriété est notre clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Spécifie que l'AUTO_Increment est géré par notre BD.
    @Column(name = "id_utilisateur")
    private int id;
    private String email; // Adresse e-mail de l'utilisateur
    private String password; // Mot de passe de l'utilisateur
}