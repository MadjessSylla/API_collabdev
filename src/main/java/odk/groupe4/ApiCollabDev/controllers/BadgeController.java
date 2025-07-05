package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.BadgeCoinDescDto;
import odk.groupe4.ApiCollabDev.dto.BadgeDto;
import odk.groupe4.ApiCollabDev.models.Badge;
import odk.groupe4.ApiCollabDev.service.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {

    private final BadgeService badgeService;

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
    public Badge obtenirBadgeParId(@PathVariable("idBadge") int idBadge) {
        return badgeService.obtenirBadgeParId(idBadge);
    }
    // GET All
    @GetMapping
    public List<Badge> obtenirTousLesBadges(){
        return badgeService.obtenirTousLesBadges();
    }

    // PUT
    @PutMapping("/{idBadge}")
    public Badge miseAJourBadge(@PathVariable("idBadge") int idBadge,@RequestBody BadgeDto badgeDto){
        return badgeService.mettreAJourBagde(idBadge, badgeDto);
    }

    // PATCH
    @PatchMapping("{id}")
    public Badge miseAJourCoinDesc(@PathVariable("id") int idBadge,@RequestBody BadgeCoinDescDto dto){
        return badgeService.mettreAJourCoinEtDescription(idBadge, dto);
    }

    // DELETE
    @DeleteMapping("{id}")
    public String supprimerBadge(@PathVariable("id") int idBadge){
        return badgeService.supprimerBadge(idBadge);
    }
}
