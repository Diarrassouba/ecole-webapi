package ci.kossovo.ecole.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ci.kossovo.ecole.entity.Enseigne;
import ci.kossovo.ecole.entity.Personne;
import ci.kossovo.ecole.exceptions.InvalidPersonneException;
import ci.kossovo.ecole.web.models.personne.ApplicationModelPersonne;

@RestController
public class EnseignantRestService {
	@Autowired
	private ApplicationModelPersonne modelPersonne;

	@Autowired
	private ObjectMapper jsonMapper;

	public Personne creer(Personne entity) throws InvalidPersonneException {
		return modelPersonne.creer(entity);
	}

	public Personne modifier(Personne entity) throws InvalidPersonneException {
		return modelPersonne.modifier(entity);
	}

	public Personne find(Long id) {
		return modelPersonne.find(id);
	}

	public void spprimer(List<Personne> entities) {
		modelPersonne.spprimer(entities);
	}

	public boolean supprimer(Long id) {
		return modelPersonne.supprimer(id);
	}

	public boolean existe(Long id) {
		return modelPersonne.existe(id);
	}

	public Long compter() {
		return modelPersonne.compter();
	}

	public Personne chercherParIdentifiantS(String numCni) {
		return modelPersonne.chercherParIdentifiantS(numCni);
	}

	public List<Personne> chercherParStatus(String status) {
		return modelPersonne.chercherParStatus(status);
	}

	public List<Personne> chercherEnseignantParMc(String mc) {
		return modelPersonne.chercherEnseignantParMc(mc);
	}

	public List<Personne> listEnseignants() {
		return modelPersonne.listEnseignants();
	}

	public Enseigne affecterPromgMat(Long idEnseignant, Long idPromo, Long idMatiere) {
		return modelPersonne.affecterPromgMat(idEnseignant, idPromo, idMatiere);
	}

}
