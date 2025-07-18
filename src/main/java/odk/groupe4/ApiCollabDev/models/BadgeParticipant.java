package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BadgeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_badge_participant")
    private int id;

    private LocalDate dateAcquisition;

    // Un badge participant est associé à un badge spécifique
    @ManyToOne
    @JoinColumn(name = "id_bagde")
    private Badge badge;

    // Un badge participant est associé à un participant spécifique
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;
}
