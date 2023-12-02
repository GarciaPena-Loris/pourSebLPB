package m1.archi.restclient.clientInterface.swingInterface;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;

public class InputDocumentListener implements DocumentListener {
    private final JButton button;
    private final ArrayList<JTextField> fields = new ArrayList<>();

    public InputDocumentListener(JButton button, JTextField... fields) {
        this.button = button;
        for (JTextField field : fields) {
            field.getDocument().addDocumentListener(this);
            this.fields.add(field);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateButtonState();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateButtonState();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateButtonState();
    }

    private void updateButtonState() {
        button.setEnabled(fields.stream().allMatch(field -> field.getText().length() >= 3));
    }
}
