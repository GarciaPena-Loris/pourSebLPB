package m1.archi.resthotel.exceptions;

public class ChambreNotFoundException extends ChambreException {
    public ChambreNotFoundException() {
    }

    public ChambreNotFoundException(String message) {
        super(message);
    }
}
