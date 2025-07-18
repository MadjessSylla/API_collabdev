package odk.groupe4.ApiCollabDev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // Un badge est créé par un administrateur
    @ManyToOne
    @JoinColumn(name = "id_administrateur")
    @JsonIgnore
    private Administrateur createur;

    // Un badge peut être attribué à plusieurs participants
    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<BadgeParticipant> badgeParticipants = new HashSet<>();
}
