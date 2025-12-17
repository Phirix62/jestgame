package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import java.util.List;
import java.util.Scanner;

/**
 * Représente un joueur humain qui interagit via la console.
 */
public class JoueurPhysique extends Joueur {
    private Scanner scanner;
    
    /**
     * Constructeur de JoueurPhysique.
     * @param nom Nom du joueur
     */
    public JoueurPhysique(String nom) {
        super(nom);
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Constructeur avec scanner personnalisé (pour tests).
     * @param nom Nom du joueur
     * @param scanner Scanner à utiliser
     */
    public JoueurPhysique(String nom, Scanner scanner) {
        super(nom);
        this.scanner = scanner;
    }
    
    @Override
    public Carte choisirCarteOffre(List<Carte> main) {
        System.out.println("\n=== " + nom + ", créez votre offre ===");
        System.out.println("Vos " + main.size() + " cartes :");
        for (int i = 0; i < main.size(); i++) {
            System.out.println((i + 1) + ". " + main.get(i));
        }
        
        System.out.print("Choisissez la carte à jouer FACE CACHÉE (1-" + main.size() + ") : ");
        int choix = lireChoix(1, main.size());
        
        return main.get(choix - 1);
    }
    
    @Override
    public Offre choisirOffreCible(List<Offre> offres) {
        System.out.println("\n=== " + nom + ", choisissez une offre ===");
        System.out.println("Offres disponibles :");
        for (int i = 0; i < offres.size(); i++) {
            Offre o = offres.get(i);
            System.out.println((i + 1) + ". Offre de " + o.getProprietaire().getNom());
            for (int j = 0; j < o.getNombreCartesVisibles(); j++) {
            System.out.println(" - Carte visible: " + o.getCartesVisibles().get(j));
            }
            System.out.println(" - Carte cachée: ???");                 
        }
        
        System.out.print("Choisissez une offre (1-" + offres.size() + ") : ");
        int choix = lireChoix(1, offres.size());
        
        return offres.get(choix - 1);
    }
    
    @Override
    public Carte choisirCarteDansOffre(Offre offre) {
        int totalCartes = offre.getNombreCartesVisibles() + 1;
        System.out.println("\n=== " + nom + ", choisissez une carte dans l'offre ===");
        System.out.println("Offre de " + offre.getProprietaire().getNom() + " :");
        for (int j = 0; j < totalCartes - 1; j++) {
            System.out.println(" - Carte visible: " + offre.getCartesVisibles().get(j));
            }
        System.out.println(" - Carte cachée: ???");
        
        System.out.print("Choisissez une carte (1-" + totalCartes + ") : ");
        int choix = lireChoix(1, totalCartes);
        
        return choix == 1 ? offre.getCarteVisible() : offre.getCarteCachee();
    }
    
    /**
     * Lit un choix valide entre min et max.
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