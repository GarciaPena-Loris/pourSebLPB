package m1.archi.resthotel.data;

import m1.archi.resthotel.models.Adresse;
import m1.archi.resthotel.models.Chambre;
import m1.archi.resthotel.models.Hotel;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

public class RandomDonneesHotel {
    private static final String[] listeRues = {
            "Avenue des Orangers", "Rue des Étoiles Filantes", "Boulevard des Lumières", "Chemin de la Cascade", "Allée des Cerisiers",
            "Rue de la Sérénité", "Avenue des Trois Chênes", "Boulevard de l'Horizon", "Chemin des Roses", "Allée des Mélodies",
            "Rue des Perles", "Avenue du Crépuscule", "Boulevard des Montagnes", "Chemin des Papillons", "Allée des Hibiscus",
            "Rue de la Brise Marine", "Avenue des Oiseaux Chanteurs", "Boulevard des Saveurs", "Chemin de la Fontaine", "Allée des Émeraudes",
            "Rue des Rêves", "Avenue des Amoureux", "Boulevard de l'Enchantement", "Chemin des Arc-en-Ciel", "Allée des Palmiers",
            "Rue des Souvenirs", "Avenue des Poètes", "Boulevard de la Tranquillité", "Chemin des Lucioles", "Allée des Lys",
            "Rue des Illusions", "Avenue des Mirages", "Boulevard de la Douceur", "Chemin des Mélancolies", "Allée des Chansons",
            "Rue des Farfadets", "Avenue des Énigmes", "Boulevard de la Magie", "Chemin des Vignes", "Allée des Artistes",
            "Rue des Bougies", "Avenue des Rires", "Boulevard des Aurores", "Chemin des Étoiles Brillantes", "Allée des Parfums",
            "Rue des Légendes", "Avenue des Secrets", "Boulevard de l'Aventure", "Chemin des Papillons de Nuit", "Allée des Ombres",
            "Rue des Mystères", "Avenue des Contes de Fées", "Boulevard de la Liberté", "Chemin des Arômes", "Allée des Reflets",
            "Rue des Saisons", "Avenue des Embruns", "Boulevard des Merveilles", "Chemin des Sourires", "Allée des Étoiles du Matin"
    };

    private static final String[] listeNomHotel = {
            "Lumière d'Étoile", "Le Château Doré", "Auberge de la Brise Marine", "Lodge en Montagne", "Suites de la Vie Urbaine", "Vue sur la Rivière",
            "Paradis des Palmiers", "Lodge des Plages d'Or", "Couronne Royale", "Auberge des Eaux Azurées", "Manoir de la Vallée", "Retraite au Bord du Lac",
            "Suites Ciel Étoilé", "Spa Sérénité", "Vue sur le Port", "Auberge de l'Oasis Exotique", "Lodge des Pins Tranquilles",
            "Maison du Bois Rouge", "Refuge en Bord de Mer", "Lodge Montagneux", "Auberge des Pins Sereins", "Ciel Azur",
            "Auberge de la Baie de Corail", "Auberge des Rives d'Argent", "Plaza Royale", "Émeraude Île", "Retraite au Bord de la Rivière",
            "Auberge Vue sur le Port", "Palais Doré", "Auberge du Charme Cottage", "Suites Prestige", "Auberge au Bord du Lac",
            "Manoir des Bois Murmureurs", "Porte Impériale", "Auberge de la Brise Côtière", "Villa Étoilée", "Retraite de Plage aux Palmiers",
            "Lodge au Bord du Lac", "Palais de Saphir", "Rive de la Rivière", "Auberge de la Campagne Tranquille", "Port de Lumière",
            "Manoir Clair de Lune Mirage", "Auberge Vue sur le Soleil Couchant", "Lodge Vue sur la Mer", "Auberge au Paradis Tropical",
            "Retraite Impériale", "Majesté Montagnarde", "Suites du Centre-Ville", "Auberge de la Baie de Corail", "Auberge de l'Île d'Émeraude",
            "Auberge au Bord de la Rivière", "Retraite au Port de Marina", "Palais aux Palmiers Dorés", "Retraite au Bord de la Rivière Tranquille",
            "Oasis de Luxe", "Bord du Port Harmonieux", "Printemps de l'Émeraude", "Retraite au Bord du Lac Scintillant", "Vue sur le Soleil Couchant Paradisiaque",
            "Retraite au Bord du Lac Paisible", "Retraite au Port de Marina", "Château d'Émeraude", "Lodge du Charme Cottage", "Plaza Prestige",
            "Auberge de Sérénité au Bord du Lac Tranquille", "Auberge de la Campagne Chuchotante"
    };

    private static final String[] listePays = {
            "France", "Espagne", "Grece", "Canada"
    };

    private static final HashMap<String, ArrayList<String>> listeVillePays = new HashMap<>();

    static {
        listeVillePays.put("France", new ArrayList<>() {{
            add("Paris");
            add("Toulouse");
            add("Nice");
        }});
        listeVillePays.put("Espagne", new ArrayList<>() {{
            add("Madrid");
            add("Barcelone");
            add("Valence");
        }});
        listeVillePays.put("Grece", new ArrayList<>() {{
            add("Athènes");
            add("Rhodes");
        }});
        listeVillePays.put("Canada", new ArrayList<>() {{
            add("Toronto");
            add("Montréal");
        }});
    }

    private static <T> T getElementListeAleatoire(T[] liste) {
        return liste[new Random().nextInt(liste.length)];
    }

    public static String randomNomHotel() {
        return getElementListeAleatoire(listeNomHotel);
    }

    public static String randomRue() {
        return getElementListeAleatoire(listeRues);
    }

    public static String randomPays() {
        return getElementListeAleatoire(listePays);
    }

    public static String randomVille(String pays) {
        ArrayList<String> villesPays = listeVillePays.get(pays);
        return getElementListeAleatoire(villesPays.toArray(new String[0]));
    }

    public static File randomImagePays(String pays) {
        String imageDirectory = "src/main/resources/imagesHotels/" + pays + "/";
        return getImage(imageDirectory);
    }

    private static File getImage(String imageDirectory) {
        File directory = new File(imageDirectory);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                Random random = new Random();
                int randomIndex = random.nextInt(files.length);
                return files[randomIndex];
            }
        }
        System.out.println("Aucune image trouvée dans le dossier " + imageDirectory);
        return null; // Aucune image trouvée
    }

    public static String randomNumero() {
        Random random = new Random();
        String numeroRue = String.valueOf(random.nextInt(100) + 1); // Numéro de rue entre 1 et 100
        if (random.nextInt(8) == 0) {
            numeroRue += "bis"; // Ajoute "bis" avec une chance sur 8
        } else if (random.nextInt(15) == 0) {
            numeroRue += "ter"; // Ajoute "ter" avec une chance sur 15
        } else if (random.nextInt(100) == 0) {
            numeroRue += "shrek"; // Ajoute "shrek" avec une chance sur 100
        }
        return numeroRue;
    }

    public static String randomPositionGPS() {
        return new Random().nextDouble() * 180 - 90 + "," + (new Random().nextDouble() * 360 - 180); // Position GPS aléatoire
    }

    public static int randomNombreEtoiles() {
        return new Random().nextInt(5) + 1; // Nombre d'étoiles entre 1 et 5
    }

    public static int randomPrix(int nombreEtoile, int nombreLits) {
        switch (nombreEtoile) {
            case 1:
                return new Random().nextInt(10) + 30 + (nombreLits * 10); // Prix entre 50 et 80 + 5€ par lit
            case 2:
                return new Random().nextInt(20) + 55 + (nombreLits * 12); // Prix entre 55 et 85 + 5€ par lit
            case 3:
                return new Random().nextInt(40) + 90 + (nombreLits * 15); // Prix entre 60 et 90 + 5€ par lit
            case 4:
                return new Random().nextInt(80) + 130 + (nombreLits * 15); // Prix entre 65 et 95 + 5€ par lit
            case 5:
                return new Random().nextInt(250) + 200 + (nombreLits * 20); // Prix entre 70 et 100 + 5€ par lit
            default:
                return new Random().nextInt(50) + (nombreLits * (nombreEtoile * 5)) + 50 * nombreEtoile; // Prix entre 50 et 130 + 5€ par lit + 50€ par étoile  (prix entre 50 et 130 pour 1 étoile, entre 55 et 135 pour 2 étoiles, etc.)
        }
    }

    public static int randomNombreLits() {
        return new Random().nextInt(4) + 1; // Nombre de personnes entre 1 et 4
    }

    public static File randomImageChambre(int nombreEtoiles) {
        String imageDirectory = "src/main/resources/imagesChambres/" + nombreEtoiles + "etoiles/";
        return getImage(imageDirectory);
    }

    public static int randomNombreChambres() {
        return new Random().nextInt(50) + 5; // Nombre de chambres entre 1 et 10
    }

    /* Hotel */
    public static Hotel generateRandomHotel(Adresse adresse) throws IOException {
        String nomHotel = RandomDonneesHotel.randomNomHotel();
        int nombreEtoiles = RandomDonneesHotel.randomNombreEtoiles();
        File imageHotel = RandomDonneesHotel.randomImagePays(adresse.getPays());
        String base64ImageHotel = getImageBase64(imageHotel);

        return new Hotel(nomHotel, adresse, nombreEtoiles, base64ImageHotel);
    }

    public static Adresse generateRandomAdresse() {
        String pays = RandomDonneesHotel.randomPays();
        String ville = RandomDonneesHotel.randomVille(pays);
        String rue = RandomDonneesHotel.randomRue();
        String numero = RandomDonneesHotel.randomNumero();
        String position = RandomDonneesHotel.randomPositionGPS();

        return new Adresse(pays, ville, rue, numero, position);
    }

    public static Chambre generateRandomChambres(Hotel hotel, int numero) throws IOException {
        int nombreLits = RandomDonneesHotel.randomNombreLits();
        int prix = RandomDonneesHotel.randomPrix(hotel.getNombreEtoiles(), nombreLits);
        File imageChambre = RandomDonneesHotel.randomImageChambre(hotel.getNombreEtoiles());
        String base64ImageChambre = getImageBase64(imageChambre);

        return new Chambre(numero, prix, nombreLits, hotel, base64ImageChambre);
    }

    private static String getImageBase64(File imageFile) throws IOException {
        if (imageFile == null) {
            return "";
        }
        byte[] imageBytes = readFileToByteArray(imageFile);

        return Base64.encodeBase64String(imageBytes);
    }

}
