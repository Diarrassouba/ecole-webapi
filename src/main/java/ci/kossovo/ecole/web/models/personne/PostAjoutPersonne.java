package ci.kossovo.ecole.web.models.personne;

public class PostAjoutPersonne {
	protected String titre;
	protected String nom;
	protected String prenom;
	protected String numCni;
	protected PostAdresse adresse;

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNumCni() {
		return numCni;
	}

	public void setNumCni(String numCni) {
		this.numCni = numCni;
	}

	public PostAdresse getAdresse() {
		return adresse;
	}

	public void setAdresse(PostAdresse adresse) {
		this.adresse = adresse;
	}

	

}
