package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import java.util.List;

/**
 * Interface définissant le contrat pour les stratégies de jeu des joueurs virtuels.
 * Pattern Strategy : permet de changer le comportement des IA sans modifier JoueurVirtuel.
 */
public interface StrategieJeu {
    
    /**
     * Choisit quelle carte jouer face cachée dans l'offre.
     * @param main Cartes en main
     * @param jest Jest actuel du joueur (pour contexte)
     * @return Carte choisie
     */
    Carte choisirCarteOffre(List<Carte> main, Jest jest);
    
    /**
     * Choisit de quelle offre prendre une carte.
     * @param offres Offres disponibles
     * @param jest Jest actuel du joueur
     * @return Offre choisie
     */
    Offre choisirOffreCible(List<Offre> offres, Jest jest);
    
    /**
     * Choisit quelle carte prendre dans une offre.
     * @param offre Offre dans laquelle piocher
     * @param jest Jest actuel du joueur
     * @return Carte choisie
     */
    Carte choisirCarteDansOffre(Offre offre, Jest jest);
}