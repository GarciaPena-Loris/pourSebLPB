package m1.archi.restclient.clientInterface.swingInterface;

import ch.qos.logback.core.joran.sanity.Pair;
import m1.archi.restclient.models.modelsAgence.Agence;
import m1.archi.restclient.models.modelsHotel.Chambre;
import m1.archi.restclient.models.modelsHotel.Hotel;
import m1.archi.restclient.models.modelsHotel.Offre;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ResultPanel extends JPanel {
    private final RestTemplate proxyComparateur = new RestTemplate();
    private final Map<Long, Color> hotelColorMap = new HashMap<>();
    private final Map<Long, ImageIcon> chambreImageMap = new HashMap<>();
    private final Map<Long, Offre> mapOffreHotelPrixMin = new HashMap<>();
    private Offre offreMin;
    private final String font = "Palatino Linotype";
    private final Random RANDOM = new Random();

    public ResultPanel(Map<Agence, List<List<Offre>>> mapAgenceOffres, RestTemplate proxyComparateur) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel infoLabel = new JLabel("<html>Voici les <b>" + mapAgenceOffres.size() + " agences </b> qui proposent des offres correspondant à votre recherche <i>(les offres les moins chères par hotel sont en <font color='green'>vert</font> et l'offre la moins chère est en <font color='purple'>violet</font>)</i> :</div></html>");
        infoLabel.setFont(new Font(font, Font.PLAIN, 22));
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.BLACK), // Ajoutez une bordure en bas
                new EmptyBorder(0, 0, 20, 0) // Ajoutez de l'espace en haut et en bas
        ));
        add(infoLabel, BorderLayout.NORTH);


        // Créer un panneau pour contenir les agencePanels
        JPanel agencePanelContainer = new JPanel();
        agencePanelContainer.setLayout(new BoxLayout(agencePanelContainer, BoxLayout.Y_AXIS));

        // Rechercher pour l'ensemble des offres, la moins chere pour chaque hotel
        for (Map.Entry<Agence, List<List<Offre>>> entry : mapAgenceOffres.entrySet()) { // Pour chaque agence
            List<List<Offre>> offres = entry.getValue();
            for (List<Offre> offreList : offres) { // Pour chaque hôtel
                for (Offre offre : offreList) { // Pour chaque offre
                    if (mapOffreHotelPrixMin.containsKey(offre.getHotel().getIdHotel())) {
                        if (mapOffreHotelPrixMin.get(offre.getHotel().getIdHotel()).getPrixAvecReduction() > offre.getPrixAvecReduction()) {
                            mapOffreHotelPrixMin.put(offre.getHotel().getIdHotel(), offre);
                        }
                    } else {
                        mapOffreHotelPrixMin.put(offre.getHotel().getIdHotel(), offre);
                    }
                    if (offreMin == null || offreMin.getPrixAvecReduction() > offre.getPrixAvecReduction()) {
                        offreMin = offre;
                    }
                }
            }
        }

        for (Map.Entry<Agence, List<List<Offre>>> entry : mapAgenceOffres.entrySet()) { // Pour chaque agence
            List<List<Offre>> offres = entry.getValue();
            System.out.println(entry.getKey());
            JPanel agencePanel = createAgencePanel(entry.getKey(), offres);
            agencePanelContainer.add(agencePanel);
            agencePanelContainer.add(createSeparator());
        }

        JScrollPane scrollPane = new JScrollPane(agencePanelContainer);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

    }

    private JPanel createAgencePanel(Agence agence, List<List<Offre>> offres) {
        JPanel agencePanel = new JPanel(new BorderLayout());
        agencePanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        agencePanel.setOpaque(false);

        String agenceName = agence.getNom();

        JLabel agenceLabel;
        if (offres.size() == 1)
            agenceLabel = new JLabel(agenceName + " (" + offres.size() + " hotel) : ");
        else
            agenceLabel = new JLabel(agenceName + " (" + offres.size() + " hotels) : ");
        agenceLabel.setFont(new Font(font, Font.BOLD, 32));
        agenceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        agencePanel.add(agenceLabel, BorderLayout.NORTH);

        JPanel offrePanelContainer = new JPanel();
        offrePanelContainer.setLayout(new BoxLayout(offrePanelContainer, BoxLayout.Y_AXIS));
        offrePanelContainer.setOpaque(false);

        for (List<Offre> offreList : offres) { // Pour chaque hôtel
            Hotel hotel = offreList.get(0).getHotel();
            hotelColorMap.putIfAbsent(hotel.getIdHotel(), getRandomColorWithTransparency());
            JLabel hotelLabel = createHotelLabel(hotel, offreList.size(), hotelColorMap.get(hotel.getIdHotel()));
            JPanel hotelLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            hotelLabelPanel.add(hotelLabel);
            offrePanelContainer.add(hotelLabelPanel);

            JPanel hotelPanelContainer = new JPanel();
            hotelPanelContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 5));
            hotelPanelContainer.setOpaque(false);

            for (Offre offre : offreList) {
                JPanel offrePanel = createOffrePanel(agence, offre, hotelColorMap.get(hotel.getIdHotel()));
                hotelPanelContainer.add(offrePanel);

                // Si ce n'est pas la dernière offre, ajouter un séparateur
                if (offreList.indexOf(offre) != offreList.size() - 1) {
                    JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
                    separator.setForeground(hotelColorMap.get(hotel.getIdHotel()));
                    separator.setPreferredSize(new Dimension(2, 150));
                    hotelPanelContainer.add(separator);
                }
            }

            JPanel offrePanelContainerVertical = new JPanel();
            offrePanelContainerVertical.setLayout(new BoxLayout(offrePanelContainerVertical, BoxLayout.Y_AXIS));
            offrePanelContainerVertical.setBorder(new EmptyBorder(0, 20, 0, 0));
            offrePanelContainerVertical.add(hotelPanelContainer);

            offrePanelContainer.add(offrePanelContainerVertical);
        }

        agencePanel.add(offrePanelContainer, BorderLayout.CENTER);

        return agencePanel;
    }

    private JPanel createOffrePanel(Agence agence, Offre offre, Color hotelColor) {
        JPanel offrePanel;
        // Verifier si ce n'est pas l'offre la moins chere pour l'hotel si oui, mettre le fond en vert transparant
        // Mettre l'offre la moins chere en rouge
        Color blancTransparant = new Color(255, 255, 255, 200);
        Color vertTransparant = new Color(0, 255, 0, 50);
        Color violetTransparant = new Color(255, 0, 255, 50);
        Color violet = new Color(211, 21, 211);
        if (offre.getIdOffre() == offreMin.getIdOffre())
            offrePanel = new RoundedPanel(new BorderLayout(), 20, violetTransparant, violet);
        else if (offre.getIdOffre() == mapOffreHotelPrixMin.get(offre.getHotel().getIdHotel()).getIdOffre())
            offrePanel = new RoundedPanel(new BorderLayout(), 20, vertTransparant, hotelColor);
        else
            offrePanel = new RoundedPanel(new BorderLayout(), 20, blancTransparant, hotelColor);
        offrePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //Calculer la reduction en % entre le prix et le prix réduit
        int reduction = (int) ((1 - (offre.getPrixAvecReduction() / offre.getPrix())) * 100);

        // Texte centré en haut
        JLabel infoLabel;
        if (offre.getChambres().size() == 1)
            infoLabel = new JLabel("<html><div style='text-align: center;'><b>" + offre.getChambres().size() + "</b> chambre au prix de " + offre.getPrix() + "€, proposé à <b><font color='red'>" + offre.getPrixAvecReduction() + "€ </b> grâce à une réduction de <b>" + reduction + "% </b> :</div></html>");
        else
            infoLabel = new JLabel("<html><div style='text-align: center;'><b>" + offre.getChambres().size() + "</b> chambres au prix de " + offre.getPrix() + "€, proposées à <b><font color='red'>" + offre.getPrixAvecReduction() + "€</b> :</div></html>");
        infoLabel.setFont(new Font(font, Font.PLAIN, 15));
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel photoChambreContainer = new JPanel();
        photoChambreContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        photoChambreContainer.setOpaque(false);

        for (Chambre chambre : offre.getChambres()) {
            try {
                String base64ImageChambre = chambre.getImageChambre();
                if (base64ImageChambre == null || base64ImageChambre.isEmpty())
                    throw new Exception("Erreur lors de la récupération de la chambre");
                if (!chambreImageMap.containsKey(chambre.getIdChambre())) {
                    byte[] imageBytes = Base64.getDecoder().decode(base64ImageChambre);
                    ImageIcon chambreImageIcon = new ImageIcon(new ImageIcon(imageBytes).getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH));
                    chambreImageMap.put(chambre.getIdChambre(), chambreImageIcon);
                }
                // Créer un panel pour chaque chambre
                JPanel chambrePanel = new JPanel();
                chambrePanel.setOpaque(false);
                chambrePanel.setLayout(new BoxLayout(chambrePanel, BoxLayout.Y_AXIS));

                // Ajouter l'image de la chambre au panel
                JLabel chambreImage = new JLabel(chambreImageMap.get(chambre.getIdChambre()));
                chambreImage.setBorder(BorderFactory.createLineBorder(hotelColor, 2));
                chambreImage.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer l'image
                chambrePanel.add(chambreImage);

                // Créer un label pour le nombre de places dans la chambre
                JLabel placesLabel;
                if (chambre.getNombreLits() == 1)
                    placesLabel = new JLabel("Chambre " + chambre.getNombreLits() + " place, pour " + chambre.getPrix() + "€");
                else
                    placesLabel = new JLabel("Chambre " + chambre.getNombreLits() + " places, pour " + chambre.getPrix() + "€");
                placesLabel.setFont(new Font(font, Font.PLAIN, 13));
                placesLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer le label
                placesLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

                // Ajouter le label au panel
                chambrePanel.add(placesLabel);

                // Ajouter le panel au container
                photoChambreContainer.add(chambrePanel);
            } catch (Exception e) {
                System.out.println("Erreur lors de la récupération de la chambre");
            }
        }

        offrePanel.add(infoLabel, BorderLayout.NORTH);
        offrePanel.add(photoChambreContainer, BorderLayout.CENTER);

        // Mouse listener pour le panel
        offrePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                offrePanel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change le curseur en pointeur
                offrePanel.setBackground(offrePanel.getBackground().darker()); // Assombrit le fond sinon

            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                offrePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // Réinitialise le curseur
                offrePanel.setBackground(offrePanel.getBackground().brighter());
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                // Reduit légérement sa taille
                offrePanel.setSize(offrePanel.getWidth() - 5, offrePanel.getHeight() - 5);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                // Réinitialise sa taille
                offrePanel.setSize(offrePanel.getWidth() + 5, offrePanel.getHeight() + 5);
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // Popup qui demande si on veut vraiment reserver cette offre
                UIManager.put("OptionPane.yesButtonText", "Oui");
                UIManager.put("OptionPane.noButtonText", "Non");

                JPanel offreResume = new JPanel(new BorderLayout());
                offreResume.setBorder(new EmptyBorder(10, 10, 10, 10));

                JLabel infoLabelResume;
                if (offre.getChambres().size() == 1)
                    infoLabelResume = new JLabel("<html>Voulez-vous vraiment réserver cette offre à <b><font color='red'>" + offre.getPrixAvecReduction() + "€</font></b> avec " + offre.getChambres().size() + " chambre ?</html>");
                else
                    infoLabelResume = new JLabel("<html>Voulez-vous vraiment réserver cette offre à <b><font color='red'>" + offre.getPrixAvecReduction() + "€</font></b> avec " + offre.getChambres().size() + " chambres ?</html>");

                infoLabelResume.setFont(new Font(font, Font.PLAIN, 20));
                infoLabelResume.setHorizontalAlignment(JLabel.CENTER);
                infoLabelResume.setAlignmentX(Component.LEFT_ALIGNMENT);

                JPanel photoChambreContainerResume = new JPanel();
                photoChambreContainerResume.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                photoChambreContainerResume.setOpaque(false);

                for (Chambre chambre : offre.getChambres()) {
                    try {
                        String base64ImageChambre = chambre.getImageChambre();
                        if (base64ImageChambre == null || base64ImageChambre.isEmpty())
                            throw new Exception("Erreur lors de la récupération de la chambre");

                        byte[] imageBytes = Base64.getDecoder().decode(base64ImageChambre);
                        ImageIcon chambreImageIcon = new ImageIcon(new ImageIcon(imageBytes).getImage().getScaledInstance(350, 260, Image.SCALE_SMOOTH));

                        // Créer un panel pour chaque chambre
                        JPanel chambrePanelResume = new JPanel();
                        chambrePanelResume.setOpaque(false);
                        chambrePanelResume.setLayout(new BoxLayout(chambrePanelResume, BoxLayout.Y_AXIS));

                        // Ajouter l'image de la chambre au panel
                        JLabel chambreImageResume = new JLabel(chambreImageIcon);
                        chambreImageResume.setBorder(BorderFactory.createLineBorder(hotelColor, 2));
                        chambreImageResume.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer l'image
                        chambrePanelResume.add(chambreImageResume);

                        // Créer un label pour le nombre de places dans la chambre
                        JLabel placesLabelResume;
                        if (chambre.getNombreLits() == 1)
                            placesLabelResume = new JLabel("Chambre " + chambre.getNombreLits() + " place, pour " + chambre.getPrix() + "€");
                        else
                            placesLabelResume = new JLabel("Chambre " + chambre.getNombreLits() + " places, pour " + chambre.getPrix() + "€");
                        placesLabelResume.setFont(new Font(font, Font.PLAIN, 18));
                        placesLabelResume.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer le label
                        placesLabelResume.setBorder(new EmptyBorder(5, 0, 0, 0));

                        // Ajouter le label au panel
                        chambrePanelResume.add(placesLabelResume);

                        // Ajouter le panel au container
                        photoChambreContainerResume.add(chambrePanelResume);
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la récupération de la chambre");
                    }

                    offreResume.add(infoLabelResume, BorderLayout.NORTH);
                    offreResume.add(photoChambreContainerResume, BorderLayout.CENTER);
                }

                Object[] options = {"<html><font size=\"4\">Oui</font></html>", "<html><font size=\"4\">Non</font></html>"};
                int dialogResult = JOptionPane.showOptionDialog(null, offreResume, "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    new ConnexionAgenceFrame(agence, offre, proxyComparateur);
                }
            }
        });

        return offrePanel;
    }


    private JLabel createHotelLabel(Hotel hotel, int nombreOffre, Color hotelColor) {
        // Créer un label pour l'hôtel
        JLabel hotelLabel;
        String adresseHotel = hotel.getAdresse().getNumero() + " " + hotel.getAdresse().getRue();

        if (nombreOffre == 1)
            hotelLabel = new JLabel("<html><div style='font-family: Palatino Linotype; font-size: 17px; font-weight: bold;'> · L'hotel <font color='" + getColorHexString(hotelColor) + "'> " + hotel.getNom() + "</font> <font style='font-size: 20px' color='#B8860B'>" + "⭐".repeat(Math.max(0, hotel.getNombreEtoiles())) + "</font>, " + adresseHotel + " <i>(" + nombreOffre + " option)</i> :</div></html>");
        else
            hotelLabel = new JLabel("<html><div style='font-family: Palatino Linotype; font-size: 17px; font-weight: bold;'> · L'hotel <font color='" + getColorHexString(hotelColor) + "'> " + hotel.getNom() + "</font> <font style='font-size: 20px' color='#B8860B'>" + "⭐".repeat(Math.max(0, hotel.getNombreEtoiles())) + "</font>, " + adresseHotel + "  <i>(" + nombreOffre + " options)</i> :</div></html>");
        hotelLabel.setFont(new Font(font, Font.BOLD, 17));

        return hotelLabel;
    }

    private String getColorHexString(Color color) {
        // Convertir la couleur en représentation hexadécimale
        return String.format("#%06x", color.getRGB() & 0xFFFFFF);
    }

    private Color getRandomColorWithTransparency() {
        int red = RANDOM.nextInt(201);
        int green = RANDOM.nextInt(201);
        int blue = RANDOM.nextInt(201);
        return new Color(red, green, blue);
    }


    private JSeparator createSeparator() {
        // Créer un séparateur
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.GRAY); // Couleur du trait
        separator.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrer le trait horizontalement

        return separator;
    }

}