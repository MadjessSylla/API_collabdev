package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dto.BadgeCoinDescDto;
import odk.groupe4.ApiCollabDev.dto.BadgeDto;
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

    public BadgeResponseDto creerBadge(BadgeDto dto, int idAdmin){
        Administrateur admin = administrateurDao.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'id: " + idAdmin));
        
        Badge badge = new Badge();
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setNombreContribution(dto.getNombreContribution());
        badge.setCoin_recompense(dto.getCoin_recompense());
        badge.setCreateur(admin);
        
        Badge savedBadge = badgeDao.save(badge);
        return mapToResponseDto(savedBadge);
    }

    public BadgeResponseDto obtenirBadgeParId(int idBadge) {
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + idBadge));
        return mapToResponseDto(badge);
    }

    public List<BadgeResponseDto> obtenirTousLesBadges() {
        return badgeDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public BadgeResponseDto mettreAJourBagde(int idBadge, BadgeDto dto) {
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + idBadge));
        
        badge.setType(dto.getType());
        badge.setDescription(dto.getDescription());
        badge.setNombreContribution(dto.getNombreContribution());
        badge.setCoin_recompense(dto.getCoin_recompense());
        
        Badge updatedBadge = badgeDao.save(badge);
        return mapToResponseDto(updatedBadge);
    }

    public BadgeResponseDto mettreAJourCoinEtDescription(int idBadge, BadgeCoinDescDto dto) {
        Badge badge = badgeDao.findById(idBadge)
                .orElseThrow(() -> new RuntimeException("Badge non trouvé avec l'id: " + idBadge));
        
        badge.setCoin_recompense(dto.getCoin_recompense());
        badge.setDescription(dto.getDescription());
        
        Badge updatedBadge = badgeDao.save(badge);
        return mapToResponseDto(updatedBadge);
    }

    public void supprimerBadge(int idBadge) {
        if (!badgeDao.existsById(idBadge)) {
            throw new RuntimeException("Badge non trouvé avec l'id: " + idBadge);
        }
        badgeDao.deleteById(idBadge);
    }

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
