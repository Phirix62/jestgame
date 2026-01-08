package jest.vue;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import java.util.List;

/**
 * Interface pour gérer les interactions utilisateur (console ou GUI).
 * Permet aux joueurs physiques de déléguer leurs choix à la vue active.
 */
public interface GestionnaireInteraction {
    
    /**
     * Demande au joueur de choisir quelle carte jouer face cachée.
     * @param joueurNom Nom du joueur
     * @param main Liste des cartes en main
     * @return Carte choisie pour être face cachée
     */
    Carte choisirCarteOffre(String joueurNom, List<Carte> main);
    
    /**
     * Demande au joueur de choisir dans quelle offre prendre une carte.
     * @param joueurNom Nom du joueur
     * @param offres Liste des offres disponibles
     * @return Offre choisie
     */
    Offre choisirOffreCible(String joueurNom, List<Offre> offres);
    
    /**
     * Demande au joueur de choisir quelle carte prendre dans une offre.
     * @param joueurNom Nom du joueur
     * @param offre Offre dans laquelle choisir
     * @return Carte choisie (visible ou cachée)
     */
    Carte choisirCarteDansOffre(String joueurNom, Offre offre);
}
