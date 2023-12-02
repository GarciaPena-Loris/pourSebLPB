package m1.archi.restclient.clientInterface.swingInterface;

import m1.archi.restclient.exceptions.InterfaceException;
import m1.archi.restclient.models.modelsComparateur.Comparateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class Interface {
    private final Comparateur comparateurService;
    private final RestTemplate proxyComparateur;
    private static final String BASE_URI = "http://localhost:8100/comparateurService/api";

    public Interface(RestTemplate proxyComparateur) throws InterfaceException {
        if (proxyComparateur == null)
            throw new InterfaceException("Problème de connexion avec le comparateur");

        this.proxyComparateur = proxyComparateur;
        // Récupération le comparateur
        comparateurService = proxyComparateur.getForObject(BASE_URI + "/comparateur", Comparateur.class);

        if (comparateurService == null)
            throw new InterfaceException("Problème lors de la récupération du comparateur");

        // Création de l'interface graphique
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
            String nomComparateur = comparateurService.getNom();
            JFrame frame = new JFrame("Bienvenue sur le comparateur d'agences " + nomComparateur + " :");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CustomContentPane customContentPane = new CustomContentPane(frame);
            frame.setContentPane(customContentPane);

            // Création de la barre de recherche
            JPanel searchPanel = new SearchPanel(customContentPane, proxyComparateur);
            customContentPane.add(searchPanel, BorderLayout.NORTH);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });
    }
}
