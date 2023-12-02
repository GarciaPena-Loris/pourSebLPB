package m1.archi.resthotel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class HotelNotFoundExceptionAdvice {
    @ExceptionHandler(HotelNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String hotelNotFoundHandler(HotelNotFoundException ex) {
        return String.format("{\"%s\": \"%s\"}", "error", ex.getMessage());
    }
}
