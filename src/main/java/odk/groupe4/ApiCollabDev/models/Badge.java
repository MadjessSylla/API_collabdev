package odk.groupe4.ApiCollabDev.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odk.groupe4.ApiCollabDev.models.enums.TypeBadge;

import java.util.HashSet;
import java.util.Set;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_badge")
    private int id;
    @Enumerated(EnumType.STRING)
    private TypeBadge type;
    private String description;
    private int nombreContribution;
    private int coin_recompense;

    // Administrateur qui a créé le badge
    @ManyToOne
    @JoinColumn(name = "id_administrateur")
    private Administrateur administrateur;

    // Clé de reference vers la classe association Badge_Participation
    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Badge_participant> badgeParticipants = new HashSet<>();
}
