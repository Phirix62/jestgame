package jest.vue;

import jest.controleur.ControleurJeu;
import jest.modele.joueurs.Joueur;
import jest.modele.extensions.Extension;
import jest.modele.extensions.Variante;

import java.util.List;

/**
 * Interface commune pour toutes les vues du jeu Jest.
 * Définit les méthodes que toute vue doit implémenter.
 */
public interface Vue {
    
    /**
     * Initialise la vue.
     * @param controleur Contrôleur associé à la vue
     */
    void initialiser(ControleurJeu controleur);
    
    /**
     * Démarre la vue (affichage ou lancement).
     */
    void demarrer();
    
    /**
     * Arrête la vue.
     */
    void arreter();
    
    /**
     * Configure une nouvelle partie (collecte des informations).
     * @return Tableau contenant [joueurs, extension, variante]
     */
    Object[] configurerNouvellePartie();
    
    /**
     * Demande le nom d'une sauvegarde à charger.
     * @param sauvegardes Liste des sauvegardes disponibles
     * @return Nom de la sauvegarde choisie, ou null si annulé
     */
    String choisirSauvegarde(List<String> sauvegardes);
    
    /**
     * Demande le nom pour une nouvelle sauvegarde.
     * @return Nom de la sauvegarde
     */
    String demanderNomSauvegarde();
    
    /**
     * Affiche un message à l'utilisateur.
     * @param message Message à afficher
     */
    void afficherMessage(String message);
    
    /**
     * Affiche une erreur à l'utilisateur.
     * @param erreur Message d'erreur
     */
    void afficherErreur(String erreur);
}
