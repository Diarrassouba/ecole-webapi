package ci.kossovo.ecole.web.models.personne;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ci.kossovo.ecole.entity.Enseigne;
import ci.kossovo.ecole.entity.Etudiant;
import ci.kossovo.ecole.entity.Personne;
import ci.kossovo.ecole.exceptions.InvalidPersonneException;
import ci.kossovo.ecole.metier.IPersonneMetier;

@Component
public class ApplicationModelPersonne implements IPersonneMetier {
	@Autowired
	private IPersonneMetier personneMetier;

	@Override
	public Personne creer(Personne entity) throws InvalidPersonneException {
		return personneMetier.creer(entity);
	}

	@Override
	public Personne modifier(Personne entity) throws InvalidPersonneException {
		return personneMetier.modifier(entity);
	}

	@Override
	public Personne find(Long id) {
		return personneMetier.find(id);
	}

	@Override
	public List<Personne> findAll() {
		return personneMetier.findAll();
	}

	@Override
	public void spprimer(List<Personne> entities) {
		personneMetier.spprimer(entities);

	}

	@Override
	public boolean supprimer(Long id) {
		return personneMetier.supprimer(id);
	}

	@Override
	public boolean existe(Long id) {
		return personneMetier.existe(id);
	}

	@Override
	public Long compter() {
		return personneMetier.compter();
	}

	@Override
	public Personne chercherParMatricule(String matricule) {
		return personneMetier.chercherParMatricule(matricule);
	}

	@Override
	public Personne chercherParIdentifiantS(String numCni) {
		return personneMetier.chercherParIdentifiantS(numCni);
	}

	@Override
	public List<Personne> chercherParStatus(String status) {
		return personneMetier.chercherParStatus(status);
	}

	@Override
	public List<Personne> chercherParFonction(String fonction) {
		return personneMetier.chercherParFonction(fonction);
	}

	@Override
	public List<Personne> chercherEtudiantParMc(String mc) {
		return personneMetier.chercherEtudiantParMc(mc);
	}

	@Override
	public List<Personne> chercherEnseignantParMc(String mc) {
		return personneMetier.chercherEnseignantParMc(mc);
	}

	@Override
	public List<Personne> chercherAdministrateurParMc(String mc) {
		return personneMetier.chercherAdministrateurParMc(mc);
	}

	@Override
	public List<Personne> chercherUserParMc(String mc) {
		return personneMetier.chercherUserParMc(mc);
	}

	@Override
	public List<Etudiant> listEtudiants() {
		return personneMetier.listEtudiants();
	}

	@Override
	public List<Personne> listEnseignants() {
		return personneMetier.listEnseignants();
	}

	@Override
	public List<Personne> listAdministrateurs() {
		return personneMetier.listAdministrateurs();
	}

	@Override
	public List<Personne> userAll() {
		return personneMetier.userAll();
	}

	@Override
	public Enseigne affecterPromgMat(Long idEnseignant, Long idPromo, Long idMatiere) {
		return personneMetier.affecterPromgMat(idEnseignant, idPromo, idMatiere);
	}

	@Override
	public List<Personne> personneAll() {
		return personneMetier.personneAll();
	}

	@Override
	public List<Personne> personneAll(String type) {
		return personneMetier.personneAll(type);
	}

}
