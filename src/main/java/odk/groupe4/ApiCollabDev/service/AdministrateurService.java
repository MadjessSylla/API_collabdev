package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.ContributionDao;
import odk.groupe4.ApiCollabDev.dto.AdministrateurDto;
import odk.groupe4.ApiCollabDev.dto.ContributionDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.Contribution;
import odk.groupe4.ApiCollabDev.models.enums.StatusContribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdministrateurService {
    @Autowired
    private ContributionDao contributionDao;
    private ContributionDto ContributionDaoToContributionDto(Contribution contribution) {
        ContributionDto contributionDto = new ContributionDto();
        contributionDto.setLienUrl(contribution.getLienUrl());
        contributionDto.setFileUrl(contribution.getFileUrl());
        contributionDto.setStatus(contribution.getStatus());
        contributionDto.setDateCreation(contribution.getDateCreation());
        contributionDto.setParticipant(contribution.getParticipant());
        contributionDto.setGestionnaire(contribution.getGestionnaire());
        contributionDto.setFonctionnalite(contribution.getFonctionnalite());
        return contributionDto;
    }
    // Méthode AdministrateurServiceToDto
    private AdministrateurDto AministrateurServiceToDto(Administrateur administrateur) {
        AdministrateurDto administrateurDto = new AdministrateurDto();
        administrateurDto.setEmail(administrateur.getEmail());
        administrateurDto.setPassword(administrateur.getPassword());
        return administrateurDto;
    }
    // Méthode pour valider ou refuser une contribution
    public ContributionDto validerOuRefuserContribution(int idContribution, StatusContribution status) {
        Optional<Contribution> optionalContribution = contributionDao.findById(idContribution);
        if (optionalContribution.isPresent()) {
            Contribution contribution = optionalContribution.get();
            contribution.setStatus(status);
            Contribution updatedContribution = contributionDao.save(contribution);
            return ContributionDaoToContributionDto(updatedContribution);
        } else {
            return null; // ou lancer une exception si la contribution n'existe pas
        }
    }

}
