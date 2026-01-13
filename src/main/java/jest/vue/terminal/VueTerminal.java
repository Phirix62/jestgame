package jest.vue.terminal;

import jest.controleur.ConfigurateurPartie;
import jest.controleur.ControleurJeu;
import jest.controleur.ObservateurPartie;
import jest.modele.joueurs.*;
import jest.modele.extensions.*;
import jest.modele.cartes.Carte;
import jest.modele.cartes.Trophee;
import jest.modele.jeu.Offre;
import jest.vue.Vue;
import jest.vue.GestionnaireInteraction;

import java.util.*;

/**
 * Vue terminal pour le jeu Jest.
 * Implémente l'interface en ligne de commande.
 */
public class VueTerminal implements Vue, ObservateurPartie, GestionnaireInteraction {
    private Scanner scanner;
    private ControleurJeu controleur;
    private boolean actif;
    private ConfigurateurPartie configurateur;
    private boolean premierTrophee = true;

    /**
     * Constructeur de VueTerminal.
     */
    public VueTerminal() {
        this.scanner = new Scanner(System.in);
        this.actif = false;
        this.configurateur = new ConfigurateurPartie(scanner);
    }

    @Override
    public void initialiser(ControleurJeu controleur) {
        this.controleur = controleur;
        controleur.ajouterObservateur(this);
    }

    @Override
    public void demarrer() {
        this.actif = true;
        afficherBanniere();
    }

    @Override
    public void arreter() {
        this.actif = false;
        scanner.close();
    }

    /**
     * Affiche la bannière du jeu.
     */
    private void afficherBanniere() {
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

    @Override
    public Object[] configurerNouvellePartie() {
        return configurateur.configurerNouvellePartie();
    }

    @Override
    public String choisirSauvegarde(List<String> sauvegardes) {
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

        return sauvegardes.get(choix - 1);
    }

    @Override
    public String demanderNomSauvegarde() {
        System.out.print("\nNom de la sauvegarde : ");
        String nom = scanner.nextLine().trim();

        if (nom.isEmpty()) {
            nom = "partie_" + System.currentTimeMillis();
        }

        return nom;
    }

    @Override
    public void afficherMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void afficherErreur(String erreur) {
        System.err.println("ERREUR : " + erreur);
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

    // Implémentation des méthodes ObservateurPartie

    @Override
    public void notifierInitialisation(List<Joueur> joueurs, int nbTrophees) {
        System.out.println("\n=== PARTIE INITIALISÉE ===");
        System.out.println("Joueurs : " + joueurs.size());
        for (Joueur j : joueurs) {
            System.out.println("  - " + j.getNom());
        }
        System.out.println("Trophées : " + nbTrophees);
    }

    @Override
    public void notifierDebutTour(int numeroTour) {
        System.out.println("\n═══════════════════════════════════");
        System.out.println("         TOUR " + numeroTour);
        System.out.println("═══════════════════════════════════\n");
    }

    @Override
    public void notifierDistribution(Map<Joueur, List<Carte>> mains) {
        System.out.println("--- Distribution des cartes ---");
    }

    @Override
    public void notifierOffresCreees(Map<Joueur, Offre> offres) {
        System.out.println("\n--- Offres créées ---");
        for (Offre offre : offres.values()) {
            System.out.println(offre);
        }
    }

    @Override
    public void notifierPriseCarte(Joueur joueur, Carte carte, Offre offreCible) {
        System.out.println(joueur.getNom() + " prend " + carte.toStringCourt() + 
                          " de l'offre de " + offreCible.getProprietaire().getNom());
    }

    @Override
    public void notifierFinTour(int numeroTour) {
        System.out.println("\n--- Fin du tour " + numeroTour + " ---");
    }

    @Override
    public void notifierFinPartie(List<Joueur> classement) {
        premierTrophee = true; // Réinitialiser pour la prochaine partie
        
        System.out.println("\n");
        System.out.println("═══════════════════════════════════");
        System.out.println("       RÉSULTATS FINAUX");
        System.out.println("═══════════════════════════════════");

        for (int i = 0; i < classement.size(); i++) {
            Joueur j = classement.get(i);
            String rang = (i + 1) + ". ";
            String trophees = j.getJest().getTrophees().isEmpty() ? "" 
                : " ✦ Trophée*" + j.getJest().getTrophees().size();
            System.out.println(rang + j.getNom() + " : " + j.getScore() + " points" + trophees);
        }

        if (!classement.isEmpty()) {
            System.out.println("\n** VAINQUEUR : " + classement.get(0).getNom() + " **");
        }
        System.out.println("═══════════════════════════════════\n");
    }

    @Override
    public void notifierSauvegarde(String nomSauvegarde) {
        System.out.println(" Partie sauvegardée : " + nomSauvegarde);
    }

    @Override
    public void notifierChargement(String nomSauvegarde) {
        System.out.println(" Partie chargée : " + nomSauvegarde);
    }
    
    @Override
    public void notifierDetailsInitialisation(Extension extension, List<Trophee> trophees, int taillePioche) {
        System.out.println(FormateurAffichage.formaterDetailsInitialisation(extension, trophees, taillePioche));
    }
    
    @Override
    public void notifierRecuperationDernieresCartes(Map<Joueur, Carte> recuperations) {
        System.out.println(FormateurAffichage.formaterRecuperations(recuperations));
    }
    
    @Override
    public void notifierRevelationJests(List<Joueur> joueurs) {
        System.out.println(FormateurAffichage.formaterRevelationJests(joueurs));
    }
    
    @Override
    public void notifierScoresBase(Map<Joueur, Integer> scores, Map<Joueur, String> details) {
        System.out.println(FormateurAffichage.formaterScores(scores, details, "--- Calcul des scores ---"));
    }
    
    @Override
    public void notifierAttributionTrophee(Trophee trophee, Joueur gagnant) {
        if (premierTrophee) {
            System.out.println("\n--- Attribution des trophées ---");
            premierTrophee = false;
        }
        System.out.println(FormateurAffichage.formaterAttributionTrophee(trophee, gagnant));
    }
    
    @Override
    public void notifierScoresFinaux(Map<Joueur, Integer> scores, Map<Joueur, String> details) {
        System.out.println(FormateurAffichage.formaterScores(scores, details, "\n--- Calcul des scores finaux avec trophées ---"));
    }
    
    // Implémentation de GestionnaireInteraction
    
    @Override
    public Carte choisirCarteOffre(String joueurNom, List<Carte> main) {
        // Vérifier si déjà interrompu (l'autre vue a répondu)
        // interrupted() consomme le flag pour permettre les futurs choix
        if (Thread.interrupted()) {
            System.out.println("\n[INFO] Choix déjà effectué via l'interface graphique.");
            // Vider le buffer du scanner au cas où
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            return main.get(0);
        }
        
        System.out.println("\n=== " + joueurNom + ", créez votre offre ===");
        System.out.println("Vos " + main.size() + " cartes :");
        for (int i = 0; i < main.size(); i++) {
            System.out.println((i + 1) + ". " + main.get(i));
        }
        
        System.out.print("Choisissez la carte à jouer FACE CACHÉE (1-" + main.size() + ") : ");
        int choix = lireChoix(1, main.size());
        
        // Vérifier après la lecture si on a été interrompu
        if (Thread.interrupted()) {
            System.out.println("[INFO] Choix déjà effectué via l'interface graphique.");
            // Vider le buffer au cas où
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            return main.get(0);
        }
        
        return main.get(choix - 1);
    }
    
    @Override
    public Offre choisirOffreCible(String joueurNom, List<Offre> offres) {
        // Vérifier si déjà interrompu
        if (Thread.interrupted()) {
            System.out.println("\n[INFO] Choix déjà effectué via l'interface graphique.");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            return offres.get(0);
        }
        
        System.out.println("\n=== " + joueurNom + ", choisissez une offre ===");
        System.out.println("Offres disponibles :");
        for (int i = 0; i < offres.size(); i++) {
            Offre o = offres.get(i);
            System.out.println((i + 1) + ". Offre de " + o.getProprietaire().getNom());
            for (int j = 0; j < o.getNombreCartesVisibles(); j++) {
                System.out.println("   - Carte visible: " + o.getCartesVisibles().get(j));
            }
            System.out.println("   - Carte cachée: ???");
        }
        
        System.out.print("Choisissez une offre (1-" + offres.size() + ") : ");
        int choix = lireChoix(1, offres.size());
        
        // Vérifier après la lecture
        if (Thread.interrupted()) {
            System.out.println("[INFO] Choix déjà effectué via l'interface graphique.");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            return offres.get(0);
        }
        
        return offres.get(choix - 1);
    }
    
    @Override
    public Carte choisirCarteDansOffre(String joueurNom, Offre offre) {
        List<Carte> cartesVisibles = offre.getCartesVisibles();
        int totalCartes = cartesVisibles.size() + (offre.getCarteCachee() != null ? 1 : 0);
        
        // Vérifier si le thread est interrompu
        if (Thread.interrupted()) {
            System.out.println("\n[INFO] Choix déjà effectué via l'interface graphique.");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            return !cartesVisibles.isEmpty() ? cartesVisibles.get(0) : offre.getCarteCachee();
        }
        
        System.out.println("\n=== " + joueurNom + ", choisissez une carte dans l'offre ===");
        System.out.println("Offre de " + offre.getProprietaire().getNom() + " :");
        
        for (int i = 0; i < cartesVisibles.size(); i++) {
            System.out.println((i + 1) + ". Carte visible: " + cartesVisibles.get(i));
        }
        if (offre.getCarteCachee() != null) {
            System.out.println(cartesVisibles.size() + 1 + ". Carte cachée: ???");
        }
        
        System.out.print("Choisissez une carte (1-" + totalCartes + ") : ");
        int choix = lireChoix(1, totalCartes);
        
        // Vérifier à nouveau après la lecture
        if (Thread.interrupted()) {
            System.out.println("[INFO] Choix déjà effectué via l'interface graphique.");
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            return !cartesVisibles.isEmpty() ? cartesVisibles.get(0) : offre.getCarteCachee();
        }
        
        if (choix <= cartesVisibles.size()) {
            return cartesVisibles.get(choix - 1);
        } else {
            return offre.getCarteCachee();
        }
    }
}
