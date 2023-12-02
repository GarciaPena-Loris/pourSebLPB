package m1.archi.restclient.clientInterface.swingInterface;

import m1.archi.restclient.models.modelsAgence.Agence;
import m1.archi.restclient.models.modelsHotel.Offre;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CustomContentPane extends JPanel {
    private final JFrame frame;
    public CustomContentPane(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

    }
    public void rebuild(Map<Agence, List<List<Offre>>> mapAgenceOffres, RestTemplate proxyComparateur) {
        Component[] components = getComponents();
        if (components.length > 1) {
            remove(components[1]);
        }
        add(new ResultPanel(mapAgenceOffres, proxyComparateur), BorderLayout.CENTER);

        frame.revalidate();
        frame.repaint();
    }
}
