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
import ci.kossovo.ecole.entity.Personne;
import ci.kossovo.ecole.web.models.personne.ApplicationModelPersonne;
import ci.kossovo.ecole.web.models.personne.PostAjoutPersonne;
import ci.kossovo.ecole.web.models.personne.PostModifPersonne;
import ci.kossovo.ecole.web.services.PersonneRestService;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonneRestService.class)
public class PersonneRestServiceTest {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ApplicationModelPersonne personneModel;

	private ObjectMapper mapper= new ObjectMapper();
	

	// teste findAll()
	@Test
	public void trouverToutesPersonnes() throws Exception {
	
	//Donnée	
		Personne p1=new Personne(1l,"Mr", "Diarra", "Drissa","CNI01");
		p1.setType("PE");
		Personne p2=new Personne(2l,"Mr", "Traoré", "Abdoulaye","CNI02");
		p2.setType("PE");
		List<Personne> personnes= Arrays.asList(p1,p2);
		
		//when
		given(this.personneModel.personneAll("PE"))
		.willReturn(personnes);
		
		//then
		this.mvc.perform(get("/personnes"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.body.length()", is(2)))
		.andExpect(jsonPath("$.body.[0].numCni", is("CNI01")));
		
	}
	
	
	//////////////////////////////////////////////////////
	//teste enregistrement personne
	@Test
	public void CreerUnePersonne() throws Exception {

		PostAjoutPersonne pos= new PostAjoutPersonne();
		pos.setNom("Diarra");
		pos.setPrenom("Drissa");
		pos.setTitre("Mr");
		pos.setNumCni("CNI01");
		pos.setCodePostal("bb");
		pos.setEmail("ed");
		pos.setQuartier("yo");
		
		Personne p =new Personne(pos.getTitre(), pos.getNom(), pos.getPrenom(),pos.getNumCni());
		Adresse ad= new Adresse(pos.getQuartier(), pos.getCodePostal(), pos.getEmail());
		p.setAdresse(ad);
		p.setId(1L);
		
		given(this.personneModel.creer(any(Personne.class))).willReturn(p);
		
		this.mvc.perform(post("/personnes")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
		.content(mapper.writeValueAsString(pos))
		)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is(0)))
		.andExpect(jsonPath("$.body.id", is(1)))
		.andExpect(jsonPath("$.body.nom", is("Diarra")));
	}
	
	/////////////////////////////////////////////////
	//teste un modification de personne
	@Test
	public void ModifierUnePersonne() throws Exception {

		PostModifPersonne posModif= new PostModifPersonne();
		posModif.setNom("Diarrassouba");
		posModif.setPrenom("Drissa");
		posModif.setTitre("Mr");
		posModif.setNumCni("CNI01");
		posModif.setCodePostal("bb");
		posModif.setEmail("ed");
		posModif.setQuartier("yo");
		posModif.setId(2L);
		
		Personne pexist =new Personne(posModif.getTitre(), "Diarra", posModif.getPrenom(),posModif.getNumCni());
		Adresse ad= new Adresse(posModif.getQuartier(), posModif.getCodePostal(), posModif.getEmail());
		pexist.setAdresse(ad);
		pexist.setId(2L);
		
		Personne prep =new Personne(posModif.getTitre(), "Diarrassouba", posModif.getPrenom(),posModif.getNumCni());
		Adresse adr= new Adresse(posModif.getQuartier(), posModif.getCodePostal(), posModif.getEmail());
		prep.setAdresse(adr);
		prep.setId(2L);
		
		given(this.personneModel.find(posModif.getId())).willReturn(pexist);
		
		given(this.personneModel.modifier(any(Personne.class))).willReturn(prep);
		
		
		
		this.mvc.perform(put("/personnes")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
		.content(mapper.writeValueAsString(posModif))
		)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is(0)))
		.andExpect(jsonPath("$.body.id", is(2)))
		.andExpect(jsonPath("$.body.nom", is("Diarrassouba")));
		verify(this.personneModel, timeout(1)).find(pexist.getId());
		verify(this.personneModel, timeout(1)).modifier(prep);
	}
	
	//////////////////////////////////////////////
	// teste chercher une personne
	@Test
	public void trouverUnePersonne() throws Exception {
	
	//Donnée	
		Personne p1=new Personne("Mr", "Diarra", "Drissa","CNI01");
		p1.setType("PE");
		p1.setId(1L);
		
		
		//when
		given(this.personneModel.find(p1.getId()))
		.willReturn(p1);
		
		//then
		this.mvc.perform(get("/personnes/{id}", p1.getId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.status", is(0)))
		.andExpect(jsonPath("$.body.id", is(1)))
		.andExpect(jsonPath("$.body.nom", is("Diarra")));
		verify(this.personneModel, timeout(1)).find(p1.getId());
		
		
	}
	
//////////////////////////////////////////////
// teste supprimer une personne
@Test
public void supprimerUnePersonne() throws Exception {

//Donnée	
Personne p1=new Personne("Mr", "Diarra", "Drissa","CNI01");
p1.setType("PE");
p1.setId(1L);


//when
given(this.personneModel.find(p1.getId()))
.willReturn(p1);
given(this.personneModel.supprimer(p1.getId())).willReturn(true);

//then
this.mvc.perform(delete("/personnes/{id}", p1.getId()))
.andExpect(status().isOk())
.andExpect(jsonPath("$.status", is(0)))
.andExpect(jsonPath("$.body", is(true)));
verify(this.personneModel, timeout(1)).find(p1.getId());
verify(this.personneModel, timeout(1)).supprimer(p1.getId());

}

}
