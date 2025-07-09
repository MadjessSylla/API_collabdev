package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.AdministrateurDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping ("/api/admin")
public class AdministrateurController {
    private final AdministrateurService administrateurService;

    @Autowired
    public AdministrateurController(AdministrateurService administrateurService) {
        this.administrateurService = administrateurService;
    }

    // Afficher tous les admins (GET /admin)
    @GetMapping
    public List<Administrateur> getAllAdmins() {
        return administrateurService.getAll();
    }

    // Créer un admin (POST /admin)
    @PostMapping
    public Administrateur createAdmin(@RequestBody AdministrateurDto adminDto) {
        return administrateurService.create(adminDto);
    }

    // Mettre à jour un admin (PUT /admin/{id})
    @PutMapping("/{id}")
    public Administrateur updateAdmin(@PathVariable Integer id, @RequestBody AdministrateurDto dto) {
        return administrateurService.update(id, dto);
    }

    // Supprimer un admin (DELETE /admin/{id})
    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Integer id) {
        administrateurService.delete(id);
    }

    // Bloquer un admin (PUT /admin/block/{id})
    @PutMapping("/block/{id}")
    public Administrateur blockAdmin(@PathVariable Integer id) {
        return administrateurService.block(id);
    }

    // Débloquer un admin (PUT /admin/unblock/{id})
    @PutMapping("/unblock/{id}")
    public Administrateur unblockAdmin(@PathVariable Integer id) {
        return administrateurService.unblock(id);
    }
}
