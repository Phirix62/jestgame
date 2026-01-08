package jest.modele;

import jest.controleur.ControleurJeu;
import jest.vue.*;
import jest.modele.joueurs.Joueur;
import jest.modele.joueurs.JoueurPhysique;
import jest.modele.extensions.Extension;
import jest.modele.extensions.Variante;

import java.util.List;
import java.util.Scanner;

/**
 * Lance le jeu Jest avec terminal et interface graphique simultanés.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ControleurJeu controleur = new ControleurJeu();

        VueTerminal vueTerminal = new VueTerminal();
        VueGraphique vueGraphique = new VueGraphique();

        vueTerminal.initialiser(controleur);
        vueGraphique.initialiser(controleur);

        vueTerminal.demarrer();
        vueGraphique.demarrer();
        
        // Configure les deux interfaces pour fonctionner simultanément
        System.out.println("\n Les deux interfaces sont actives simultanément !");
        System.out.println("  Vous pouvez répondre via le terminal OU la GUI - la première réponse compte.\n");
        
        GestionnaireConcurrent gestionnaireConcurrent = 
            new GestionnaireConcurrent(vueTerminal, vueGraphique);
        JoueurPhysique.setGestionnaireInteraction(gestionnaireConcurrent);

        // Boucle de jeu principale
        boolean continuer = true;
        while (continuer) {
            try {
                int choix = menuPrincipal();

                switch (choix) {
                    case 1:
                        nouvellePartie(controleur, vueTerminal);
                        jouerPartie(controleur, vueTerminal);
                        continuer = demanderRejouer();
                        break;
                    case 2:
                        chargerPartie(controleur, vueTerminal);
                        if (controleur.getPartie() != null) {
                            // Réattacher les observateurs après chargement
                            controleur.ajouterObservateur(vueTerminal);
                            controleur.ajouterObservateur(vueGraphique);
                            jouerPartie(controleur, vueTerminal);
                            continuer = demanderRejouer();
                        }
                        break;
                    case 3:
                        continuer = false;
                        break;
                    default:
                        continuer = false;
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("\nMerci d'avoir joué à Jest !");
        vueTerminal.arreter();
        vueGraphique.arreter();
    }

    /**
     * Affiche le menu principal et retourne le choix.
     */
    private static int menuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===\n");
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Charger une partie");
        System.out.println("3. Quitter");
        System.out.print("\nChoix : ");

        return lireChoix(1, 3);
    }

    /**
     * Crée et initialise une nouvelle partie.
     */
    private static void nouvellePartie(ControleurJeu controleur, VueTerminal vue) {
        Object[] config = vue.configurerNouvellePartie();
        if (config != null) {
            @SuppressWarnings("unchecked")
            List<Joueur> joueurs = (List<Joueur>) config[0];
            Extension extension = (Extension) config[1];
            Variante variante = (Variante) config[2];

            controleur.initialiserPartie(joueurs, extension, variante);
            System.out.println("\nPartie initialisée avec succès !\n");
        }
    }

    /**
     * Charge une partie sauvegardée.
     */
    private static void chargerPartie(ControleurJeu controleur, VueTerminal vue) {
        List<String> sauvegardes = controleur.listerSauvegardes();
        String nomSauvegarde = vue.choisirSauvegarde(sauvegardes);

        if (nomSauvegarde != null) {
            try {
                controleur.chargerPartie(nomSauvegarde);
                System.out.println("\nPartie chargée avec succès !");
                System.out.println("Reprise au tour " + controleur.getTourActuel() + "\n");
            } catch (Exception e) {
                System.out.println("\nErreur lors du chargement : " + e.getMessage());
            }
        }
    }

    /**
     * Gère la boucle de jeu avec options de sauvegarde.
     */
    private static void jouerPartie(ControleurJeu controleur, VueTerminal vue) {
        while (!controleur.estPartieTerminee()) {
            // Menu d'options avant chaque tour
            System.out.println("\n--- Options ---");
            System.out.println("1. Continuer le jeu");
            System.out.println("2. Sauvegarder et continuer");
            System.out.println("3. Sauvegarder et quitter");
            System.out.print("Choix : ");

            int choix = lireChoix(1, 3);

            // Sauvegarder si demandé
            if (choix == 2 || choix == 3) {
                String nom = vue.demanderNomSauvegarde();
                try {
                    controleur.sauvegarderPartie(nom);
                    if (choix == 3) {
                        System.out.println("\nPartie sauvegardée. À bientôt !");
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
                }
            }

            // Exécuter le tour
            controleur.executerProchainTour();
        }

        // Afficher les résultats finaux
        controleur.afficherResultatsFinaux();
    }

    /**
     * Demande si le joueur veut rejouer.
     */
    private static boolean demanderRejouer() {
        System.out.print("\nRejouer ? (o/N) : ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        return reponse.equals("o") || reponse.equals("oui");
    }

    /**
     * Lit un choix valide entre min et max.
     */
    private static int lireChoix(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choix = Integer.parseInt(input);
                if (choix >= min && choix <= max) {
                    return choix;
                }
                System.out.print("Choix invalide. Entrez un nombre entre " + min + " et " + max + " : ");
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Entrez un nombre : ");
            }
        }
    }
}