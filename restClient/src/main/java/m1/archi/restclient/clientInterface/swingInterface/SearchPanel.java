package m1.archi.restclient.clientInterface.swingInterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import m1.archi.restclient.models.modelsAgence.Agence;
import m1.archi.restclient.models.modelsHotel.Offre;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class SearchPanel extends JPanel {
    private final RestTemplate proxyComparateur;
    private final CustomContentPane customContentPane;
    // Pickers
    private final JComboBox<String> villeField;
    private final JDatePickerImpl dateDepartPicker;
    private final JDatePickerImpl dateArriveePicker;
    private final JSpinner peopleSpinner;
    private final JSpinner starsSpinner;
    private final JButton searchButton;
    private final String baseUri = "http://localhost:8100/comparateurService/api";
    private static final String font = "Palatino Linotype";
    private static final String[] listeVilles = {"Paris", "Toulouse", "Nice", "Madrid", "Barcelone", "Valence", "Athènes", "Rhodes", "Toronto", "Montréal"};

    public SearchPanel(CustomContentPane customContentPane, RestTemplate proxyComparateur) {
        this.customContentPane = customContentPane;
        this.proxyComparateur = proxyComparateur;

        setBorder(new EmptyBorder(10, 15, 10, 15));
        setOpaque(false);

        Integer nombreAgencePartenaire;
        try {
            nombreAgencePartenaire = proxyComparateur.getForObject(baseUri + "/comparateur/idAgences/count", Integer.class);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Aucune agence n'est disponible...", "Erreur", JOptionPane.ERROR_MESSAGE);
            throw e;
        }

        JPanel bubbleBox = new JPanel(new BorderLayout());
        bubbleBox.setBorder(new TextBubbleBorder(Color.BLACK, 4, 20, 20));

        JLabel searchLabel = new JLabel("Veuillez renseigner les informations de recherche pour trouver votre hôtel parmi nos " + nombreAgencePartenaire + " agences partenaires :");
        searchLabel.setHorizontalAlignment(JLabel.CENTER);
        searchLabel.setFont(new Font(font, Font.BOLD, 17));
        bubbleBox.add(searchLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setOpaque(false);

        // Ville d'hébergement
        Arrays.sort(listeVilles);
        villeField = new JComboBox<>(listeVilles);
        JLabel villeLabel = new JLabel("Ville d'hébergement :");
        villeField.setFont(new Font(font, Font.PLAIN, 14));
        villeLabel.setFont(new Font(font, Font.PLAIN, 14));
        searchPanel.add(villeLabel);
        searchPanel.add(villeField);

        // Dates de réservation
        UtilDateModel dateArriveeModel = new UtilDateModel();
        dateArriveePicker = new JDatePickerImpl(new JDatePanelImpl(dateArriveeModel, new Properties()), new DateLabelFormatter());

        UtilDateModel dateDepartModel = new UtilDateModel();
        dateDepartPicker = new JDatePickerImpl(new JDatePanelImpl(dateDepartModel, new Properties()), new DateLabelFormatter());

        JLabel dateArriveeLabel = new JLabel("Arrivée :");
        dateArriveeLabel.setFont(new Font(font, Font.PLAIN, 14));
        dateArriveeLabel.setBorder(new EmptyBorder(5, 15, 0, 0));
        searchPanel.add(dateArriveeLabel);
        searchPanel.add(dateArriveePicker);

        JLabel dateDepartLabel = new JLabel("Départ :");
        dateDepartLabel.setFont(new Font(font, Font.PLAIN, 14));
        dateDepartLabel.setBorder(new EmptyBorder(5, 15, 0, 0));
        searchPanel.add(dateDepartLabel);
        searchPanel.add(dateDepartPicker);

        // Nombre de personnes à héberger
        peopleSpinner = createSpinner(1, 1, 10);
        JLabel peopleLabel = new JLabel("Nombre de personnes :");
        peopleLabel.setFont(new Font(font, Font.PLAIN, 14));
        peopleLabel.setBorder(new EmptyBorder(2, 15, 0, 0));
        peopleSpinner.setFont(new Font(font, Font.BOLD, 18));
        searchPanel.add(peopleLabel);
        searchPanel.add(peopleSpinner);

        // Nombre minimum d'étoiles
        starsSpinner = createSpinner(1, 1, 5);
        JLabel starsLabel = new JLabel("Étoiles minimum :");
        starsLabel.setFont(new Font(font, Font.PLAIN, 14));
        starsLabel.setBorder(new EmptyBorder(2, 15, 0, 0));
        starsSpinner.setFont(new Font(font, Font.BOLD, 18));
        starsSpinner.setBorder(new EmptyBorder(0, 0, 0, 20));
        searchPanel.add(starsLabel);
        searchPanel.add(starsSpinner);

        searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        // Bouton de recherche
        searchButton = new CustomButton();
        searchButton.setText("Rechercher");
        searchButton.setToolTipText("Veuillez renseigner les informations de recherche");
        ((CustomButton) searchButton).setRadius(20);
        searchButton.setFont(new Font(font, Font.BOLD, 20));
        searchButton.setBorder(new EmptyBorder(10, 20, 0, 20));
        searchButton.addActionListener(e -> {
            // Action à effectuer lors du clic sur le bouton de recherche
            performSearch((String) villeField.getSelectedItem(), dateArriveeModel.getValue(), dateDepartModel.getValue(),
                    (int) peopleSpinner.getValue(), (int) starsSpinner.getValue());
        });
        searchPanel.add(searchButton);

        // Appeler la méthode pour mettre à jour l'état du bouton chaque fois que les données changent
        updateButtonState();

        // Ajouter des écouteurs de changement pour les composants qui affectent l'état du bouton
        villeField.addActionListener(e -> updateButtonState());
        dateArriveeModel.addChangeListener(e -> updateButtonState());
        dateDepartModel.addChangeListener(e -> updateButtonState());
        peopleSpinner.addChangeListener(e -> updateButtonState());
        starsSpinner.addChangeListener(e -> updateButtonState());

        bubbleBox.add(searchPanel, BorderLayout.CENTER);
        add(bubbleBox, BorderLayout.WEST);
    }

    private JSpinner createSpinner(int init, int min, int max) {
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(init, min, max, 1));

        return spinner;
    }

    private void updateButtonState() {
        // Vérifiez la validité des données et activez/désactivez le bouton en conséquence
        boolean isDateArriveeValid = dateArriveePicker.getModel().getValue() != null;
        boolean isDateDepartValid = dateDepartPicker.getModel().getValue() != null;
        boolean isVilleValid = villeField.getSelectedItem() != null;
        boolean isDateOrderValid = true;
        if (isDateArriveeValid && isDateDepartValid) {
            Date dateArrivee = (Date) dateArriveePicker.getModel().getValue();
            Date dateDepart = (Date) dateDepartPicker.getModel().getValue();
            isDateOrderValid = dateArrivee.before(dateDepart);
        }
        boolean isValid = isDateArriveeValid && isDateDepartValid && isVilleValid && isDateOrderValid;
        searchButton.setEnabled(isValid);
        if (!isValid) {
            ((CustomButton) searchButton).setColorOver(Color.WHITE);
        } else {
            ((CustomButton) searchButton).setColorOver(Color.GREEN);
        }
    }

    private void performSearch(String ville, Date dateArrivee, Date dateDepart, int nombrePersonne, int nombreEtoilesMin) {
        String villeEncodee = URLEncoder.encode(ville, StandardCharsets.UTF_8);
        String comparateurUri = baseUri + "/comparateur/recherche";

        //Convertie les Date en LocalDateTime
        LocalDateTime dateArriveeLDT = dateArrivee.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateDepartLDT = dateDepart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(comparateurUri)
                .queryParam("ville", villeEncodee)
                .queryParam("dateArrivee", dateArriveeLDT)
                .queryParam("dateDepart", dateDepartLDT)
                .queryParam("prixMin", 0)
                .queryParam("prixMax", Integer.MAX_VALUE)
                .queryParam("nombrePersonne", nombrePersonne)
                .queryParam("nombreEtoiles", nombreEtoilesMin);


        ParameterizedTypeReference<Map<Agence, List<List<Offre>>>> typeReference =
                new ParameterizedTypeReference<Map<Agence, List<List<Offre>>>>() {};


        // Appel à la méthode de recherche d'offres de l'hôtel via le proxyHotel
        try {
            ResponseEntity<Map<Agence, List<List<Offre>>>> responseEntity = proxyComparateur.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, null, typeReference);
            Map<Agence, List<List<Offre>>> mapAgenceOffres = responseEntity.getBody();

            for (Map.Entry<Agence, List<List<Offre>>> entry : mapAgenceOffres.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getKey().getClass());
            }

            if (mapAgenceOffres != null && mapAgenceOffres.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune offre n'est disponible...", "Résultat", JOptionPane.ERROR_MESSAGE);
            } else {
                //customContentPane.rebuild(mapAgenceOffres, proxyComparateur);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

            JOptionPane.showMessageDialog(this, "Aucune offre n'est disponible...", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
