package jest;

import jest.controleur.ControleurJeu;
import jest.controleur.GestionnaireBoucleJeu;
import jest.controleur.LecteurEntree;
import jest.vue.*;
import jest.vue.terminal.VueTerminal;
import jest.vue.graphique.VueGraphique;
import jest.modele.joueurs.JoueurPhysique;

/**
 * Point d'entrée de l'application Jest.
 * instanciation et connexion des composants MVC.
 */
public class Main {
    
    /**
     * Méthode principale : lance l'application Jest.
     */
    public static void main(String[] args) {
        ControleurJeu controleur = new ControleurJeu();
        
        VueTerminal vueTerminal = new VueTerminal();
        VueGraphique vueGraphique = new VueGraphique();
        
        vueTerminal.initialiser(controleur);
        vueGraphique.initialiser(controleur);

        vueTerminal.demarrer();
        vueGraphique.demarrer();
        
        GestionnaireConcurrent gestionnaireConcurrent = 
            new GestionnaireConcurrent(vueTerminal, vueGraphique);
        JoueurPhysique.setGestionnaireInteraction(gestionnaireConcurrent);
        
        LecteurEntree lecteur = new LecteurEntree();
        GestionnaireBoucleJeu gestionnaireBoucle = 
            new GestionnaireBoucleJeu(controleur, lecteur);
        
        // Lancer la boucle de jeu principale
        gestionnaireBoucle.demarrer(vueTerminal, vueGraphique);
        
        // Nettoyer les ressources à la fin
        lecteur.fermer();
        vueTerminal.arreter();
        vueGraphique.arreter();
    }
}