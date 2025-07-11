package odk.groupe4.ApiCollabDev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String lienUrl;
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    private ContributionStatus status;

    private LocalDate dateSoumission;

    @OneToOne
    @JoinColumn(name = "id_fonctionnalite")
    @JsonIgnore
    private Fonctionnalite fonctionnalite;

    @ManyToOne
    @JoinColumn(name = "id_participant")
    @JsonIgnore
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "id_gestionnaire")
    @JsonIgnore
    private Participant gestionnaire;
}
