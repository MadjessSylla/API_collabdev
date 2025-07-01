package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id_commentaire")
    private int id;

    private String contenu;

    // Clé étrnagère de Contribution (ManyToMany)
    @ManyToOne
    @JoinColumn(name = "id_contribution")
    private Contribution contribution;

    // Clé étrangère de Participant (ManyToMany)
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;
}
