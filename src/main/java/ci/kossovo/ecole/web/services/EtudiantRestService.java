package ci.kossovo.ecole.web.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	public Personne modifier(Personne entity) throws InvalidPersonneException {
		return modelPersonne.modifier(entity);
	}
	
	////////////////////////////
	//liste des etudiants
	@GetMapping("/etudiants")
	public String listEtudiants() throws JsonProcessingException {
		Reponse<List<Etudiant>> reponse= null;
		try {
			List<Etudiant> etudiants=modelPersonne.listEtudiants();
			
			//autre façon
			/*List<Personne> personnes= modelPersonne.findAll();
			List<Personne>Etudiants=personnes.stream().filter(
					p -> p.getType().equals("ET")).collect(Collectors.toList());*/
			
			if (!etudiants.isEmpty()) {
				reponse= new Reponse<List<Etudiant>>(0, null, etudiants);
			}
		} catch (Exception e) {
			List<String> messages = new ArrayList<>();
			messages.add("Pas d'étudiants enregistrées à ce jour");
			reponse = new Reponse<List<Etudiant>>(3, messages, null);
		}
		return jsonMapper.writeValueAsString(reponse) ;
	}

	//////////////////////////////
	// chercher un etudiant par identifiant
	public Personne find(Long id) {
		return modelPersonne.find(id);
	}

	//////////////////////////////
	// supprimer des etudiant
	public void spprimer(List<Personne> entities) {
		modelPersonne.spprimer(entities);
	}

	//////////////////////////////
	// supprimer un etudiant par identifiant
	public boolean supprimer(Long id) {
		return modelPersonne.supprimer(id);
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
