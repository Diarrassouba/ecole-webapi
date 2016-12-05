package ci.kossovo.ecole.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebConfig {
	
	//mapper json	
	@Bean
	public ObjectMapper jsonMapper(){
		return new ObjectMapper();
	}

}
