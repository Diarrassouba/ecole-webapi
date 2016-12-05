package ci.kossovo.ecole.web.utilitaires;

import java.util.ArrayList;
import java.util.List;

public class Static {

	public Static() {
		super();
	}

	// Lites des messages d'erreurs d'une exception
	public static List<String> getErreurforexception(Exception exception) {

		// on récupère la liste des messages d'erreur de l'exception
		Throwable cause = exception;
		List<String> erreurs = new ArrayList<>();
		while (cause != null) {

			// on récupère le message seulement s'il est !=null et non blanc
			String message = cause.getMessage();
			if (message != null) {
				message = message.trim();
				if (message.length() != 0) {
					erreurs.add(message);
				}
			}

			// cause suivante
			cause = cause.getCause();
		}
		return erreurs;
	}

}
