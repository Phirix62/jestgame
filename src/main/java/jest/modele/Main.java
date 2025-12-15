package jest.modele;

import jest.modele.jeu.Partie;
import jest.modele.joueurs.*;
import jest.modele.extensions.*;

import java.util.*;

/**
 * Classe principale - Interface en ligne de commande pour Jest.
 * Permet de configurer et lancer une partie en mode console.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        afficherBanniere();

        boolean continuer = true;
        while (continuer) {
            Partie partie = menuPrincipal();

            if (partie == null) {
                continuer = false;
            } else {
                jouerPartie(partie);
                continuer = demanderRejouer();
            }
        }

        System.out.println("\nMerci d'avoir joué à Jest !");
        scanner.close();
    }

    /**
     * Affiche la bannière du jeu.
     */
    private static void afficherBanniere() {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║                                   ║");
        System.out.println("║                JEST               ║");
        System.out.println("║                                   ║");
        System.out.println("║      Jeu de cartes stratégique    ║");
        System.out.println("║                                   ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();
    }

    /**
     * Affiche le menu principal et retourne la partie à jouer.
     * 
     * @return Partie à jouer ou null pour quitter
     */
    private static Partie menuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===\n");
        System.out.println("1. Nouvelle partie");
        System.out.println("2. Charger une partie");
        System.out.println("3. Quitter");
        System.out.print("\nChoix : ");

        int choix = lireChoix(1, 3);

        switch (choix) {
            case 1:
                return nouvellePartie();
            case 2:
                return chargerPartie();
            case 3:
                return null;
            default:
                return null;
        }
    }

    /**
     * Gère la boucle de jeu avec options de sauvegarde.
     * 
     * @param partie Partie à jouer
     */
    private static void jouerPartie(Partie partie) {
        while (!partie.estTerminee()) {
            // Menu d'options avant chaque tour
            System.out.println("\n--- Options ---");
            System.out.println("1. Continuer le jeu");
            System.out.println("2. Sauvegarder et continuer");
            System.out.println("3. Sauvegarder et quitter");
            System.out.print("Choix : ");

            int choix = lireChoix(1, 3);

            // Sauvegarder si demandé
            if (choix == 2 || choix == 3) {
                if (sauvegarderPartie(partie)) {
                    if (choix == 3) {
                        System.out.println("\nPartie sauvegardée. À bientôt !");
                        return;
                    }
                }
            }

            // Exécuter le tour
            partie.executerProchainTour();
        }

        // Afficher les résultats finaux
        partie.afficherResultatsFinaux();
    }

    /**
     * Crée et configure une nouvelle partie.
     * 
     * @return Nouvelle partie initialisée
     */
    private static Partie nouvellePartie() {
        int nbJoueurs = choisirNombreJoueurs();
        List<Joueur> joueurs = creerJoueurs(nbJoueurs);
        Extension extension = choisirExtensions(joueurs.size());

        Partie partie = new Partie();
        partie.initialiser(joueurs, extension);

        System.out.println("\nPartie initialisée avec succès !\n");
        return partie;
    }

    /**
     * Charge une partie sauvegardée.
     * 
     * @return Partie chargée ou null si annulé
     */
    private static Partie chargerPartie() {
        List<String> sauvegardes = Partie.listerSauvegardes();

        if (sauvegardes.isEmpty()) {
            System.out.println("\nAucune sauvegarde disponible.");
            return null;
        }

        System.out.println("\n=== SAUVEGARDES DISPONIBLES ===\n");
        for (int i = 0; i < sauvegardes.size(); i++) {
            System.out.println((i + 1) + ". " + sauvegardes.get(i));
        }
        System.out.println((sauvegardes.size() + 1) + ". Annuler");

        System.out.print("\nChoix : ");
        int choix = lireChoix(1, sauvegardes.size() + 1);

        if (choix == sauvegardes.size() + 1) {
            return null;
        }

        try {
            String nomFichier = sauvegardes.get(choix - 1);
            Partie partie = Partie.charger(nomFichier);
            System.out.println("\nPartie chargée avec succès !");
            System.out.println("Reprise au tour " + partie.getTourActuel() + "\n");
            return partie;
        } catch (Exception e) {
            System.out.println("\nErreur lors du chargement : " + e.getMessage());
            return null;
        }
    }

    /**
     * Sauvegarde la partie en cours.
     * 
     * @param partie Partie à sauvegarder
     * @return true si sauvegarde réussie
     */
    private static boolean sauvegarderPartie(Partie partie) {
        System.out.print("\nNom de la sauvegarde : ");
        String nom = scanner.nextLine().trim();

        if (nom.isEmpty()) {
            nom = "partie_" + System.currentTimeMillis();
        }

        try {
            partie.sauvegarder(nom);
            System.out.println("✓ Partie sauvegardée avec succès !");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Erreur lors de la sauvegarde : " + e.getMessage());
            return false;
        }
    }

    /**
     * Demande le nombre de joueurs.
     * 
     * @return Nombre de joueurs (3 ou 4)
     */
    private static int choisirNombreJoueurs() {
        System.out.println("=== CONFIGURATION ===\n");
        System.out.print("Nombre de joueurs (3 ou 4) : ");

        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int nb = Integer.parseInt(input);
                if (nb == 3 || nb == 4) {
                    return nb;
                }
                System.out.print("Veuillez entrer 3 ou 4 : ");
            } catch (NumberFormatException e) {
                System.out.print("Entrée invalide. Nombre de joueurs (3 ou 4) : ");
            }
        }
    }

    /**
     * Crée la liste des joueurs selon la configuration.
     * 
     * @param nbJoueurs Nombre total de joueurs
     * @return Liste des joueurs créés
     */
    private static List<Joueur> creerJoueurs(int nbJoueurs) {
        List<Joueur> joueurs = new ArrayList<>();

        System.out.println("\n=== CRÉATION DES JOUEURS ===\n");

        for (int i = 1; i <= nbJoueurs; i++) {
            System.out.println("Joueur " + i + " :");
            System.out.print("  Nom : ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) {
                nom = "Joueur" + i;
            }

            System.out.println("  Type :");
            System.out.println("    1. Humain");
            System.out.println("    2. IA Aléatoire");
            System.out.println("    3. IA Gloutonne (à implémenter)");
            System.out.println("    4. IA Défensive (à implémenter)");
            System.out.print("  Choix (1-4) : ");

            int choix = lireChoix(1, 4);

            Joueur joueur;
            switch (choix) {
                case 1:
                    joueur = new JoueurPhysique(nom);
                    break;
                case 2:
                    joueur = new JoueurVirtuel(nom, new StrategieAleatoire());
                    break;
                case 3:
                    // TODO: Implémenter StrategieGloutonne
                    System.out.println("  [IA Gloutonne non implémentée, utilisation IA Aléatoire]");
                    joueur = new JoueurVirtuel(nom, new StrategieAleatoire());
                    break;
                case 4:
                    // TODO: Implémenter StrategieDefensive
                    System.out.println("  [IA Défensive non implémentée, utilisation IA Aléatoire]");
                    joueur = new JoueurVirtuel(nom, new StrategieAleatoire());
                    break;
                default:
                    joueur = new JoueurPhysique(nom);
            }

            joueurs.add(joueur);
            System.out.println("  joueur " + joueur.getNom() + " créé\n");
        }

        return joueurs;
    }

    /**
     * Demande si les extensions doivent être activées.
     * 
     * @return true si extensions activées
     */
    private static Extension choisirExtensions(int nbJoueurs) {
        System.out.println("Choisir une extension : ");
        System.out.println("    1. Sans extension");
        System.out.println("    2. Extension Magique");
        System.out.println("    3. Autres extensions à venir...");
        System.out.print("  Choix (1-3) : ");

        int choix = lireChoix(1, 3);
        if (choix == 2) {
            ExtensionMagique extension = new ExtensionMagique(nbJoueurs);
            return extension;
        }
        return null;
    }

    /**
     * Demande si le joueur veut rejouer.
     * 
     * @return true si rejouer
     */
    private static boolean demanderRejouer() {
        System.out.print("\nRejouer ? (o/N) : ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        return reponse.equals("o") || reponse.equals("oui");
    }

    /**
     * Lit un choix valide entre min et max.
     * 
     * @param min Valeur minimale
     * @param max Valeur maximale
     * @return Choix validé
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