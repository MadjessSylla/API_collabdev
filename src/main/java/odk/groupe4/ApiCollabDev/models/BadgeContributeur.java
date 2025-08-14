package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id_badge", "id_contributeur"}))
public class BadgeContributeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_badge_contributeur")
    private int id;

    private LocalDate dateAcquisition;

    // Badge associé
    @ManyToOne
    @JoinColumn(name = "id_badge")
    private Badge badge;

    // Contributeur associé
    @ManyToOne
    @JoinColumn(name = "id_contributeur")
    private Contributeur contributeur;
}
