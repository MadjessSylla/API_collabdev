package odk.groupe4.ApiCollabDev.controllers;

import odk.groupe4.ApiCollabDev.dto.ParametreCoinDto;
import odk.groupe4.ApiCollabDev.models.ParametreCoin;
import odk.groupe4.ApiCollabDev.service.ParametreCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/parametres-coins")
public class ParametreCoinController {

    private final ParametreCoinService parametreCoinService;

    @Autowired
    public ParametreCoinController(ParametreCoinService parametreCoinService) {
        this.parametreCoinService = parametreCoinService;
    }
    // Ici, vous pouvez ajouter des méthodes pour gérer les requêtes HTTP
    // POST
    @PostMapping("/admin/{id}")
    public ParametreCoin creerParametreCoin(@PathVariable("id") int idAdmin, @RequestBody ParametreCoinDto dto) {
        return parametreCoinService.creerParametreCoin(idAdmin, dto);
    }

    // GET ALL
    @GetMapping
    public List<ParametreCoin> obtenirTousLesParametresCoins() {
        return parametreCoinService.obtenirTousLesParametresCoins();
    }

    // PUT
    @PutMapping("/{id}")
    public ParametreCoin modifierParametreCoin(@PathVariable("id") int id, @RequestBody ParametreCoinDto dto) {
        return parametreCoinService.modifierParametreCoin(id, dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String supprimerParametreCoin(@PathVariable("id") int id) {
        return parametreCoinService.supprimerParametreCoin(id);
    }
}
