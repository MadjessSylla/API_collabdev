package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.FonctionnaliteDao;
import odk.groupe4.ApiCollabDev.dao.ProjetDao;
import odk.groupe4.ApiCollabDev.dto.FonctionnaliteNewDto;
import odk.groupe4.ApiCollabDev.models.Fonctionnalite;
import odk.groupe4.ApiCollabDev.models.Projet;
import odk.groupe4.ApiCollabDev.models.enums.FeaturesStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FonctionnaliteService {
    private final FonctionnaliteDao fonctionnaliteDao;
    private final ProjetDao projetDao;

    @Autowired
    public FonctionnaliteService(FonctionnaliteDao fonctionnaliteDao, ProjetDao projetDao) {
        this.fonctionnaliteDao = fonctionnaliteDao;
        this.projetDao = projetDao;
    }

    /**
     * Affiche toutes les fonctionnalités.
     *
     * @return Une liste de toutes les fonctionnalités.
     */
    public List<Fonctionnalite> afficherFonctionnalite() {
        return fonctionnaliteDao.findAll();
    }

    /**
     * Ajoute une nouvelle fonctionnalité à un projet existant.
     *
     * @param idProjet L'ID du projet auquel la fonctionnalité sera ajoutée.
     * @param dto      Le DTO contenant les détails de la nouvelle fonctionnalité.
     * @return La fonctionnalité créée.
     */
    public Fonctionnalite ajouterFonctionnalite(int idProjet, FonctionnaliteNewDto dto){
        // Vérification de l'existence du projet
        Projet projet = projetDao.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé avec l'ID: " + idProjet));

        // Création de la fonctionnalité à partir du DTO
        Fonctionnalite fonctionnalite = new Fonctionnalite();
        // Remplissage des champs de la fonctionnalité
        fonctionnalite.setTitre(dto.getTitre()); // Titre de la fonctionnalité
        fonctionnalite.setContenu(dto.getContenu()); // Contenu de la fonctionnalité
        fonctionnalite.setStatusFeatures(FeaturesStatus.A_FAIRE); // Statut de la fonctionnalité
        fonctionnalite.setProjet(projet); // Associer le projet à la fonctionnalité
        return fonctionnaliteDao.save(fonctionnalite);
    }
}
