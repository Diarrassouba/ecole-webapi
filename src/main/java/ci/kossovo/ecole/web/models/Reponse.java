package ci.kossovo.ecole.web.models;

import java.util.List;

public class Reponse<T> {
	// ----------------- propriétés
	
		// statut de l'opération
	private int status;
	
	// les éventuels messages d'erreur
	private List<String> messages;
	// le corps de la réponse
	private T body;
	public Reponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Reponse(int status, List<String> messages, T body) {
		super();
		this.status = status;
		this.messages = messages;
		this.body = body;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	
	

}
