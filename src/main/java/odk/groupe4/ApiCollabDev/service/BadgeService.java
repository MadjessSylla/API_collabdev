package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dto.BadgeDto;
import odk.groupe4.ApiCollabDev.models.Badge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BadgeService {
    @Autowired
    private BadgeDao badgeDao;

    public Badge ajouterBadge(BadgeDto badge){
        Badge badge1 = new Badge();
        //
        badge1.setId(badge.getId());
        badge1.setType(badge.getType());
        badge1.setDescription(badge.getDescription());
        badge1.setCoin_recompense(badge.getCoin_recompense());
        badge1.getAdministrateur();
        //
        return  badgeDao.save(badge1);
    }
}
