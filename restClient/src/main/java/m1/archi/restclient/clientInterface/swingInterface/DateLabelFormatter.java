package m1.archi.restclient.clientInterface.swingInterface;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    private final String datePattern = "dd MMMM yyyy";
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern, Locale.getDefault());


    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            if (value instanceof Date) {
                return dateFormatter.format((Date) value);
            } else if (value instanceof GregorianCalendar) {
                return dateFormatter.format(((GregorianCalendar) value).getTime());
            } else {
                return "Erreur...";
            }
        } else {
            return "Selectionnez une date..."; // ou une représentation par défault
        }
    }
}
