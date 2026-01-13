package jest.controleur;

import java.util.Scanner;

/**
 * Utilitaire qui gère la lecture et validation des entrées utilisateur
 */
public class LecteurEntree {
    private Scanner scanner;
    
    /**
     * Constructeur avec un scanner existant.
     * @param scanner Scanner à utiliser pour la lecture
     */
    public LecteurEntree(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Constructeur par défaut (crée un nouveau scanner sur System.in).
     */
    public LecteurEntree() {
        this(new Scanner(System.in));
    }
    
    /**
     * Lit un choix valide entre min et max.
     * Redemande tant que l'entrée est invalide.
     * @param min Valeur minimale acceptée
     * @param max Valeur maximale acceptée
     * @return Choix valide de l'utilisateur
     */
    public int lireChoix(int min, int max) {
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
    
    /**
     * Lit une réponse oui/non.
     * 
     * @param message Message à afficher
     * @return true si l'utilisateur répond "o" ou "oui"
     */
    public boolean lireOuiNon(String message) {
        System.out.print(message);
        String reponse = scanner.nextLine().trim().toLowerCase();
        return reponse.equals("o") || reponse.equals("oui");
    }
    
    /**
     * Lit une ligne de texte.
     * 
     * @param message Message à afficher avant la lecture
     * @return Ligne lue 
     */
    public String lireLigne(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
    
    /**
     * Ferme le scanner.
     */
    public void fermer() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
