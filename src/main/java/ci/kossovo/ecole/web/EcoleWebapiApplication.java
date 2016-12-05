package ci.kossovo.ecole.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import ci.kossovo.ecole.DomainAndPersistenceConfig;

@SpringBootApplication
@Import(DomainAndPersistenceConfig.class)
public class EcoleWebapiApplication{
	

	public static void main(String[] args) {
	SpringApplication.run(EcoleWebapiApplication.class, args);
	
	}
	
	/*@Bean
	public CommandLineRunner personneMetier(ApplicationModelPersonne modelPersonne) {
		return (args) -> {
			// save a couple of customers
			modelPersonne.creer(new Personne("Mr", "Jack", "Bauer","CNI0021"));
			modelPersonne.creer(new Personne("Mlle", "Chloe", "O'Brian","CNI0022"));
			modelPersonne.creer(new Etudiant("Mr", "Traore", "Abou","CNI0023", "E00102"));
			modelPersonne.creer(new Etudiant("Mr", "Koné", "Moussa","CNI0024", "E00103"));
			modelPersonne.creer(new Etudiant("Mr", "Bamba", "Abou","CNI0028", "E00101"));
			modelPersonne.creer(new Enseignant("Mme", "Koné", "Asta","CNI0121", "Active"));
			modelPersonne.creer(new Enseignant("Mr", "Kanté", "Bakary","CNI0021", "Active"));
			modelPersonne.creer(new Enseignant("Mme", "Fofana", "Fanta","CNI0041", "Active"));
			modelPersonne.creer(new Administrateur("Mr", "Kaba", "Mawa","CNI0065", "Censeur"));
			modelPersonne.creer(new Administrateur("Mme", "Fofana", "Sita","CNI0033", "Comptable"));
			modelPersonne.creer(new Administrateur("Mme", "Diomandé", "Mariam","CNI00541", "Directeur etude"));
		};
		}
*/
	
}
