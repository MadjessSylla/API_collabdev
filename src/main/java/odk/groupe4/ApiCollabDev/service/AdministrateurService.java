package odk.groupe4.ApiCollabDev.service;


import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dto.AdministrateurDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Déclare cette classe comme composant métier géré par Spring
public class AdministrateurService {
    @Autowired // Injecte automatiquement l’interface DAO
    private AdministrateurDao adminDao;

    // Créer un admin à partir d’un DTO
    public Administrateur create(AdministrateurDto dto) {
        Administrateur admin = new Administrateur(); // Instanciation
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setActif(true); // actif par défaut
        return adminDao.save(admin); // Sauvegarde en BDD
    }

    // Modifier un admin
    public Administrateur update(Integer id, AdministrateurDto dto) {
        Optional<Administrateur> adminOpt = adminDao.findById(id);
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get();
            admin.setEmail(dto.getEmail());
            admin.setPassword(dto.getPassword());
            return adminDao.save(admin); // Mise à jour
        }
        return null;
    }

    // Supprimer un admin
    public void delete(Integer id) {
        adminDao.deleteById(id);
    }

    // Bloquer un compte admin
    public Administrateur block(Integer id) {
        Optional<Administrateur> adminOpt = adminDao.findById(id);
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get();
            admin.setActif(false); // On le désactive
            return adminDao.save(admin);
        }
        return null;
    }

    // Afficher tous les admins
    public List<Administrateur> getAll() {
        return adminDao.findAll();
    }
}
