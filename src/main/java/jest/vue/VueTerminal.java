package jest.vue;

import jest.controleur.ControleurJeu;
import jest.controleur.ObservateurPartie;
import jest.modele.joueurs.*;
import jest.modele.extensions.*;
import jest.modele.cartes.Carte;
import jest.modele.jeu.Offre;

import java.util.*;

/**
 * Vue terminal pour le jeu Jest.
 * Implémente l'interface en ligne de commande.
 */
public class VueTerminal implements Vue, ObservateurPartie, GestionnaireInteraction {
    private Scanner scanner;
    private ControleurJeu controleur;
    private boolean actif;

    /**
     * Constructeur de VueTerminal.
     */
    public VueTerminal() {
        this.scanner = new Scanner(System.in);
        this.actif = false;
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
        int nbJoueurs = choisirNombreJoueurs();
        List<Joueur> joueurs = creerJoueurs(nbJoueurs);
        Extension extension = choisirExtensions(joueurs.size());
        Variante variante = choisirVariante();

        return new Object[]{joueurs, extension, variante};
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
            System.out.println("  joueur " + joueur.getNom() + " créé\n");
        }

        return joueurs;
    }

    /**
     * Choisir l'extension à utiliser pour la partie
     * 
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
     * Choisir la variante de règles à utiliser pour la partie
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
        // Les détails sont affichés par les joueurs physiques lors du choix
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
        System.out.println("\n");
        System.out.println("═══════════════════════════════════");
        System.out.println("       RÉSULTATS FINAUX");
        System.out.println("═══════════════════════════════════");

        for (int i = 0; i < classement.size(); i++) {
            Joueur j = classement.get(i);
            String rang = (i + 1) + ". ";
            String trophees = j.getJest().getTrophees().isEmpty() ? "" 
                : " Trophée*" + j.getJest().getTrophees().size();
            System.out.println(rang + j.getNom() + " : " + j.getScore() + " points" + trophees);
        }

        if (!classement.isEmpty()) {
            System.out.println("\n** VAINQUEUR : " + classement.get(0).getNom() + " **");
        }
        System.out.println("═══════════════════════════════════\n");
    }

    @Override
    public void notifierSauvegarde(String nomSauvegarde) {
        System.out.println("✓ Partie sauvegardée : " + nomSauvegarde);
    }

    @Override
    public void notifierChargement(String nomSauvegarde) {
        System.out.println("✓ Partie chargée : " + nomSauvegarde);
    }
    
    // Implémentation de GestionnaireInteraction
    
    @Override
    public Carte choisirCarteOffre(String joueurNom, List<Carte> main) {
        
        System.out.println("\n=== " + joueurNom + ", créez votre offre ===");
        System.out.println("Vos " + main.size() + " cartes :");
        for (int i = 0; i < main.size(); i++) {
            System.out.println((i + 1) + ". " + main.get(i));
        }
        
        System.out.print("Choisissez la carte à jouer FACE CACHÉE (1-" + main.size() + ") : ");
        int choix = lireChoix(1, main.size());
        
        return main.get(choix - 1);
    }
    
    @Override
    public Offre choisirOffreCible(String joueurNom, List<Offre> offres) {
        
        System.out.println("\n=== " + joueurNom + ", choisissez une offre ===");
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
    public Carte choisirCarteDansOffre(String joueurNom, Offre offre) {
        List<Carte> cartesVisibles = offre.getCartesVisibles();
        int totalCartes = cartesVisibles.size() + (offre.getCarteCachee() != null ? 1 : 0);
        
        // Vérifier si le thread est interrompu avant d'afficher les prompts
        if (Thread.currentThread().isInterrupted()) {
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
        if (Thread.currentThread().isInterrupted()) {
            return !cartesVisibles.isEmpty() ? cartesVisibles.get(0) : offre.getCarteCachee();
        }
        
        if (choix <= cartesVisibles.size()) {
            return cartesVisibles.get(choix - 1);
        } else {
            return offre.getCarteCachee();
        }
    }
}
