package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.UtilisateurDao;
import odk.groupe4.ApiCollabDev.dto.AdministrateurDto;
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

    public AdministrateurResponseDto create(AdministrateurDto dto) {
        // Vérifier l'unicité de l'email
        Optional<Utilisateur> existingUser = utilisateurDao.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Cet email est déjà utilisé.");
        }

        Administrateur admin = new Administrateur();
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        admin.setActif(true);
        
        Administrateur savedAdmin = adminDao.save(admin);
        return mapToResponseDto(savedAdmin);
    }

    public AdministrateurResponseDto getById(Integer id) {
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));
        return mapToResponseDto(admin);
    }

    public AdministrateurResponseDto update(Integer id, AdministrateurDto dto) {
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));
        
        admin.setEmail(dto.getEmail());
        admin.setPassword(dto.getPassword());
        
        Administrateur updatedAdmin = adminDao.save(admin);
        return mapToResponseDto(updatedAdmin);
    }

    public void delete(Integer id) {
        if (!adminDao.existsById(id)){
            throw new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas.");
        }
        adminDao.deleteById(id);
    }

    public AdministrateurResponseDto block(Integer id) {
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));
        
        admin.setActif(false);
        Administrateur blockedAdmin = adminDao.save(admin);
        return mapToResponseDto(blockedAdmin);
    }

    public AdministrateurResponseDto unblock(Integer id) {
        Administrateur admin = adminDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrateur avec l'ID " + id + " n'existe pas."));
        
        admin.setActif(true);
        Administrateur unblockedAdmin = adminDao.save(admin);
        return mapToResponseDto(unblockedAdmin);
    }

    public List<AdministrateurResponseDto> getAll() {
        return adminDao.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private AdministrateurResponseDto mapToResponseDto(Administrateur admin) {
        return new AdministrateurResponseDto(
                admin.getId(),
                admin.getEmail(),
                admin.isActif()
        );
    }
}
