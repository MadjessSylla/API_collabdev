package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dto.BadgeCoinDescDto;
import odk.groupe4.ApiCollabDev.dto.BadgeRequestDto;
import odk.groupe4.ApiCollabDev.dto.BadgeResponseDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Badge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BadgeService {
    private final BadgeDao badgeDao;
    private final AdministrateurDao administrateurDao;

    @Autowired
    public BadgeService(BadgeDao badgeDao, AdministrateurDao administrateurDao) {
        this.badgeDao = badgeDao;
        this.administrateurDao = administrateurDao;
    }

    /**
     * Crée un nouveau badge.
     *
     * @param dto les informations du badge à créer
     * @param idAdmin l'ID de l'administrateur créateur
     * @return les détails du badge créé
     */
    public BadgeResponseDto creerBadge(BadgeRequestDto dto, int idAdmin){
        // Vérification de l'existence de l'administrateur
        Administrateur admin = administrateurDao.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'id: " + idAdmin));

        // Création du badge
        Badge badge = new Badge();
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setNombreContribution(dto.getNombreContribution());
        badge.setCoin_recompense(dto.getCoin_recompense());
        badge.setCreateur(admin);

        // Enregistrement du badge
        Badge savedBadge = badgeDao.save(badge);
        // Retourne les détails du badge créé
        return mapToResponseDto(savedBadge);
    }

    /**
     * Récupère un badge par son ID.
     *
     * @param idBadge l'ID du badge à récupérer
     * @return les détails du badge trouvé
     */
    public BadgeResponseDto obtenirBadgeParId(int idBadge) {
        // Recherche du badge par ID
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + idBadge));
        return mapToResponseDto(badge);
    }

    /**
     * Récupère tous les badges.
     *
     * @return la liste de tous les badges
     */
    public List<BadgeResponseDto> obtenirTousLesBadges() {
        // Récupération de tous les badges et transformation en DTO
        return badgeDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour un badge existant.
     *
     * @param idBadge l'ID du badge à mettre à jour
     * @param dto les nouvelles informations du badge
     * @return les détails du badge mis à jour
     */
    public BadgeResponseDto mettreAJourBagde(int idBadge, BadgeRequestDto dto) {
        // Vérification de l'existence du badge
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + idBadge));

        // Mise à jour des informations du badge
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setNombreContribution(dto.getNombreContribution());
        badge.setCoin_recompense(dto.getCoin_recompense());

        // Enregistrement du badge mis à jour
        Badge updatedBadge = badgeDao.save(badge);
        // Retourne les détails du badge mis à jour
        return mapToResponseDto(updatedBadge);
    }

    /**
     * Met à jour le nombre de coins et la description d'un badge.
     *
     * @param idBadge l'ID du badge à mettre à jour
     * @param dto les nouvelles informations de coins et description
     * @return les détails du badge mis à jour
     */
    public BadgeResponseDto mettreAJourCoinEtDescription(int idBadge, BadgeCoinDescDto dto) {
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + idBadge));

        // Mise à jour des informations de coins et description
        badge.setCoin_recompense(dto.getCoin_recompense());
        badge.setDescription(dto.getDescription());

        // Enregistrement du badge mis à jour
        Badge updatedBadge = badgeDao.save(badge);
        // Retourne les détails du badge mis à jour
        return mapToResponseDto(updatedBadge);
    }

    /**
     * Supprime un badge par son ID.
     *
     * @param idBadge l'ID du badge à supprimer
     */
    public void supprimerBadge(int idBadge) {
        // Vérification de l'existence du badge avant la suppression
        if (!badgeDao.existsById(idBadge)) {
            throw new RuntimeException("Badge non trouvé avec l'id: " + idBadge);
        }
        // Suppression du badge
        badgeDao.deleteById(idBadge);
    }

    /**
     * Mappe un objet Badge en BadgeResponseDto.
     *
     * @param badge l'objet Badge à mapper
     * @return l'objet BadgeResponseDto correspondant
     */
    private BadgeResponseDto mapToResponseDto(Badge badge) {
        return new BadgeResponseDto(
                badge.getId(),
                badge.getType(),
                badge.getDescription(),
                badge.getNombreContribution(),
                badge.getCoin_recompense(),
                badge.getCreateur() != null ? badge.getCreateur().getEmail() : null
        );
    }
}
