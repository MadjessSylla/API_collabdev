package odk.groupe4.ApiCollabDev.service;

import odk.groupe4.ApiCollabDev.dao.AdministrateurDao;
import odk.groupe4.ApiCollabDev.dao.TemplateProjetDao;
import odk.groupe4.ApiCollabDev.dto.TemplateProjetDto;
import odk.groupe4.ApiCollabDev.dto.TemplateProjetResponseDto;
import odk.groupe4.ApiCollabDev.models.Administrateur;
import odk.groupe4.ApiCollabDev.models.TemplateProjet;
import odk.groupe4.ApiCollabDev.models.enums.ProjectDomain;
import odk.groupe4.ApiCollabDev.models.enums.ProjectSector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemplateProjetService {
    private final TemplateProjetDao templateDao;
    private final AdministrateurDao administrateurDao;

    @Autowired
    public TemplateProjetService(TemplateProjetDao templateDao, AdministrateurDao administrateurDao) {
        this.templateDao = templateDao;
        this.administrateurDao = administrateurDao;
    }

    public TemplateProjetResponseDto creerTemplate(TemplateProjetDto dto, int idAdmin) {
        Administrateur admin = administrateurDao.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'ID: " + idAdmin));

        TemplateProjet template = new TemplateProjet();
        template.setNom(dto.getNom());
        template.setDescription(dto.getDescription());
        template.setDomaine(dto.getDomaine());
        template.setSecteur(dto.getSecteur());
        template.setNiveauRecommande(dto.getNiveauRecommande());
        template.setStructureProjet(dto.getStructureProjet());
        template.setDateCreation(LocalDate.now());
        template.setActif(true);
        template.setCreateur(admin);

        TemplateProjet savedTemplate = templateDao.save(template);
        return mapToResponseDto(savedTemplate);
    }

    public List<TemplateProjetResponseDto> getAllTemplatesActifs() {
        return templateDao.findByActifTrue().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<TemplateProjetResponseDto> getTemplatesByDomaine(ProjectDomain domaine) {
        return templateDao.findByDomaineAndActifTrue(domaine).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<TemplateProjetResponseDto> getTemplatesBySecteur(ProjectSector secteur) {
        return templateDao.findBySecteurAndActifTrue(secteur).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public TemplateProjetResponseDto getTemplateById(int id) {
        TemplateProjet template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé avec l'ID: " + id));
        return mapToResponseDto(template);
    }

    public TemplateProjetResponseDto updateTemplate(int id, TemplateProjetDto dto) {
        TemplateProjet template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé avec l'ID: " + id));

        template.setNom(dto.getNom());
        template.setDescription(dto.getDescription());
        template.setDomaine(dto.getDomaine());
        template.setSecteur(dto.getSecteur());
        template.setNiveauRecommande(dto.getNiveauRecommande());
        template.setStructureProjet(dto.getStructureProjet());

        TemplateProjet updatedTemplate = templateDao.save(template);
        return mapToResponseDto(updatedTemplate);
    }

    public void desactiverTemplate(int id) {
        TemplateProjet template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé avec l'ID: " + id));

        template.setActif(false);
        templateDao.save(template);
    }

    public void activerTemplate(int id) {
        TemplateProjet template = templateDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Template non trouvé avec l'ID: " + id));

        template.setActif(true);
        templateDao.save(template);
    }

    private TemplateProjetResponseDto mapToResponseDto(TemplateProjet template) {
        return new TemplateProjetResponseDto(
                template.getId(),
                template.getNom(),
                template.getDescription(),
                template.getDomaine(),
                template.getSecteur(),
                template.getNiveauRecommande(),
                template.getStructureProjet(),
                template.getDateCreation(),
                template.isActif(),
                template.getCreateur().getEmail(),
                template.getQuestionnaires().size()
        );
    }
}
