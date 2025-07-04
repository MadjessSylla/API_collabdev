package odk.groupe4.ApiCollabDev.service;


import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContributionService {
    @Autowired
    private ContributionDao contributionDao;


    public List<Contribution> afficherContribution(){
        return contributionDao.findAll();
    }

    public Contribution ajouterContribution(ContributionDto contribution){

        Contribution contribution1 = new Contribution();
        //
        contribution1.setLienUrl(contribution.getLienUrl());
        contribution1.setFileUrl(contribution.getFileUrl());
        contribution1.setStatus(contribution.getStatus());
        contribution1.setDateCreation(contribution.getDateCreation());
        contribution1.setParticipant(contribution.getParticipant());
        contribution1.setGestionnaire(contribution.getGestionnaire());
        contribution1.setFonctionnalite(contribution.getFonctionnalite());
        //
        return contributionDao.save(contribution1);
    }

}
