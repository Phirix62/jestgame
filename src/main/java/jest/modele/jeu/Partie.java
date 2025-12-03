package jest.modele.jeu;

import jest.modele.cartes.*;
import jest.modele.joueurs.Joueur;
import jest.modele.score.CalculateurScore;
import java.util.*;

/**
 * Classe principale du moteur de jeu Jest.
 * Orchestre le déroulement complet d'une partie.
 * Pattern Façade : point d'entrée unique pour toute l'application.
 */
public class Partie {
    private List<Joueur> joueurs;
    private Paquet paquet;
    private Pioche pioche;
    private List<Trophee> tropheesEnJeu;
    private int tourActuel;
    private boolean extensionActive;
    private CalculateurScore calculateur;
    
    /**
     * Constructeur de Partie.
     */
    public Partie() {
        this.joueurs = new ArrayList<>();
        this.paquet = new Paquet();
        this.tropheesEnJeu = new ArrayList<>();
        this.tourActuel = 0;
        this.extensionActive = false;
        this.calculateur = new CalculateurScore();
    }
    
    /**
     * Initialise la partie avec les joueurs.
     * @param joueurs Liste des joueurs (3 ou 4)
     * @param avecExtension true pour activer les extensions
     */
    public void initialiser(List<Joueur> joueurs, boolean avecExtension) {
        if (joueurs.size() < 3 || joueurs.size() > 4) {
            throw new IllegalArgumentException("Le jeu nécessite 3 ou 4 joueurs");
        }
        
        this.joueurs = new ArrayList<>(joueurs);
        this.extensionActive = avecExtension;
        
        // Initialiser et mélanger le paquet
        paquet.initialiser(avecExtension);
        paquet.melanger();
        
        // Placer les trophées
        int nbTrophees = joueurs.size() == 3 ? 2 : 1;
        List<Carte> cartesTrophees = paquet.distribuer(nbTrophees);
        
        for (Carte carte : cartesTrophees) {
            if (carte instanceof Trophee) {
                tropheesEnJeu.add((Trophee) carte);
            } else {
                // Créer un trophée à partir d'une carte normale
                ConditionTrophee condition = genererConditionAleatoire();
                Trophee trophee = new Trophee(carte.getCouleur(), carte.getValeurFaciale(), condition);
                tropheesEnJeu.add(trophee);
            }
        }
        
        // Créer la pioche avec les cartes restantes
        List<Carte> cartesRestantes = paquet.getCartes();
        pioche = new Pioche(cartesRestantes);
        pioche.melanger();
        
        System.out.println("\n=== PARTIE INITIALISÉE ===");
        System.out.println("Joueurs : " + joueurs.size());
        for (Joueur j : joueurs) {
            System.out.println("  - " + j.getNom());
        }
        System.out.println("\nTrophées en jeu :");
        for (Trophee t : tropheesEnJeu) {
            System.out.println("  " + t);
        }
        System.out.println("\nPioche : " + pioche.getTaille() + " cartes");
    }
    
    /**
     * Démarre la partie et exécute la boucle de tours.
     */
    public void demarrer() {
        tourActuel = 1;
        
        while (!verifierFinPartie()) {
            System.out.println("\n");
            System.out.println("═══════════════════════════════════");
            System.out.println("         TOUR " + tourActuel);
            System.out.println("═══════════════════════════════════");
            
            executerTour();
            tourActuel++;
        }
        
        // Fin de partie
        terminerPartie();
    }
    
    /**
     * Exécute un tour complet.
     * @return true si le tour s'est bien déroulé
     */
    private boolean executerTour() {
        Tour tour = new Tour(tourActuel, joueurs, pioche);
        
        // Définir les cartes résiduelles si tour > 1
        if (tourActuel > 1 && cartesResiduelles != null) {
            tour.setCartesResiduelles(cartesResiduelles);
        }
        
        // Phase 1 : Distribution
        Map<Joueur, List<Carte>> mains = tour.distribuerCartes();
        
        // Phase 2 : Création des offres
        tour.creerOffres(mains);
        
        // Phase 3 : Prises de cartes
        tour.executerPrisesCartes();
        
        // Récupérer les cartes résiduelles pour le prochain tour
        cartesResiduelles = tour.getCartesResiduelles();
        
        return true;
    }
    
    private List<Carte> cartesResiduelles; // Stockage temporaire
    
    /**
     * Vérifie si la partie est terminée (pioche vide).
     * @return true si fin de partie
     */
    private boolean verifierFinPartie() {
        return pioche.estVide();
    }
    
    /**
     * Termine la partie : récupération dernières cartes, attribution trophées, scores.
     */
    private void terminerPartie() {
        System.out.println("\n");
        System.out.println("═══════════════════════════════════");
        System.out.println("         FIN DE PARTIE");
        System.out.println("═══════════════════════════════════");
        
        // Chaque joueur récupère la dernière carte de son offre (cartes résiduelles)
        if (cartesResiduelles != null && !cartesResiduelles.isEmpty()) {
            System.out.println("\nRécupération des dernières cartes...");
            for (int i = 0; i < joueurs.size() && i < cartesResiduelles.size(); i++) {
                Joueur joueur = joueurs.get(i);
                Carte carte = cartesResiduelles.get(i);
                joueur.ajouterCarteAuJest(carte);
                System.out.println(joueur.getNom() + " récupère " + carte.toStringCourt());
            }
        }
        
        // Révéler tous les Jests
        System.out.println("\n--- Révélation des Jests ---");
        for (Joueur joueur : joueurs) {
            joueur.getJest().revelerCartes();
            System.out.println(joueur.getNom() + " : " + joueur.getJest().afficherDetails());
        }
        
        // Calculer les scores de base (sans trophées)
        for (Joueur joueur : joueurs) {
            int score = calculateur.calculerScore(joueur.getJest(), true);
            joueur.setScore(score);
        }
        
        // Attribuer les trophées
        attribuerTrophees();
        
        // Recalculer les scores finaux (avec trophées)
        calculerScoresFinal();
        
        // Déterminer et afficher le gagnant
        Joueur gagnant = determinerGagnant();
        afficherResultatsFinaux(gagnant);
    }
    
    /**
     * Attribue les trophées aux joueurs selon les conditions.
     */
    private void attribuerTrophees() {
        System.out.println("\n--- Attribution des trophées ---");
        
        for (Trophee trophee : tropheesEnJeu) {
            Joueur gagnant = trophee.evaluerCondition(joueurs);
            if (gagnant != null) {
                gagnant.getJest().ajouterTrophee(trophee);
                System.out.println("🏆 " + trophee.getCondition().getDescription() + 
                                 " → " + gagnant.getNom());
            } else {
                System.out.println("🏆 " + trophee.getCondition().getDescription() + 
                                 " → Aucun gagnant");
            }
        }
    }
    
    /**
     * Calcule les scores finaux de tous les joueurs (avec trophées).
     */
    private void calculerScoresFinal() {
        System.out.println("\n--- Calcul des scores finaux ---");
        for (Joueur joueur : joueurs) {
            int scoreFinal = calculateur.calculerScore(joueur.getJest(), false);
            joueur.setScore(scoreFinal);
            System.out.println(joueur.getNom() + " : " + scoreFinal + " points");
            System.out.println(calculateur.afficherDetailScore(joueur.getJest()));
        }
    }
    
    /**
     * Détermine le joueur gagnant (score le plus élevé).
     * @return Joueur gagnant
     */
    public Joueur determinerGagnant() {
        Joueur gagnant = null;
        int scoreMax = Integer.MIN_VALUE;
        
        for (Joueur joueur : joueurs) {
            if (joueur.getScore() > scoreMax) {
                scoreMax = joueur.getScore();
                gagnant = joueur;
            } else if (joueur.getScore() == scoreMax) {
                // Bris d'égalité : carte la plus haute
                if (gagnant != null) {
                    Carte c1 = gagnant.getJest().getCartePlusHauteGlobale();
                    Carte c2 = joueur.getJest().getCartePlusHauteGlobale();
                    if (c2 != null && (c1 == null || c2.comparerForce(c1) > 0)) {
                        gagnant = joueur;
                    }
                }
            }
        }
        
        return gagnant;
    }
    
    /**
     * Affiche les résultats finaux de la partie.
     * @param gagnant Joueur gagnant
     */
    private void afficherResultatsFinaux(Joueur gagnant) {
        System.out.println("\n");
        System.out.println("═══════════════════════════════════");
        System.out.println("       RÉSULTATS FINAUX");
        System.out.println("═══════════════════════════════════");
        
        // Trier les joueurs par score décroissant
        List<Joueur> classement = new ArrayList<>(joueurs);
        classement.sort((j1, j2) -> Integer.compare(j2.getScore(), j1.getScore()));
        
        for (int i = 0; i < classement.size(); i++) {
            Joueur j = classement.get(i);
            String rang = (i + 1) + ". ";
            String trophees = j.getJest().getTrophees().isEmpty() ? "" : 
                            " 🏆×" + j.getJest().getTrophees().size();
            System.out.println(rang + j.getNom() + " : " + j.getScore() + " points" + trophees);
        }
        
        System.out.println("\n🎉 VAINQUEUR : " + gagnant.getNom() + " 🎉");
        System.out.println("═══════════════════════════════════\n");
    }
    
    /**
     * Génère une condition de trophée aléatoire.
     * @return Condition aléatoire
     */
    private ConditionTrophee genererConditionAleatoire() {
        ConditionTrophee[] conditions = ConditionTrophee.values();
        Random rand = new Random();
        return conditions[rand.nextInt(conditions.length)];
    }
    
    /**
     * Retourne la liste des joueurs.
     * @return Liste des joueurs
     */
    public List<Joueur> getJoueurs() {
        return new ArrayList<>(joueurs);
    }
    
    /**
     * Retourne le calculateur de score.
     * @return Calculateur
     */
    public CalculateurScore getCalculateur() {
        return calculateur;
    }
}