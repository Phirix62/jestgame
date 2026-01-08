package jest.controleur;

import jest.modele.jeu.Partie;
import jest.modele.joueurs.Joueur;
import jest.modele.extensions.Extension;
import jest.modele.extensions.Variante;
import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;

import java.io.IOException;
import java.util.List;

/**
 * Contrôleur principal du jeu Jest (pattern MVC).
 * Coordonne les interactions entre le modèle (Partie) et les vues (Terminal/Graphique).
 * Gère les actions utilisateur et synchronise les vues.
 */
public class ControleurJeu {
    private Partie partie;
    
    /**
     * Constructeur du contrôleur.
     */
    public ControleurJeu() {
        this.partie = new Partie();
    }
    
    /**
     * Ajoute une vue observatrice à la partie.
     * @param observateur Vue à ajouter
     */
    public void ajouterObservateur(ObservateurPartie observateur) {
        partie.ajouterObservateur(observateur);
    }
    
    /**
     * Retire une vue observatrice.
     * @param observateur Vue à retirer
     */
    public void retirerObservateur(ObservateurPartie observateur) {
        partie.retirerObservateur(observateur);
    }
    
    /**
     * Initialise une nouvelle partie.
     * @param joueurs Liste des joueurs
     * @param extension Extension choisie (null si aucune)
     * @param variante Variante choisie
     */
    public void initialiserPartie(List<Joueur> joueurs, Extension extension, Variante variante) {
        partie.initialiser(joueurs, extension, variante);
    }
    
    /**
     * Exécute le prochain tour de la partie.
     * @return true si le tour s'est bien déroulé
     */
    public boolean executerProchainTour() {
        return partie.executerProchainTour();
    }
    
    /**
     * Vérifie si la partie est terminée.
     * @return true si terminée
     */
    public boolean estPartieTerminee() {
        return partie.estTerminee();
    }
    
    /**
     * Affiche les résultats finaux de la partie.
     */
    public void afficherResultatsFinaux() {
        partie.afficherResultatsFinaux();
    }
    
    /**
     * Sauvegarde la partie en cours.
     * @param nomSauvegarde Nom de la sauvegarde
     * @throws IOException Si erreur lors de la sauvegarde
     */
    public void sauvegarderPartie(String nomSauvegarde) throws IOException {
        partie.sauvegarder(nomSauvegarde);
    }
    
    /**
     * Charge une partie sauvegardée.
     * @param nomSauvegarde Nom de la sauvegarde
     * @throws IOException Si erreur de lecture
     * @throws ClassNotFoundException Si classe non trouvée
     */
    public void chargerPartie(String nomSauvegarde) throws IOException, ClassNotFoundException {
        this.partie = Partie.charger(nomSauvegarde);
        // Note: Les observateurs doivent être réattachés après le chargement
    }
    
    /**
     * Liste toutes les sauvegardes disponibles.
     * @return Liste des noms de sauvegardes
     */
    public List<String> listerSauvegardes() {
        return Partie.listerSauvegardes();
    }
    
    /**
     * Supprime une sauvegarde.
     * @param nomSauvegarde Nom de la sauvegarde à supprimer
     * @return true si supprimée avec succès
     */
    public boolean supprimerSauvegarde(String nomSauvegarde) {
        return Partie.supprimerSauvegarde(nomSauvegarde);
    }
    
    /**
     * Retourne la partie en cours.
     * @return Partie
     */
    public Partie getPartie() {
        return partie;
    }
    
    /**
     * Retourne la liste des joueurs.
     * @return Liste des joueurs
     */
    public List<Joueur> getJoueurs() {
        return partie.getJoueurs();
    }
    
    /**
     * Retourne le tour actuel.
     * @return Numéro du tour actuel
     */
    public int getTourActuel() {
        return partie.getTourActuel();
    }
}
