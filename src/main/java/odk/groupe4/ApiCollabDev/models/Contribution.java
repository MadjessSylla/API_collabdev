package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.ContributionStatus;

import java.time.LocalDate;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contribution")
    private int id;

    private String lienUrl; // Lien vers la contribution (par exemple, un lien vers un dépôt GitHub, une maquette figma, un document, etc.)

    private String fileUrl; // Lien vers un fichier de contribution (par exemple, un fichier de code, une image, un document, etc.) au format binaire

    @Enumerated(EnumType.STRING)
    private ContributionStatus status; // Statut de la contribution (En attente, Acceptée, Rejetée)

    private LocalDate dateSoumission; // Date de soumission de la contribution.

    // La fonctionnalité à laquelle la contribution est associée.
    // Une contribution est liée à une seule fonctionnalité et une fonctionnalité est traitée par une seule contribution.
    @OneToOne
    @JoinColumn(name = "id_fonctionnalite")
    private Fonctionnalite fonctionnalite;

    // Un participant peut soumettre plusieurs contributions, mais une contribution appartient à un seul participant.
    @ManyToOne
    @JoinColumn(name = "id_participant")
    private Participant participant;

    // Un participant Gestionnaire peut valider plusieurs contributions, mais une contribution est validée par un seul participant Gestionnaire.
    @ManyToOne
    @JoinColumn(name = "id_gestionnaire")
    private Participant gestionnaire;
}
