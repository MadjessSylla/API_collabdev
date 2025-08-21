package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.AdministrateurRequestDto;
import odk.groupe4.ApiCollabDev.dto.AdministrateurResponseDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdministrateurService {
    private final AdministrateurDao adminDao;
    private final UtilisateurDao utilisateurDao;

    @Autowired
    public AdministrateurService(AdministrateurDao adminDao, UtilisateurDao utilisateurDao) {
        this.adminDao = adminDao;
        this.utilisateurDao = utilisateurDao;
    }

    /**
     * Crée un nouvel administrateur.
     *
     * @param dto les informations de l'administrateur à créer
     * @return les détails de l'administrateur créé
     * @throws IllegalArgumentException si l'email est déjà utilisé
     */
    public AdministrateurResponseDto create(AdministrateurRequestDto dto) {
        // Vérifier l'unicité de l'email
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        // Créer un nouvel administrateur
        Administrateur admin = new Administrateur();
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setActif(true);

        // Enregistrer l'administrateur dans la base de données
        Administrateur savedAdmin = adminDao.save(admin);
        // Mapper l'administrateur vers le DTO de réponse
        return mapToResponseDto(savedAdmin);
    }

    /**
     * Récupère un administrateur par son ID.
     *
     * @param id l'ID de l'administrateur à récupérer
     * @return les détails de l'administrateur
     * @throws IllegalArgumentException si l'administrateur n'existe pas
     */
    public AdministrateurResponseDto getById(Integer id) {
        // Vérifier si l'administrateur existe
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));
        // Mapper l'administrateur vers le DTO de réponse
        return mapToResponseDto(admin);
    }

    /**
     * Met à jour les informations d'un administrateur.
     *
     * @param id l'ID de l'administrateur à mettre à jour
     * @param dto les nouvelles informations de l'administrateur
     * @return les détails de l'administrateur mis à jour
     * @throws IllegalArgumentException si l'administrateur n'existe pas
     */
    public AdministrateurResponseDto update(Integer id, AdministrateurRequestDto dto) {
        // Vérifier si l'administrateur existe
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));

        // Mettre à jour les informations de l'administrateur
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());

        // Sauvegarder les modifications dans la base de données
        Administrateur updatedAdmin = adminDao.save(admin);
        // Mapper l'administrateur mis à jour vers le DTO de réponse
        return mapToResponseDto(updatedAdmin);
    }

    /**
     * Supprime un administrateur par son ID.
     *
     * @param id l'ID de l'administrateur à supprimer
     * @throws IllegalArgumentException si l'administrateur n'existe pas
     */
    public void delete(Integer id) {
       // Vérifier si l'administrateur existe avant de le supprimer
        if (!adminDao.existsById(id)){
            throw new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas.");
        }
        // Supprimer l'administrateur de la base de données
        adminDao.deleteById(id);
    }

    /**
     * Bloque un administrateur par son ID.
     *
     * @param id l'ID de l'administrateur à bloquer
     * @return les détails de l'administrateur bloqué
     * @throws IllegalArgumentException si l'administrateur n'existe pas
     */
    public AdministrateurResponseDto block(Integer id) {
       // Vérifier si l'administrateur existe
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));

        // Bloquer l'administrateur en le marquant comme inactif
        admin.setActif(false);
        // Enregistrer l'administrateur bloqué dans la base de données
        Administrateur blockedAdmin = adminDao.save(admin);
        // Mapper l'administrateur bloqué vers le DTO de réponse
        return mapToResponseDto(blockedAdmin);
    }

    /**
     * Débloque un administrateur par son ID.
     *
     * @param id l'ID de l'administrateur à débloquer
     * @return les détails de l'administrateur débloqué
     * @throws IllegalArgumentException si l'administrateur n'existe pas
     */
    public AdministrateurResponseDto unblock(Integer id) {
       // Vérifier si l'administrateur existe
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));

        // Débloquer l'administrateur en le marquant comme actif
        admin.setActif(true);
        // Enregistrer l'administrateur débloqué dans la base de données
        Administrateur unblockedAdmin = adminDao.save(admin);
        // Mapper l'administrateur débloqué vers le DTO de réponse
        return mapToResponseDto(unblockedAdmin);
    }

    /**
     * Récupère tous les administrateurs.
     *
     * @return une liste de DTO contenant les détails de tous les administrateurs
     */
    public List<AdministrateurResponseDto> getAll() {
       // Récupérer tous les administrateurs de la base de données
        return adminDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Mappe un administrateur vers un DTO de réponse.
     *
     * @param admin l'administrateur à mapper
     * @return le DTO de réponse correspondant
     */
    private AdministrateurResponseDto mapToResponseDto(Administrateur admin) {
        return new AdministrateurResponseDto(
                admin.getId(),
                admin.getEmail(),
                admin.isActif()
        );
    }
}
