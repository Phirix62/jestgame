package jest.controleur;

import jest.vue.Vue;
import jest.modele.joueurs.Joueur;
import jest.modele.extensions.Extension;
import jest.modele.extensions.Variante;

import java.util.List;

/**
 * Gère la boucle de jeu principale et les menus.
 */
public class GestionnaireBoucleJeu {
    private ControleurJeu controleur;
    private LecteurEntree lecteur;
    private boolean enCours;
    
    /**
     * Constructeur du gestionnaire de boucle.
     * 
     * @param controleur Contrôleur principal (accès au modèle)
     * @param lecteur Lecteur d'entrées utilisateur
     */
    public GestionnaireBoucleJeu(ControleurJeu controleur, LecteurEntree lecteur) {
        this.controleur = controleur;
        this.lecteur = lecteur;
        this.enCours = false;
    }
    
    /**
     * Démarre la boucle de jeu principale (menu + parties).
     * 
     * LOGIQUE :
     * 1. Affiche le menu principal
     * 2. Gère les choix (nouvelle partie, charger, quitter)
     * 3. Lance les parties jusqu'à ce que l'utilisateur quitte
     * 
     * @param vueTerminal Vue terminal pour l'interaction
     * @param vueGraphique Vue graphique (peut être null)
     */
    public void demarrer(Vue vueTerminal, Vue vueGraphique) {
        this.enCours = true;
        
        while (enCours) {
            try {
                int choix = afficherMenuPrincipal();
                
                switch (choix) {
                    case 1:
                        gererNouvellePartie(vueTerminal, vueGraphique);
                        break;
                    case 2:
                        gererChargementPartie(vueTerminal, vueGraphique);
                        break;
                    case 3:
                        enCours = false;
                        break;
                    default:
                        enCours = false;
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("\nMerci d'avoir joué à Jest !");
    }
    
    /**
     * Affiche le menu principal et retourne le choix de l'utilisateur.
     * @return Choix de l'utilisateur (1-3)
     */
    private int afficherMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===\n");
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Charger une partie");
        System.out.println("3. Quitter");
        System.out.print("\nChoix : ");
        
        return lecteur.lireChoix(1, 3);
    }
    
    /**
     * Gère la création et le lancement d'une nouvelle partie.
     * @param vueTerminal Vue terminal
     * @param vueGraphique Vue graphique (peut être null)
     */
    private void gererNouvellePartie(Vue vueTerminal, Vue vueGraphique) {
        // 1. Demander configuration via la vue terminal
        Object[] config = vueTerminal.configurerNouvellePartie();
        
        if (config != null) {
            // 2. Extraire les paramètres
            @SuppressWarnings("unchecked")
            List<Joueur> joueurs = (List<Joueur>) config[0];
            Extension extension = (Extension) config[1];
            Variante variante = (Variante) config[2];
            
            // 3. Initialiser la partie via le contrôleur
            controleur.initialiserPartie(joueurs, extension, variante);
            System.out.println("\nPartie initialisée avec succès !\n");
            
            // 4. Lancer la boucle de jeu
            jouerPartie(vueTerminal, vueGraphique);
            
            // 5. Demander si rejouer
            if (lecteur.lireOuiNon("\nRejouer ? (o/N) : ")) {
                enCours = true;
            } else {
                enCours = false;
            }
        }
    }
    
    /**
     * Gère le chargement d'une partie sauvegardée.
     * 
     * @param vueTerminal Vue terminal
     * @param vueGraphique Vue graphique (peut être null)
     */
    private void gererChargementPartie(Vue vueTerminal, Vue vueGraphique) {
        // 1. Lister les sauvegardes disponibles
        List<String> sauvegardes = controleur.listerSauvegardes();
        String nomSauvegarde = vueTerminal.choisirSauvegarde(sauvegardes);
        
        if (nomSauvegarde != null) {
            try {
                // 2. Charger la partie
                controleur.chargerPartie(nomSauvegarde);
                System.out.println("\nPartie chargée avec succès !");
                System.out.println("Reprise au tour " + controleur.getTourActuel() + "\n");
                
                // 3. Réattacher les observateurs (important après désérialisation)
                controleur.ajouterObservateur((ObservateurPartie) vueTerminal);
                if (vueGraphique != null) {
                    controleur.ajouterObservateur((ObservateurPartie) vueGraphique);
                }
                
                // 4. Reprendre le jeu
                jouerPartie(vueTerminal, vueGraphique);
                
                // 5. Demander si rejouer
                if (lecteur.lireOuiNon("\nRejouer ? (o/N) : ")) {
                    enCours = true;
                } else {
                    enCours = false;
                }
            } catch (Exception e) {
                System.out.println("\nErreur lors du chargement : " + e.getMessage());
            }
        }
    }
    
    /**
     * Boucle de jeu pour une partie en cours.
     * Gère les tours et les options (sauvegarder, continuer, quitter).
     * @param vueTerminal Vue terminal
     * @param vueGraphique Vue graphique (peut être null)
     */
    private void jouerPartie(Vue vueTerminal, Vue vueGraphique) {
        while (!controleur.estPartieTerminee()) {
            // Menu d'options avant chaque tour
            System.out.println("\n--- Options ---");
            System.out.println("1. Continuer le jeu");
            System.out.println("2. Sauvegarder et continuer");
            System.out.println("3. Sauvegarder et quitter");
            System.out.print("Choix : ");
            
            int choix = lecteur.lireChoix(1, 3);
            
            // Sauvegarder si demandé
            if (choix == 2 || choix == 3) {
                String nom = vueTerminal.demanderNomSauvegarde();
                try {
                    controleur.sauvegarderPartie(nom);
                    System.out.println("\nPartie sauvegardée avec succès !");
                    if (choix == 3) {
                        System.out.println("À bientôt !");
                        return; // Sortir de la boucle de jeu
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
                }
            }
            
            // Exécuter le tour (délégation au contrôleur → modèle)
            controleur.executerProchainTour();
        }
        
        // Afficher les résultats finaux
        controleur.afficherResultatsFinaux();
    }
}
