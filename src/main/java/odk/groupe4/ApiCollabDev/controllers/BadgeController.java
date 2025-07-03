package odk.groupe4.ApiCollabDev.controllers;

import lombok.AllArgsConstructor;
import odk.groupe4.ApiCollabDev.dao.BadgeDao;
import odk.groupe4.ApiCollabDev.dao.Badge_participantDao;
import odk.groupe4.ApiCollabDev.dto.BadgeDto;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/Badge")
public class BadgeController {
    @Autowired
    private BadgeService badgeService;
    private BadgeDao badgeDao;


    @PostMapping
    public Badge creerBadge(@RequestBody BadgeDto badge){
        return badgeService.ajouterBadge(badge);

    }
}
