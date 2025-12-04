package jest.modele;
import jest.modele.jeu.Partie;
import jest.modele.joueurs.*;
import java.util.*;

/**
 * Classe principale - Interface en ligne de commande pour Jest.
 * Permet de configurer et lancer une partie en mode console.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        afficherBanniere();
        
        // Configuration de la partie
        int nbJoueurs = choisirNombreJoueurs();
        List<Joueur> joueurs = creerJoueurs(nbJoueurs);
        boolean avecExtension = choisirExtensions();
        
        // Créer et initialiser la partie
        Partie partie = new Partie();
        partie.initialiser(joueurs, avecExtension);
        
        // Démarrer la partie
        partie.demarrer();
        
        // Demander si rejouer
        if (demanderRejouer()) {
            main(args); // Relancer
        } else {
            System.out.println("\nMerci d'avoir joué à Jest !");
            scanner.close();
        }
    }
    
    /**
     * Affiche la bannière du jeu.
     */
    private static void afficherBanniere() {
        System.out.println("\n");
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║                                   ║");
        System.out.println("║            🃏 JEST 🃏             ║");
        System.out.println("║                                   ║");
        System.out.println("║      Jeu de cartes stratégique    ║");
        System.out.println("║                                   ║");
        System.out.println("╚═══════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Demande le nombre de joueurs.
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
            System.out.println("  ✓ " + joueur.getNom() + " créé\n");
        }
        
        return joueurs;
    }
    
    /**
     * Demande si les extensions doivent être activées.
     * @return true si extensions activées
     */
    private static boolean choisirExtensions() {
        System.out.print("Activer les extensions ? (o/N) : ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        boolean activer = reponse.equals("o") || reponse.equals("oui");
        
        if (activer) {
            System.out.println("[Extensions non encore implémentées]");
        }
        
        return false; // Toujours false pour l'instant
    }
    
    /**
     * Demande si le joueur veut rejouer.
     * @return true si rejouer
     */
    private static boolean demanderRejouer() {
        System.out.print("\nRejouer ? (o/N) : ");
        String reponse = scanner.nextLine().trim().toLowerCase();
        return reponse.equals("o") || reponse.equals("oui");
    }
    
    /**
     * Lit un choix valide entre min et max.
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