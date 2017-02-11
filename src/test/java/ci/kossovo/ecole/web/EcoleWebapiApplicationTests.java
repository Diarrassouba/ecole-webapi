package ci.kossovo.ecole.web;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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

import ci.kossovo.ecole.entity.Personne;
import ci.kossovo.ecole.web.models.personne.ApplicationModelPersonne;
import ci.kossovo.ecole.web.services.PersonneRestService;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonneRestService.class)
public class EcoleWebapiApplicationTests {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private ApplicationModelPersonne personneModel;
	
	private ObjectMapper mapper= new ObjectMapper();

	@Test
	public void trouverToutesPersonnes() throws Exception {
	
	//Donnée	
		List<Personne> personnes= Arrays.asList(
				new Personne(1l,"Mr", "Diarra", "Drissa","CNI01"),
				new Personne(2l,"Mr", "Traoré", "Abdoulaye","CNI02"));
		
		//when
		given(this.personneModel.findAll())
		.willReturn(personnes);
		
		//then
		this.mvc.perform(get("/personnes"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.body.length()", is(2)))
		.andExpect(jsonPath("$.body.[0].nom", is("Diarra")));
		
	}

}
