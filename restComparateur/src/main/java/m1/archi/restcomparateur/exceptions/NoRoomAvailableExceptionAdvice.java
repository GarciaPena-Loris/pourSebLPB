package m1.archi.restcomparateur.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoRoomAvailableExceptionAdvice {
    @ExceptionHandler(NoRoomAvailableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noRoomAvailableHandler(NoRoomAvailableException ex) {
        return String.format("{\"%s\": \"%s\"}", "no result", ex.getMessage());
    }
}
