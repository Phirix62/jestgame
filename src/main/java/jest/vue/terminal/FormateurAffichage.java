package jest.vue.terminal;

import jest.modele.joueurs.Joueur;
import jest.modele.cartes.Carte;
import jest.modele.cartes.Trophee;
import jest.modele.extensions.Extension;

import java.util.List;
import java.util.Map;

/**
 * Utilitaire pour formater l'affichage console.
 */
public class FormateurAffichage {
    
    /**
     * Formate un titre avec bordure.
     */
    public static String formaterTitre(String titre) {
        String bordure = "═".repeat(35);
        return "\n" + bordure + "\n" + centrer(titre, 35) + "\n" + bordure;
    }
    
    /**
     * Formate un sous-titre.
     */
    public static String formaterSousTitre(String sousTitre) {
        return "\n--- " + sousTitre + " ---";
    }
    
    /**
     * Centre un texte.
     */
    public static String centrer(String texte, int largeur) {
        int padding = (largeur - texte.length()) / 2;
        return " ".repeat(Math.max(0, padding)) + texte;
    }
    
    /**
     * Formate les détails d'initialisation.
     */
    public static String formaterDetailsInitialisation(Extension extension, 
                                                       List<Trophee> trophees, 
                                                       int taillePioche) {
        StringBuilder sb = new StringBuilder();
        
        if (extension != null) {
            sb.append("\nExtension utilisée : ").append(extension.getNom());
            sb.append("\n").append(extension.getDescription());
        }
        
        sb.append("\n\nTrophées en jeu :");
        for (Trophee t : trophees) {
            sb.append("\n  ").append(t);
        }
        
        sb.append("\n\nPioche : ").append(taillePioche).append(" cartes");
        
        return sb.toString();
    }
    
    /**
     * Formate les récupérations de cartes.
     */
    public static String formaterRecuperations(Map<Joueur, Carte> recuperations) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nRécupération des dernières cartes...");
        for (Map.Entry<Joueur, Carte> entry : recuperations.entrySet()) {
            sb.append("\n").append(entry.getKey().getNom())
              .append(" récupère ").append(entry.getValue().toStringCourt());
        }
        return sb.toString();
    }
    
    /**
     * Formate la révélation des Jests.
     */
    public static String formaterRevelationJests(List<Joueur> joueurs) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Révélation des Jests ---");
        for (Joueur joueur : joueurs) {
            sb.append("\n").append(joueur.getNom()).append(" : ")
              .append(joueur.getJest().afficherDetails());
        }
        return sb.toString();
    }
    
    /**
     * Formate les scores avec détails.
     */
    public static String formaterScores(Map<Joueur, Integer> scores, 
                                        Map<Joueur, String> details, 
                                        String titre) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(titre);
        for (Map.Entry<Joueur, Integer> entry : scores.entrySet()) {
            Joueur joueur = entry.getKey();
            sb.append("\n").append(joueur.getNom()).append(" : ")
              .append(entry.getValue()).append(" points");
            if (details != null && details.containsKey(joueur)) {
                sb.append("\n").append(details.get(joueur));
            }
        }
        return sb.toString();
    }
    
    /**
     * Formate l'attribution d'un trophée.
     */
    public static String formaterAttributionTrophee(Trophee trophee, Joueur gagnant) {
        StringBuilder sb = new StringBuilder();
        sb.append("Trophée ").append(trophee.getCondition().getDescription())
          .append(" (").append(trophee.toStringCourt()).append(") ");
        
        if (gagnant != null) {
            sb.append(" --> ").append(gagnant.getNom());
        } else {
            sb.append(" --> Aucun gagnant");
        }
        
        return sb.toString();
    }
}
