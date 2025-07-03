package odk.groupe4.ApiCollabDev.service;


import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.Contribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContributionService {
    @Autowired
    private ContributionDao contributionDao;

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
