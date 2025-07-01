package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;

import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id_contribution")
    private int id;
    private String lienUrl;
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private StatusContribution status;

    // Clé étrangère de la table Participant
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;

    @OneToMany( mappedBy = "contribution",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Commentaire> commentaires = new HashSet<>();
}
