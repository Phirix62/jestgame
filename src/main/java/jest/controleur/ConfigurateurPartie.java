package jest.controleur;

import jest.modele.joueurs.*;
import jest.modele.extensions.*;

import java.util.*;

/**
 * Classe utilitaire pour configurer une nouvelle partie en mode terminal.
 * Gère l'interaction avec l'utilisateur pour choisir les paramètres du jeu.
 */
public class ConfigurateurPartie {
    private Scanner scanner;

    /**
     * Constructeur du configurateur.
     * 
     * @param scanner Scanner pour lire les entrées utilisateur
     */
    public ConfigurateurPartie(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Configure une nouvelle partie en demandant tous les paramètres à l'utilisateur.
     * 
     * @return Tableau contenant [List&lt;Joueur&gt;, Extension, Variante]
     */
    public Object[] configurerNouvellePartie() {
        int nbJoueurs = choisirNombreJoueurs();
        List<Joueur> joueurs = creerJoueurs(nbJoueurs);
        Extension extension = choisirExtensions(joueurs.size());
        Variante variante = choisirVariante();

        return new Object[]{joueurs, extension, variante};
    }

    /**
     * Demande le nombre de joueurs.
     * 
     * @return Nombre de joueurs (3 ou 4)
     */
    private int choisirNombreJoueurs() {
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
    private List<Joueur> creerJoueurs(int nbJoueurs) {
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
            System.out.println("    3. IA Gloutonne");
            System.out.println("    4. IA Défensive");
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
                    joueur = new JoueurVirtuel(nom, new StrategieGloutonne());
                    break;
                case 4:
                    joueur = new JoueurVirtuel(nom, new StrategieDefensive());
                    break;
                default:
                    joueur = new JoueurPhysique(nom);
            }

            joueurs.add(joueur);
            System.out.println("  ✓ Joueur " + joueur.getNom() + " créé\n");
        }

        return joueurs;
    }

    /**
     * Choisir l'extension à utiliser pour la partie.
     * 
     * @param nbJoueurs Nombre de joueurs (nécessaire pour initialiser l'extension)
     * @return Extension choisie ou null
     */
    private Extension choisirExtensions(int nbJoueurs) {
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
     * Choisir la variante de règles à utiliser pour la partie.
     * 
     * @return Variante choisie
     */
    private Variante choisirVariante() {
        System.out.println("Choisir une variante de règles : ");
        System.out.println("    1. Standard");
        System.out.println("    2. Tactique");
        System.out.println("    3. Partie Rapide");
        System.out.print("  Choix (1-3) : ");

        int choix = lireChoix(1, 3);
        switch (choix) {
            case 2:
                return new VarianteTactique();
            case 3:
                return new VarianteRapide();
            case 1:
            default:
                return new VarianteStandard();
        }
    }

    /**
     * Lit un choix valide entre min et max.
     * 
     * @param min Valeur minimale
     * @param max Valeur maximale
     * @return Choix validé
     */
    private int lireChoix(int min, int max) {
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
