package ci.kossovo.ecole.web;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ci.kossovo.ecole.entity.Adresse;
import ci.kossovo.ecole.entity.Personne;
import ci.kossovo.ecole.web.models.personne.ApplicationModelPersonne;
import ci.kossovo.ecole.web.models.personne.PostAjoutPersonne;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EcoleWebapiApplicationTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ApplicationModelPersonne personneModel;

	private ObjectMapper mapper= new ObjectMapper();

	@Test
	public void trouverToutesPersonnes() throws Exception {
	
	//Donnée	
		Personne p1=new Personne(1l,"Mr", "Diarra", "Drissa","CNI01");
		p1.setType("PE");
		Personne p2=new Personne(2l,"Mr", "Traoré", "Abdoulaye","CNI02");
		p2.setType("PE");
		List<Personne> personnes= Arrays.asList(p1,p2);
		
		//when
		given(this.personneModel.findAll())
		.willReturn(personnes);
		
		//then
		this.mvc.perform(get("/personnes"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.body.length()", is(2)))
		.andExpect(jsonPath("$.body.[0].numCni", is("CNI01")));
		
	}
	
	
	
	/*@Test
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
		Personne p2 =new Personne(pos.getTitre(), pos.getNom(), pos.getPrenom(),pos.getNumCni());
		p2.setAdresse(ad);
		p2.setId(1L);
		
		given(this.personneModel.creer(p)).willReturn(p2);
		
		
		System.out.println(mapper.writeValueAsString(pos));
		System.out.println(mapper.writeValueAsString(p));
		System.out.println(mapper.writeValueAsString(p2));
		
		this.mvc.perform(post("/personnes")
				
				.accept(MediaType.APPLICATION_JSON_UTF8)
		.content(mapper.writeValueAsString(pos))
		)
		.andExpect(status().isOk());
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
