package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id_contribution")
    private int id;
    @Column(length = 10)
    private String lienUrl;
    @Column(length = 100)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private StatusContribution status;
    private LocalDate dateCreation;

    // Clé étrangère de la table Participant. La contribution est soumise à un participant.
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;

    // Clé étrangère de la table Participant. La contribution est validée ou refusée par un gestionnaire.
   @ManyToOne
   @JoinColumn(name = "id_projet")
   private Participant gestionnaire;

    // Clé étrangère de la table Fonctionnalite. La contribution est liée à une fonctionnalité.
    @OneToOne
    @JoinColumn(name = "id_fonctionnalite")
    private Fonctionnalite fonctionnalite;
    // Clé étrangère de la table Participant. La contribution est liée à un participant.
    @ManyToOne
    @JoinColumn(name = "id_participants")
    private Participant participants;
}
