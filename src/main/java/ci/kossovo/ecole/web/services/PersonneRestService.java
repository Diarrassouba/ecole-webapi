package ci.kossovo.ecole.web.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import ci.kossovo.ecole.entity.Personne;
import ci.kossovo.ecole.exceptions.InvalidPersonneException;
import ci.kossovo.ecole.web.models.Reponse;
import ci.kossovo.ecole.web.models.personne.ApplicationModelPersonne;
import ci.kossovo.ecole.web.models.personne.PostAjoutPersonne;
import ci.kossovo.ecole.web.models.personne.PostModifPersonne;
import ci.kossovo.ecole.web.utilitaires.Static;

@CrossOrigin(origins="http://localhost:4200")
@RestController
public class PersonneRestService {
	@Autowired
	private ApplicationModelPersonne modelPersonne;

	@Autowired
	private ObjectMapper jsonMapper;

	// Methode local
	private Reponse<Personne> getPersonne(Long id) {
		// On recupère la personne
		Personne pers = null;
		try {
			pers = modelPersonne.find(id);
		} catch (RuntimeException e) {
			return new Reponse<>(1, Static.getErreurforexception(e), null);
		}

		// personne existe?
		if (pers == null) {
			List<String> messages = new ArrayList<>();
			messages.add(String.format("La personne [%s] n'existe pas", id));
			return new Reponse<Personne>(2, messages, null);
		}
		// OK
		return new Reponse<Personne>(0, null, pers);
	}

	////////////////////////////////////////////////////////////////////////////////////////////

	// Créé une personne
	@PostMapping("/personnes")
	public String creer(@RequestBody PostAjoutPersonne p) throws JsonProcessingException {
		Reponse<Personne> reponse;
		Personne entity = new Personne(p.getTitre(), p.getNom(), p.getPrenom(), p.getNumCni());
		Adresse ad = new Adresse(p.getQuartier(), p.getCodePostal(), p.getEmail());

		entity.setAdresse(ad);

		try {
			reponse = new Reponse<Personne>(0, null, modelPersonne.creer(entity));
		} catch (InvalidPersonneException e) {
			reponse = new Reponse<Personne>(1, Static.getErreurforexception(e), null);
		}
		return jsonMapper.writeValueAsString(reponse);
	}

	// Modifier une personne
	@PutMapping("/personnes")
	public String modifier(@RequestBody PostModifPersonne p) throws JsonProcessingException {
		Reponse<Personne> reponse;
		Reponse<Personne> reponseModif = getPersonne(p.getId());
		if (reponseModif.getStatus() == 0) {
			Personne entity = reponseModif.getBody();
			entity.setTitre(p.getTitre());
			entity.setNom(p.getNom());
			entity.setPrenom(p.getPrenom());
			entity.setNumCni(p.getNumCni());
			Adresse ad = new Adresse(p.getQuartier(), p.getCodePostal(), p.getEmail());

			entity.setAdresse(ad);

			try {
				reponse = new Reponse<Personne>(0, null, modelPersonne.modifier(entity));
			} catch (InvalidPersonneException e) {
				reponse = new Reponse<Personne>(1, Static.getErreurforexception(e), null);
			}
		} else {
			reponse = new Reponse<Personne>(reponseModif.getStatus(), reponseModif.getMessages(),
					reponseModif.getBody());
		}

		return jsonMapper.writeValueAsString(reponse);
	}

	// Trouver une personne par son id
	@GetMapping("/personnes/{id}")
	public String find(@PathVariable("id") Long id) throws JsonProcessingException {
		Reponse<Personne> reponse;
		reponse = getPersonne(id);
		return jsonMapper.writeValueAsString(reponse);
	}

	// La liste des personnes.
	@GetMapping("/personnes")
	public String findAll() throws JsonProcessingException {
		Reponse<List<Personne>> reponse = null;
		// liste des personnes
		try {
			List<Personne> personnesTous = modelPersonne.personneAll("PE");

			if (!personnesTous.isEmpty()) {
				reponse = new Reponse<List<Personne>>(0, null, personnesTous);
			} else {
				List<String> messages = new ArrayList<>();
				messages.add("Pas de personnes enregistrées à ce jour");
				reponse = new Reponse<List<Personne>>(3, messages, null);
			}
		} catch (RuntimeException e) {
			reponse = new Reponse<List<Personne>>(1, Static.getErreurforexception(e), null);
		}
		return jsonMapper.writeValueAsString(reponse);
	}

	public void spprimer(List<Personne> entities) {
		modelPersonne.spprimer(entities);
	}

	@DeleteMapping("/personnes/{id}")
	public String supprimer(@PathVariable("id") Long id) throws JsonProcessingException {
		// La reponse
		Reponse<Boolean> reponse = null;
		boolean erreur = false;

		// On recupère la personne
		if (!erreur) {
			Reponse<Personne> reponsePers = getPersonne(id);
			if (reponsePers.getStatus() != 0) {
				reponse = new Reponse<>(reponsePers.getStatus(), reponsePers.getMessages(), false);
				erreur = true;
			}
		}
		if (!erreur) {
			// Suppression
			try {
				reponse = new Reponse<>(0, null, modelPersonne.supprimer(id));
			} catch (RuntimeException e) {
				reponse = new Reponse<>(3, Static.getErreurforexception(e), false);
			}

		}
		// La reponse
		return jsonMapper.writeValueAsString(reponse);
	}

	public boolean existe(Long id) {
		return modelPersonne.existe(id);
	}

	public Long compter() {
		return modelPersonne.compter();
	}

	public Personne chercherParMatricule(String matricule) {
		return modelPersonne.chercherParMatricule(matricule);
	}

	public Personne chercherParIdentifiantS(String numCni) {
		return modelPersonne.chercherParIdentifiantS(numCni);
	}

	public List<Personne> chercherUserParMc(String mc) {
		return modelPersonne.chercherUserParMc(mc);
	}

	public List<Personne> userAll() {
		return modelPersonne.userAll();
	}

}
