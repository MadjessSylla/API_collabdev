package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.NiveauProfil;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {

    @Id // Permet de spécifier que cette propriété est notre clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Spécifie que l'AUTO_Increment est géré par notre BD.
    @Column(name = "id_utilisateur")
    private int id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 80)
    private String password;

    @Enumerated(EnumType.STRING)
    private NiveauProfil niveauProfil;

}
