package m1.archi.restcomparateur.exceptions;

public class NoRoomAvailableException extends AgenceException {
    public NoRoomAvailableException() {
    }

    public NoRoomAvailableException(String message) {
        super(message);
    }
}
