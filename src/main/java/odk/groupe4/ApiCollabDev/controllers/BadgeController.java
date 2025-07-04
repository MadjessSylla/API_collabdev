package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.BadgeDto;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private BadgeService badgeService;

    @Autowired
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    // POST
    @PostMapping
    public Badge creerBadge(@RequestBody BadgeDto badge) {
        return badgeService.creerBadge(badge);
    }

    // GET By ID
    @GetMapping("/{idBadge}")
    public Badge obtenirBadgeParId(@PathVariable("{idBadge}") int idBadge) {
        return badgeService.obtenirBadgeParId(idBadge);
    }
    // GET All

    // PUT

    // PATCH

    // DELETE
}
