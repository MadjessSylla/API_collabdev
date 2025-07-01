package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributeurDao;
import odk.groupe4.ApiCollabDev.dto.ContributeurDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContributeurSercice {
    @Autowired
    private ContributeurDao contributeurDao;

    public void ajouterContributeur(ContributeurDto c) {

        // Instanciation d'un objet Contributeur
        // Conversion de l'objet ContributeurDAO en Contributeur
        // Enregistrement de l'objet Contributeur
    }
}
