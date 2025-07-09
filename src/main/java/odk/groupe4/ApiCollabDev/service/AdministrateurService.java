package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dto.AdministrateurDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrateurService {
    private AdministrateurDao adminDao;

    @Autowired
    public AdministrateurService(AdministrateurDao adminDao) {
        this.adminDao = adminDao;
    }

    /**
     * Créer un nouvel administrateur
     * @param dto : DTO contenant les informations de l'administrateur
     * @return l'administrateur créé
     * Cette méthode crée un nouvel administrateur à partir des données fournies dans le DTO.
     * Elle initialise l'administrateur avec un email, un mot de passe et le statut actif par défaut.
    */
    public Administrateur create(AdministrateurDto dto) {
        // Création d'un nouvel administrateur à partir du DTO
        Administrateur admin = new Administrateur();
        admin.setEmail(dto.getEmail()); // Email de l'administrateur
        admin.setPassword(dto.getPassword()); // Mot de passe de l'administrateur
        admin.setActif(true); // L'administrateur est actif par défaut
        // Enregistrement de l'administrateur dans la base de données
        return adminDao.save(admin);
    }

    /**
     * Mettre à jour un administrateur existant
     * @param id : ID de l'administrateur à mettre à jour
     * @param dto : DTO contenant les nouvelles informations de l'administrateur
     * @return l'administrateur mis à jour ou null si l'administrateur n'existe pas
     * Cette méthode met à jour les informations d'un administrateur existant
     * en fonction de l'ID fourni.
     * */
    public Administrateur update(Integer id, AdministrateurDto dto) {
        // Recherche de l'administrateur par ID
        Optional<Administrateur> adminOpt = adminDao.findById(id);
        // Si l'administrateur existe, on met à jour ses informations
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get(); // Récupération de l'administrateur existant
            admin.setEmail(dto.getEmail()); // Mise à jour de l'email
            admin.setPassword(dto.getPassword()); // Mise à jour du mot de passe
            // Mise à jour de l'administrateur dans la base de données
            return adminDao.save(admin);
        }
        // Si l'administrateur n'existe pas, on retourne null
        return null;
    }

    /**Supprimer un administrateur par ID
     * @param id : ID de l'administrateur à supprimer
     * Cette méthode supprime un administrateur de la base de données en fonction de son ID.
     */
    public void delete(Integer id) {
        if (!adminDao.existsById(id)){
            throw new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas.");
        }
        // Suppression de l'administrateur par ID
        adminDao.deleteById(id);
    }

    /** Bloquer un compte administrateur
     * @param id : ID de l'administrateur à bloquer
     * @return l'administrateur bloqué ou null si l'administrateur n'existe pas
     * Cette méthode bloque un administrateur en le désactivant.
     */
    public Administrateur block(Integer id) {
        // Recherche de l'administrateur par ID
        Optional<Administrateur> adminOpt = adminDao.findById(id);
        // Si l'administrateur existe, on le désactive
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get();
            admin.setActif(false); // On le désactive
            // Mise à jour de l'administrateur dans la base de données
            return adminDao.save(admin);
        }
        // Si l'administrateur n'existe pas, on retourne null
        return null;
    }

    /** Débloquer un compte administrateur
     * @param id : ID de l'administrateur à débloquer
     * @return l'administrateur débloqué ou null si l'administrateur n'existe pas
     * Cette méthode réactive un administrateur en le rendant actif.
     */
    public Administrateur unblock(Integer id) {
        // Recherche de l'administrateur par ID
        Optional<Administrateur> adminOpt = adminDao.findById(id);
        // Si l'administrateur existe, on le réactive
        if (adminOpt.isPresent()) {
            Administrateur admin = adminOpt.get();
            admin.setActif(true); // On le réactive
            // Mise à jour de l'administrateur dans la base de données
            return adminDao.save(admin);
        }
        // Si l'administrateur n'existe pas, on retourne null
        return null;
    }

    /** Récupérer tous les administrateurs
     * @return la liste de tous les administrateurs
     * Cette méthode retourne une liste de tous les administrateurs présents dans la base de données.
     */
    public List<Administrateur> getAll() {
        return adminDao.findAll();
    }
}
