package m1.archi.restcomparateur.data;

import java.util.*;

public class RandomDonneesComparateur {
    private static final String[] listeNomAgenceSansDoublons = {
            "Trivago", "Booking.com", "Expedia", "Kayak", "Hotels.com", "TripAdvisor", "Airbnb"
    };

    private static <T> T getElementListeAleatoire(T[] liste) {
        return liste[new Random().nextInt(liste.length)];
    }

    public static String randomNomComparateur() {
        return getElementListeAleatoire(listeNomAgenceSansDoublons);
    }

}
