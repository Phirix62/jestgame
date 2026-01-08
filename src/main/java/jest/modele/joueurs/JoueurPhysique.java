package jest.modele.joueurs;

import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;
import jest.vue.GestionnaireInteraction;
import java.util.List;
import java.util.Scanner;

/**
 * Joueur humain qui interagit via la console ou la GUI.
 */
public class JoueurPhysique extends Joueur {
    private Scanner scanner;
    
    // Variable static = partagée par tous les JoueurPhysique
    private static GestionnaireInteraction gestionnaireInteraction;
    
    public JoueurPhysique(String nom) {
        super(nom);
        this.scanner = new Scanner(System.in);
    }
    
    public JoueurPhysique(String nom, Scanner scanner) {
        super(nom);
        this.scanner = scanner;
    }
    
    public static void setGestionnaireInteraction(GestionnaireInteraction gestionnaire) {
        gestionnaireInteraction = gestionnaire;
    }
    
    @Override
    public Carte choisirCarteOffre(List<Carte> main) {
        if (gestionnaireInteraction != null) {
            return gestionnaireInteraction.choisirCarteOffre(nom, main);
        }
        
        // Fallback si pas de gestionnaire
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
        // Si un gestionnaire d'interaction est défini, l'utiliser
        if (gestionnaireInteraction != null) {
            return gestionnaireInteraction.choisirOffreCible(nom, offres);
        }
        
        // Sinon, utiliser la console (fallback)
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
        // Si un gestionnaire d'interaction est défini, l'utiliser
        if (gestionnaireInteraction != null) {
            return gestionnaireInteraction.choisirCarteDansOffre(nom, offre);
        }
        
        // Sinon, utiliser la console (fallback)
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