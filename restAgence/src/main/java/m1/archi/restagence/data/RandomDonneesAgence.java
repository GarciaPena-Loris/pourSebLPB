package m1.archi.restagence.data;

import java.util.*;

public class RandomDonneesAgence {
    private static final String[] listeNomAgenceSansDoublons = {
            "Agence du Voyage Étoilé", "Agence Horizon Sans Limite", "Agence de l'Aventure Sans Frontières", "Agence du Monde Magique",
            "Agence des Explorateurs Intrépides", "Agence Terres Lointaines", "Agence des Voyages en Émeraude", "Agence des Rêves Exotiques",
            "Agence de l'Oasis de Découvertes", "Agence de l'Aventure Cachée", "Agence de Voyages au Paradis Terrestre", "Agence des Merveilles Naturelles",
            "Agence de la Découvertes au Clair de Lune", "Agence de l'Exploration Ensoleillée", "Agence des Voyages en Terres Inconnues",
            "Agence de l'Aventure Enchantée", "Agence des Légendes de Voyages", "Agence des Horizons Infinis", "Agence des Explorations Mystérieuses",
            "Agence du Monde Insolite", "Agence des Évasions Secrètes", "Agence des Voyages Surnaturels", "Agence des Rêves d'Ailleurs",
            "Agence des Voyages Fantastiques", "Agence Odyssées Inoubliables", "Agence des Rêves Inexplorés",
            "Agence de l' Aventures Magiques", "Agence de Voyages de l'Imagination", "Agence des Trésors Cachés", "Agence des Lumières de l'Aventure",
            "Agence des Voyages au Royaume des Merveilles", "Agence de l'Exploration Insoupçonnée", "Agence des Rêves d'Évasion",
            "Agence de l'Aventure Infinie", "Agence de Mystères du Monde", "Agence des Voyages Vers l'Inconnu", "Agence des Expériences Uniques"
    };

    private static <T> T getElementListeAleatoire(T[] liste) {
        return liste[new Random().nextInt(liste.length)];
    }

    public static String randomNomAgence() {
        return getElementListeAleatoire(listeNomAgenceSansDoublons);
    }


    public static int randomNombreHotelPartenaire(int nombreHotel) {
        // Entre 30 et le nombre d'hotel
        return new Random().nextInt(nombreHotel - 30) + 30;
    }

    public static List<Long> randomHotelPartenaire(List<Long> listeHotel, int nombreHotel) {
        // Recupere aleatoirement 'nombreHotelPartenaire' hotels de la liste 'listeHotel'
        List<Long> listeHotelPartenaire = new ArrayList<>();
        for (int i = 0; i < randomNombreHotelPartenaire(nombreHotel); i++) {
            Long identifiantHotel = getElementListeAleatoire(listeHotel.toArray(new Long[0]));
            if (!listeHotelPartenaire.contains(identifiantHotel)) {
                listeHotelPartenaire.add(identifiantHotel);
            }
        }
        return listeHotelPartenaire;
    }

    public static int randomReduction() {
        return new Random().nextInt(30) + 5;
    }

    public static Map<Long, Integer> randomReductionHotelPartenaire(List<Long> listeHotel, int nombreHotelPartenaire) {
        Map<Long, Integer> mapReductionHotelPartenaire = new HashMap<>();
        for (Long identifiantHotel : randomHotelPartenaire(listeHotel, nombreHotelPartenaire)) {
            mapReductionHotelPartenaire.put(identifiantHotel, randomReduction());
        }
        return mapReductionHotelPartenaire;
    }

}
