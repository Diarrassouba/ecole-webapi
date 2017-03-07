package ci.kossovo.ecole.web;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ci.kossovo.ecole.entity.Adresse;
import ci.kossovo.ecole.entity.Etudiant;
import ci.kossovo.ecole.web.models.personne.ApplicationModelPersonne;
import ci.kossovo.ecole.web.models.personne.PostAjoutEtudiant;
import ci.kossovo.ecole.web.models.personne.PostModifEtudiant;
import ci.kossovo.ecole.web.services.EtudiantRestService;

@RunWith(SpringRunner.class)
@WebMvcTest(EtudiantRestService.class)
public class EtudiantRestServiceTest {
	@Autowired
	private MockMvc mvc;

	@MockBean
	private ApplicationModelPersonne personneModel;

	private ObjectMapper mapper = new ObjectMapper();

	// teste findAll()
	@Test
	public void trouverToutesEtudiants() throws Exception {

		// Donnée
		Etudiant e1 = new Etudiant("Mr", "Diarra", "Drissa", "CNI01", "MA21");
		e1.setType("ET");
		e1.setId(1L);
		
		Etudiant e2 = new Etudiant("Mr", "Traoré", "Abdoulaye", "CNI02");
		e2.setType("PE");
		e2.setId(2L);

		List<Etudiant> etudiants = Arrays.asList(e1, e2);

		// when
		given(this.personneModel.listEtudiants()).willReturn(etudiants);

		// then
		this.mvc.perform(get("/etudiants")).andExpect(status().isOk()).andExpect(jsonPath("$.body.length()", is(2)))
				.andExpect(jsonPath("$.body.[0].matricule", is("MA21")));

	}

	//////////////////////////////////////////////////////
	// teste enregistrement personne
	@Test
	public void CreerUneEtudiant() throws Exception {

		PostAjoutEtudiant ets = new PostAjoutEtudiant();
		ets.setNom("Diarra");
		ets.setPrenom("Drissa");
		ets.setTitre("Mr");
		ets.setNumCni("CNI01");
		ets.setCodePostal("bb");
		ets.setEmail("ed");
		ets.setQuartier("yo");
		ets.setMatricule("MA21");
		ets.setDateNaissance("2000-03-21");
		ets.setStatus("affecter");

		Etudiant e = new Etudiant(ets.getTitre(), ets.getNom(), ets.getPrenom(), ets.getNumCni(), ets.getMatricule());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		e.setDateNaissance(sdf.parse(ets.getDateNaissance()));
		e.setStatus(ets.getStatus());
		Adresse ad = new Adresse(ets.getQuartier(), ets.getCodePostal(), ets.getEmail());
		e.setAdresse(ad);
		e.setId(1L);

		given(this.personneModel.creer(any(Etudiant.class))).willReturn(e);

		this.mvc.perform(post("/etudiants").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(ets)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is(0)))
				.andExpect(jsonPath("$.body.id", is(1))).andExpect(jsonPath("$.body.nom", is("Diarra")));
	}

	/////////////////////////////////////////////////
	// teste un modification de etudiant
	@Test
	public void ModifierUneEtudiant() throws Exception {

		PostModifEtudiant etudsModif = new PostModifEtudiant();
		etudsModif.setNom("Diarrassouba");
		etudsModif.setPrenom("Drissa");
		etudsModif.setTitre("Mr");
		etudsModif.setNumCni("CNI01");
		etudsModif.setCodePostal("bb");
		etudsModif.setEmail("ed");
		etudsModif.setQuartier("yo");
		etudsModif.setId(2L);
		etudsModif.setMatricule("MA21");
		etudsModif.setDateNaissance("2000-03-21");
		etudsModif.setStatus("affecter");

		Etudiant etudexist = new Etudiant(etudsModif.getTitre(), "Diarra", etudsModif.getPrenom(),
				etudsModif.getNumCni(), "MA22");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		etudexist.setDateNaissance(sdf.parse(etudsModif.getDateNaissance()));
		etudexist.setStatus(etudsModif.getStatus());
		Adresse ad = new Adresse(etudsModif.getQuartier(), etudsModif.getCodePostal(), etudsModif.getEmail());
		etudexist.setAdresse(ad);
		etudexist.setId(2L);

		Etudiant etrep = new Etudiant(etudsModif.getTitre(), "Diarrassouba", etudsModif.getPrenom(),
				etudsModif.getNumCni(), "MA36");
		Adresse adr = new Adresse(etudsModif.getQuartier(), etudsModif.getCodePostal(), etudsModif.getEmail());
		etrep.setAdresse(adr);
		etrep.setId(2L);
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);
		etrep.setDateNaissance(sdf1.parse(etudsModif.getDateNaissance()));
		etrep.setStatus(etudsModif.getStatus());

		given(this.personneModel.find(etudsModif.getId())).willReturn(etudexist);

		given(this.personneModel.modifier(any(Etudiant.class))).willReturn(etrep);

		this.mvc.perform(put("/etudiants").contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8).content(mapper.writeValueAsString(etudsModif)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.status", is(0)))
				.andExpect(jsonPath("$.body.id", is(2))).andExpect(jsonPath("$.body.nom", is("Diarrassouba")))
				.andExpect(jsonPath("$.body.matricule", is("MA36")));
		
		verify(this.personneModel, timeout(1)).find(etudexist.getId());
		verify(this.personneModel, timeout(1)).modifier(etrep);
	}

	//////////////////////////////////////////////
	// teste chercher une etudiants
	@Test
	public void trouverUnEtudiant() throws Exception {

		// Donnée
		Etudiant e1 = new Etudiant("Mr", "Diarra", "Drissa", "CNI01");
		e1.setType("ET");
		e1.setId(1L);

		// when
		given(this.personneModel.find(e1.getId())).willReturn(e1);

		// then
		this.mvc.perform(get("/etudiants/{id}", e1.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(0))).andExpect(jsonPath("$.body.id", is(1)))
				.andExpect(jsonPath("$.body.nom", is("Diarra")));
		
		verify(this.personneModel, timeout(1)).find(e1.getId());

	}

	//////////////////////////////////////////////
	// teste supprimer une etudiant
	@Test
	public void supprimerUneEtudiant() throws Exception {

		// Donnée
		Etudiant e1 = new Etudiant("Mr", "Diarra", "Drissa", "CNI01", "MA33");
		e1.setType("ET");
		e1.setId(1L);
		e1.setDateNaissance(null);
		e1.setStatus("A");

		// when
		given(this.personneModel.find(e1.getId())).willReturn(e1);

		given(this.personneModel.supprimer(e1.getId())).willReturn(true);

		// then
		this.mvc.perform(delete("/etudiants/{id}", e1.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is(0))).andExpect(jsonPath("$.body", is(true)));
		verify(this.personneModel, timeout(1)).find(e1.getId());
		verify(this.personneModel, timeout(1)).supprimer(e1.getId());

	}

}
