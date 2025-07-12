package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
/* L'annotation Inheritance permet de spécifier la stratégie d'héritage
pour les entités JPA.

Ici, nous utilisons la stratégie JOINED, qui crée une table pour la classe de base
et des tables séparées pour les sous-classes.

Les sous-classes auront une clé étrangère qui référence la clé primaire de la classe de base.*/
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private int id;

    @Column(length = 60, unique = true)
    private String email; // Adresse e-mail de l'utilisateur

    @Column(length = 100, unique = true)
    private String password; // Mot de passe de l'utilisateur

    @Column(nullable = false)
    private boolean actif; // actif par défaut

    // Un utilisateur peut avoir plusieurs notifications
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();
}