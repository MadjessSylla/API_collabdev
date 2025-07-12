package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dao.BadgeParticipantDao;
import odk.groupe4.ApiCollabDev.dao.ParticipantDao;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.models.BadgeParticipant;
import odk.groupe4.ApiCollabDev.models.Participant;
import odk.groupe4.ApiCollabDev.dto.BadgeParticipantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BadgeParticipantService {
    private BadgeDao badgeDao;
    private ParticipantDao participantDao;
    private BadgeParticipantDao badgeParticipantDao;

    @Autowired
    public BadgeParticipantService(BadgeParticipantDao badgeParticipantDao, ParticipantDao participantDao, BadgeDao badgeDao) {
        this.badgeParticipantDao = badgeParticipantDao;
        this.participantDao = participantDao;
        this.badgeDao = badgeDao;
    }

    /** Afficher tous les badges des participants
     * @return une liste de BadgeParticipant
     * Cette méthode récupère tous les badges associés aux participants dans la base de données.
     */
    public List<BadgeParticipant> afficherBadgeParticipant() {
        return badgeParticipantDao.findAll();
    }

    /**
     * Attribuer un badge à un participant
     * @param badgeParticipantDto : DTO contenant les informations du participant et du badge
     * @return le BadgeParticipant créé
     * Cette méthode attribue un badge spécifique à un participant en créant une nouvelle entrée dans la base de données.
     */
    public BadgeParticipant attribuerBadge(BadgeParticipantDto badgeParticipantDto) {
        return attribuerBadge(badgeParticipantDto.getParticipant().getId(), badgeParticipantDto.getBadge().getId());
    }

    /**
     * Attribuer un badge à un participant
     * @param idParticipant : ID du participant
     * @param idBadge : ID du badge à attribuer
     * @return le BadgeParticipant créé
     * Cette méthode attribue un badge spécifique à un participant en créant une nouvelle entrée dans la base de données.
     */
    public BadgeParticipant attribuerBadge(int idParticipant, int idBadge) {
        // Récupération des informations du badge et du participant à partir de l'ID
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new IllegalArgumentException("Badge non trouvé avec l'ID: " + idBadge));
        Participant p = participantDao.findById(idParticipant)
                .orElseThrow(() -> new IllegalArgumentException("Participant non trouvé avec l'ID: " + idParticipant));
        // Création d'un nouvel objet BadgeParticipant
        BadgeParticipant badgeParticipant = new BadgeParticipant();
        badgeParticipant.setBadge(badge); // Association du badge
        badgeParticipant.setParticipant(p); // Association du participant
        badgeParticipant.setDateAcquisition(LocalDate.now()); // Date d'acquisition du badge
        // Enregistrement du badgeParticipant dans la base de données
        return badgeParticipantDao.save(badgeParticipant);
    }
}
