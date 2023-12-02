package m1.archi.restagence.exceptions;

public class UtilisateurWrongPasswordException extends UtilisateurException {
    public UtilisateurWrongPasswordException() {
    }

    public UtilisateurWrongPasswordException(String message) {
        super(message);
    }
}
