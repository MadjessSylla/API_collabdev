package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Badge_participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_badge_participant")
    private int id;
    private LocalDate dateAcquisition; // Date d'acquisition du badge par le participant

    // Clé étrangère de Badge (ManyToMany)
    @ManyToOne
    @JoinColumn(name = "id_bagde")
    private Badge badge;

    // Clé étrangère de Participant (ManyToMany)
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;
}
