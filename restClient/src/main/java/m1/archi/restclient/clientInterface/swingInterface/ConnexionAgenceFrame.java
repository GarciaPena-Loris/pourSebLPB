package m1.archi.restclient.clientInterface.swingInterface;

import m1.archi.restclient.models.modelsAgence.Agence;
import m1.archi.restclient.models.modelsAgence.Utilisateur;
import m1.archi.restclient.models.modelsHotel.Offre;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ConnexionAgenceFrame extends JFrame {
    private final Agence agence;
    private final Offre offre;
    private final RestTemplate proxyReservation;
    private final String font = "Palatino Linotype";
    private final String baseUri = "http://localhost:8090/agenceService/api";
    private final int fontSize = 20; // Définissez la taille de la police ici

    public ConnexionAgenceFrame(Agence agence, Offre offre, RestTemplate proxyComparateur) {
        super("Connexion à l'agence " + agence.getNom() + " :");
        this.agence = agence;
        this.offre = offre;
        this.proxyReservation = proxyComparateur;

        Object[] options = {"<html><font size=\"4\">OUI : Se connecter</font></html>", "<html><font size=\"4\">NON : Créer un compte</font></html>"};
        int choice = JOptionPane.showOptionDialog(this,
                "<html><font size=\"4\">Possédé vous un compte dans cette agence ?</font></html>",
                "Connexion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            showLoginDialog("");
        } else if (choice == JOptionPane.NO_OPTION) {
            showRegisterDialog();
        }
    }

    private void showLoginDialog(String email) {
        JTextField emailField = new JTextField(20);
        emailField.setText(email);
        emailField.setFont(new Font(font, Font.PLAIN, fontSize)); // Utilisez votre police et taille de police

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font(font, Font.PLAIN, fontSize)); // Utilisez votre police et taille de police

        Object[] message = {
                "Email:", emailField,
                "Mot de passe:", passwordField
        };

        Object[] options = {"<html><font size=\"4\">Connection</font></html>", "<html><font size=\"4\">Annuler</font></html>"};
        int option = JOptionPane.showOptionDialog(this, message, "Connection", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (option == JOptionPane.OK_OPTION) {
            email = emailField.getText();
            String password = new String(passwordField.getPassword());

            String url = baseUri + "/agences/utilisateurs?email={email}&motDePasse={motDePasse}";

            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez renseigner tous les champs", "Problème...", JOptionPane.ERROR_MESSAGE);
                showLoginDialog(email);
                dispose();
            } else {
                try {
                    Utilisateur utilisateur = proxyReservation.getForObject(url, Utilisateur.class, params);
                    if (utilisateur == null) {
                        JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect", "Connection impossible !", JOptionPane.ERROR_MESSAGE);
                        showLoginDialog(email);
                        dispose();
                    } else {

                        // Vérifie sio l'utilisateur est bien dans l'agence
                        url = baseUri + "/agences/{id}/utilisateurs/{idUtilisateur}";

                        Map<String, Long> paramsUser;
                        paramsUser = new HashMap<>();
                        paramsUser.put("id", agence.getIdAgence());
                        paramsUser.put("idUtilisateur", utilisateur.getIdUtilisateur());

                        try {
                            utilisateur = proxyReservation.getForObject(url, Utilisateur.class, paramsUser);
                            if (utilisateur == null) {
                                JOptionPane.showMessageDialog(this, "Vous n'etes pas inscrit dans cette agence", "Connection impossible !", JOptionPane.ERROR_MESSAGE);
                                showLoginDialog(email);
                                dispose();
                            } else {
                                new ReserverOffreFrame(offre, utilisateur, proxyReservation);
                                dispose();
                            }

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Vous n'etes pas inscrit dans cette agence", "Connection impossible !", JOptionPane.ERROR_MESSAGE);
                            showLoginDialog(email);
                            dispose();
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Email ou mot de passe incorrect", "Connection impossible !", JOptionPane.ERROR_MESSAGE);
                    showLoginDialog(email);
                    dispose();
                }
            }
        } else {
            new ConnexionAgenceFrame(agence, offre, proxyReservation);
            dispose();
        }
    }

    private void showRegisterDialog() {
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font(font, Font.PLAIN, fontSize));

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font(font, Font.PLAIN, fontSize));

        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font(font, Font.PLAIN, fontSize));

        JTextField surnameField = new JTextField(20);
        surnameField.setFont(new Font(font, Font.PLAIN, fontSize));

        Object[] message = {
                "Email:", emailField,
                "Mot de passe:", passwordField,
                "Nom:", nameField,
                "Prénom:", surnameField
        };

        Object[] options = {"<html><font size=\"4\">Inscription</font></html>", "<html><font size=\"4\">Annuler</font></html>"};
        int option = JOptionPane.showOptionDialog(this, message, "Inscription", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (option == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String surname = surnameField.getText();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez renseigner tous les champs", "Problème...", JOptionPane.ERROR_MESSAGE);
                showRegisterDialog();
                dispose();
            } else {
                String url = baseUri + "/agences/{id}/utilisateurs";

                Map<String, Long> params = new HashMap<>();
                params.put("id", agence.getIdAgence());

                Utilisateur utilisateurToSend = new Utilisateur(email, password, name, surname, agence);

                try {
                    Utilisateur utilisateur = proxyReservation.postForObject(url, utilisateurToSend, Utilisateur.class, params);

                    if (utilisateur == null) {
                        JOptionPane.showMessageDialog(this, "Erreur lors de votre inscription", "Erreur", JOptionPane.ERROR_MESSAGE);
                        showLoginDialog(email);
                        dispose();
                    } else {
                        new ReserverOffreFrame(offre, utilisateur, proxyReservation);
                        dispose();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(this, "Vous êtes déja inscrit dans cette agence", "Inscription impossible !", JOptionPane.ERROR_MESSAGE);
                    showLoginDialog(email);
                    dispose();
                }
            }
        } else {
            new ConnexionAgenceFrame(agence, offre, proxyReservation);
            dispose();
        }
    }
}