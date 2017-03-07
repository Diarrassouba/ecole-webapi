package ci.kossovo.ecole.web.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ci.kossovo.ecole.entity.Adresse;
import ci.kossovo.ecole.entity.Etudiant;
import ci.kossovo.ecole.entity.Personne;
import ci.kossovo.ecole.exceptions.InvalidPersonneException;
import ci.kossovo.ecole.web.models.Reponse;
import ci.kossovo.ecole.web.models.personne.ApplicationModelPersonne;
import ci.kossovo.ecole.web.models.personne.PostAjoutEtudiant;
import ci.kossovo.ecole.web.models.personne.PostModifEtudiant;
import ci.kossovo.ecole.web.utilitaires.Static;

@RestController
public class EtudiantRestService {
	@Autowired
	private ApplicationModelPersonne modelPersonne;

	@Autowired
	private ObjectMapper jsonMapper;

	// Methode local
	private Reponse<Etudiant> getEtudiant(Long id) {
		// On recupère létudiant
		Etudiant etud = null;

		try {
			etud = (Etudiant) modelPersonne.find(id);
		} catch (RuntimeException e) {
			return new Reponse<>(1, Static.getErreurforexception(e), null);
		}

		// etudiant existe?
		if (etud == null) {
			List<String> messages = new ArrayList<>();
			messages.add(String.format("L'étudiant [%s] n'existe pas", id));
			return new Reponse<Etudiant>(2, messages, null);
		}
		// OK
		return new Reponse<Etudiant>(0, null, etud);
	}

	////////////////////////////////////////////////////////////////////////////////////////////

	// crée un etudiant
	@PostMapping("/etudiants")
	public String creer(@RequestBody PostAjoutEtudiant pe) throws JsonProcessingException {
		Reponse<Etudiant> reponse = null;
		boolean erreur = false;

		// Affecter les champs de l'entité etudiant
		Adresse ad = new Adresse(pe.getQuartier(), pe.getCodePostal(), pe.getEmail());
		Etudiant entity = new Etudiant(pe.getTitre(), pe.getNom(), pe.getPrenom(), pe.getNumCni(), pe.getMatricule());
		entity.setAdresse(ad);
		entity.setPhoto(pe.getPhoto());
		entity.setStatus(pe.getStatus());

		// formater la date qui est en String => html
		String nais = pe.getDateNaissance();
		Date naissance = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);

		try {
			naissance = sdf.parse(nais);
		} catch (ParseException e1) {
			List<String> messages = new ArrayList<String>();
			messages.add(String.format("La date [%s] est invalide", nais));
			reponse = new Reponse<>(8, messages, null);
			erreur = true;
		}

		// ajouter l'etudiant
		if (!erreur) {
			entity.setDateNaissance(naissance);

			try {
				reponse = new Reponse<Etudiant>(0, null, (Etudiant) modelPersonne.creer(entity));
			} catch (InvalidPersonneException e) {
				reponse = new Reponse<Etudiant>(1, Static.getErreurforexception(e), null);
			}

		}

		return jsonMapper.writeValueAsString(reponse);
	}

	////////////////////////////////

	// modifier un etudiant
	@PutMapping("/etudiants")
	public String modifier(@RequestBody PostModifEtudiant post) throws JsonProcessingException {
		Reponse<Etudiant> reponse = null;
		boolean erreur = false;
		
		Reponse<Etudiant> reponseModif = getEtudiant(post.getId());
		// etudiant existe?
		if (reponseModif.getStatus() == 0) {
			//recuperer l'etudiant
			Etudiant et = reponseModif.getBody();
			
			//affecter les modifications
			et.setTitre(post.getTitre());
			et.setNom(post.getNom());
			et.setPrenom(post.getPrenom());
			et.setNumCni(post.getNumCni());
			Adresse ad = new Adresse(post.getQuartier(), post.getCodePostal(), post.getEmail());
			et.setAdresse(ad);
			et.setPhoto(post.getPhoto());
			et.setMatricule(post.getMatricule());

			// Formater la date string en type Date
			String nais = post.getDateNaissance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			Date naissance = null;
			try {
				naissance = sdf.parse(nais);
				et.setDateNaissance(naissance);
			} catch (ParseException e) {
				// erreur de formatage de date
				List<String> messages = new ArrayList<>();
				messages.add(String.format("La date [%] est invalide", nais));
				reponse = new Reponse<Etudiant>(8, messages, null);
				erreur = true;
			}
			
			// date bien formater
			if (!erreur) {
				try {
					// modifier l'etudiant
					Etudiant entity = (Etudiant) modelPersonne.modifier(et);
					reponse = new Reponse<Etudiant>(0, null, entity);
				} catch (InvalidPersonneException e) {
					// problème de modification
					reponse = new Reponse<Etudiant>(0, Static.getErreurforexception(e), null);
				}

			}

		} else {
			// etudiant n'existe pas
			reponse = new Reponse<Etudiant>(reponseModif.getStatus(), reponseModif.getMessages(),
					reponseModif.getBody());
		}

		return jsonMapper.writeValueAsString(reponse);
	}

	////////////////////////////
	// liste des etudiants
	@GetMapping("/etudiants")
	public String listEtudiants() throws JsonProcessingException {
		Reponse<List<Etudiant>> reponse = null;
		try {
			List<Etudiant> etudiants = modelPersonne.listEtudiants();
			// List<Personne> etudiants=modelPersonne.personneAll("ET");

			if (!etudiants.isEmpty()) {
				reponse = new Reponse<List<Etudiant>>(0, null, etudiants);
			} else {
				List<String> messages = new ArrayList<>();
				messages.add("Pas d'étudiants enregistrées à ce jour");
				reponse = new Reponse<List<Etudiant>>(3, messages, null);
			}
		} catch (Exception e) {
			reponse = new Reponse<List<Etudiant>>(1, Static.getErreurforexception(e), null);
		}
		return jsonMapper.writeValueAsString(reponse);
	}

	//////////////////////////////
	// chercher un etudiant par identifiant
	@GetMapping("/etudiants/{id}")
	public String find(@PathVariable("id") Long id) throws JsonProcessingException {
		Reponse<Etudiant> reponse = null;
		reponse = getEtudiant(id);
		return jsonMapper.writeValueAsString(reponse);
	}

	//////////////////////////////
	// supprimer des etudiant
	public void spprimer(List<Personne> entities) {
		modelPersonne.spprimer(entities);
	}

	//////////////////////////////
	// supprimer un etudiant par identifiant
	
	@DeleteMapping("/etudiants/{id}")
	public String supprimer(@PathVariable("id") Long id) throws JsonProcessingException {
		Reponse<Boolean> reponse=null;
		boolean erreur=false;
		//on recupère l'étudiant
		if (!erreur) {
			Reponse<Etudiant> etudiantReponse=getEtudiant(id);
			if (etudiantReponse.getStatus()!=0) {
				reponse=new Reponse<Boolean>(etudiantReponse.getStatus(), etudiantReponse.getMessages(), false);
				erreur=true;
			}
		}
		// suppression
		if (!erreur) {
			try {
				reponse= new Reponse<Boolean>(0, null, modelPersonne.supprimer(id));
			} catch (Exception e) {
				reponse= new Reponse<Boolean>(3, Static.getErreurforexception(e), false);
			}
		}
		return jsonMapper.writeValueAsString(reponse);
	}

	///////////////////////////
	public boolean existe(Long id) {
		return modelPersonne.existe(id);
	}

	//////////////////////////
	public Long compter() {
		return modelPersonne.compter();
	}

	//////////////////////////
	public Personne chercherParMatricule(String matricule) {
		return modelPersonne.chercherParMatricule(matricule);
	}

	//////////////////////////
	public Personne chercherParIdentifiantS(String numCni) {
		return modelPersonne.chercherParIdentifiantS(numCni);
	}

	//////////////////////////
	public List<Personne> chercherEtudiantParMc(String mc) {
		return modelPersonne.chercherEtudiantParMc(mc);
	}

	//////////////////////////
	public List<Personne> listAdministrateurs() {
		return modelPersonne.listAdministrateurs();
	}

}
