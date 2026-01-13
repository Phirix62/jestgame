package jest.modele.jeu;

import jest.modele.cartes.*;
import jest.modele.joueurs.Joueur;
import jest.modele.score.CalculateurScore;
import jest.modele.utilitaires.GestionnaireSauvegarde;
import jest.modele.utilitaires.GestionnaireScores;
import jest.modele.extensions.*;
import jest.controleur.ObservateurPartie;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Classe principale du moteur de jeu Jest.
 * Orchestre le déroulement complet d'une partie.
 * Classe façade du modèle
 * Pattern Observateur : notifie les vues des changements d'état via le contrôleur
 */
public class Partie implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Joueur> joueurs;
    private Paquet paquet;
    private Pioche pioche;
    private List<Trophee> tropheesEnJeu;
    private int tourActuel;
    private CalculateurScore calculateur;
    private Extension extension;
    private Variante variante;
    private transient List<ObservateurPartie> observateurs;
    
    // Gestionnaires dédiés (SRP)
    private GestionnaireScores gestionnaireScores;
    private List<Carte> cartesResiduelles;

    /**
     * Constructeur de Partie.
     */
    public Partie() {
        this.joueurs = new ArrayList<>();
        this.paquet = new Paquet();
        this.tropheesEnJeu = new ArrayList<>();
        this.tourActuel = 0;
        this.calculateur = new CalculateurScore();
        this.extension = null;
        this.variante = null;
        this.observateurs = new ArrayList<>();
        this.gestionnaireScores = new GestionnaireScores();
    }
    
    /**
     * Ajoute un observateur à la partie.
     * @param observateur Observateur à ajouter
     */
    public void ajouterObservateur(ObservateurPartie observateur) {
        if (observateurs == null) {
            observateurs = new ArrayList<>();
        }
        observateurs.add(observateur);
    }
    
    /**
     * Retire un observateur de la partie.
     * @param observateur Observateur à retirer
     */
    public void retirerObservateur(ObservateurPartie observateur) {
        if (observateurs != null) {
            observateurs.remove(observateur);
        }
    }
    
    /**
     * Notifie tous les observateurs.
     */
    private void notifierObservateurs(java.util.function.Consumer<ObservateurPartie> action) {
        if (observateurs != null) {
            for (ObservateurPartie obs : observateurs) {
                action.accept(obs);
            }
        }
    }

    /**
     * Initialise la partie avec les joueurs.
     * 
     * @param joueurs   Liste des joueurs (3 ou 4)
     * @param extension Extension choisie (null si aucune)
     * @param variante Variante choisie (standard par défaut)
     */
    public void initialiser(List<Joueur> joueurs, Extension extension, Variante variante) {
        if (joueurs.size() < 3 || joueurs.size() > 4) {
            throw new IllegalArgumentException("Le jeu nécessite 3 ou 4 joueurs");
        }

        this.joueurs = new ArrayList<>(joueurs);
        this.extension = extension;
        this.variante = variante;

        paquet.initialiser(extension);
        paquet.melanger();

        // Placer les trophées
        int nbTrophees = this.variante.nombreTrophees(joueurs.size());
        List<Carte> cartesTrophees = paquet.distribuer(nbTrophees);
        
        for (Carte carte : cartesTrophees) {
            // Créer un trophée à partir de la carte tirée
            // La condition est déterminée automatiquement selon couleur + valeur
            Trophee trophee = new Trophee(carte);
            tropheesEnJeu.add(trophee);
        }

        // Créer la pioche avec les cartes restantes
        List<Carte> cartesRestantes = paquet.getCartes();
        pioche = new Pioche(cartesRestantes);
        pioche.melanger();

        // Initialiser le tour
        this.tourActuel = 1;
        
        // Notifier les observateurs
        notifierObservateurs(obs -> obs.notifierInitialisation(this.joueurs, nbTrophees));
        notifierObservateurs(obs -> obs.notifierDetailsInitialisation(extension, tropheesEnJeu, pioche.getTaille()));
    }

    /**
     * Exécute le prochain tour de la partie.
     * 
     * @return true si le tour s'est bien déroulé
     */
    public boolean executerProchainTour() {
        if (!estTerminee()) {
            // Notifier début du tour
            notifierObservateurs(obs -> obs.notifierDebutTour(tourActuel));
            
            boolean succes = executerTour();
            
            // Notifier fin du tour
            notifierObservateurs(obs -> obs.notifierFinTour(tourActuel));
            
            tourActuel++;
            return succes;
        }
        return false;
    }

    /**
     * Exécute un tour complet.
     * 
     * @return true si le tour s'est bien déroulé
     */
    private boolean executerTour() {
        Tour tour = new Tour(tourActuel, joueurs, pioche);

        // Définir les cartes résiduelles si tour > 1
        if (tourActuel > 1 && cartesResiduelles != null) {
            tour.setCartesResiduelles(cartesResiduelles);
        }
        int nbCartes = variante.modifierDistribution(tour, pioche.getTaille(), joueurs.size());
        
        // Phase 1 : Distribution
        Map<Joueur, List<Carte>> mains = tour.distribuerCartes(nbCartes);
        notifierObservateurs(obs -> obs.notifierDistribution(mains));

        // Phase 2 : Création des offres
        tour.creerOffres(mains);
        notifierObservateurs(obs -> obs.notifierOffresCreees(tour.getOffres()));

        // Phase 3 : Prises de cartes avec notification
        tour.executerPrisesCartes((joueur, carte, offre) -> {
            notifierObservateurs(obs -> obs.notifierPriseCarte(joueur, carte, offre));
        });

        // Récupérer les cartes résiduelles pour le prochain tour
        cartesResiduelles = tour.getCartesResiduelles();

        return true;
    }

    /**
     * Vérifie si la partie est terminée.
     * 
     * @return true si fin de partie
     */
    public boolean estTerminee() {
        boolean cartesInsuffisantes = false;
        
        if (tourActuel > 1) {
            int nbJoueurs = joueurs.size();
            int cartesMinimales = nbJoueurs * 2;
            cartesInsuffisantes = (cartesResiduelles.size() + pioche.getTaille()) < cartesMinimales;
        }
        
        return variante.verifierFinPartie(cartesInsuffisantes, tourActuel);
    }

    /**
     * Termine la partie : récupération dernières cartes, attribution trophées, scores.
     */
    public void afficherResultatsFinaux() {
        if (!estTerminee()) {
            return;
        }
        // 1. Récupérer les dernières cartes
        Map<Joueur, Carte> recuperations = recupererDernieresCartes();
        if (!recuperations.isEmpty()) {
            notifierObservateurs(obs -> obs.notifierRecuperationDernieresCartes(recuperations));
        }

        // 2. Révéler tous les Jests
        for (Joueur joueur : joueurs) {
            joueur.getJest().revelerCartes();
        }
        notifierObservateurs(obs -> obs.notifierRevelationJests(joueurs));
        
        // 3. Calculer les scores de base (sans trophées)
        Map<Joueur, Integer> scoresBase = new HashMap<>();
        Map<Joueur, String> detailsBase = new HashMap<>();
        for (Joueur joueur : joueurs) {
            int score = calculateur.calculerScore(joueur.getJest(), true);
            joueur.setScore(score);
            scoresBase.put(joueur, score);
            detailsBase.put(joueur, calculateur.afficherDetailScore(joueur.getJest()));
        }
        notifierObservateurs(obs -> obs.notifierScoresBase(scoresBase, detailsBase));

        // 4. Attribuer les trophées avec notification individuelle
        attribuerTropheesAvecNotification();

        // 5. Recalculer les scores finaux (avec trophées)
        Map<Joueur, Integer> scoresFinaux = new HashMap<>();
        Map<Joueur, String> detailsFinaux = new HashMap<>();
        for (Joueur joueur : joueurs) {
            int scoreFinal = calculateur.calculerScore(joueur.getJest(), false);
            joueur.setScore(scoreFinal);
            scoresFinaux.put(joueur, scoreFinal);
            detailsFinaux.put(joueur, calculateur.afficherDetailScore(joueur.getJest()));
        }
        notifierObservateurs(obs -> obs.notifierScoresFinaux(scoresFinaux, detailsFinaux));

        // 6. Déterminer le classement et notifier fin de partie
        List<Joueur> classement = gestionnaireScores.obtenirClassement(joueurs);
        notifierObservateurs(obs -> obs.notifierFinPartie(classement));
    }
    
    /**
     * Fait récupérer à chaque joueur sa dernière carte résiduelle.
     * @return Map des récupérations (joueur -> carte)
     */
    private Map<Joueur, Carte> recupererDernieresCartes() {
        Map<Joueur, Carte> recuperations = new HashMap<>();
        if (cartesResiduelles != null && !cartesResiduelles.isEmpty()) {
            for (int i = 0; i < joueurs.size() && i < cartesResiduelles.size(); i++) {
                Joueur joueur = joueurs.get(i);
                Carte carte = cartesResiduelles.get(i);
                joueur.ajouterCarteAuJest(carte);
                recuperations.put(joueur, carte);
            }
        }
        return recuperations;
    }
    
    /**
     * Attribue les trophées avec notification individuelle pour chaque attribution.
     */
    private void attribuerTropheesAvecNotification() {
        for (Trophee trophee : tropheesEnJeu) {
            Joueur gagnant = trophee.evaluerCondition(joueurs);
            if (gagnant != null) {
                gagnant.getJest().ajouterTrophee(trophee);
            }
            notifierObservateurs(obs -> obs.notifierAttributionTrophee(trophee, gagnant));
        }
    }

    /**
     * Sauvegarde la partie en cours.
     * 
     * @param nomSauvegarde Nom de la sauvegarde
     * @throws IOException Si erreur lors de la sauvegarde
     */
    public void sauvegarder(String nomSauvegarde) throws IOException {
        GestionnaireSauvegarde.sauvegarder(this, nomSauvegarde);
        notifierObservateurs(obs -> obs.notifierSauvegarde(nomSauvegarde));
    }

    /**
     * Charge une partie depuis une sauvegarde.
     * 
     * @param nomSauvegarde Nom de la sauvegarde
     * @return Partie chargée
     * @throws IOException            Si erreur de lecture
     * @throws ClassNotFoundException Si classe non trouvée
     */
    public static Partie charger(String nomSauvegarde) throws IOException, ClassNotFoundException {
        Partie partie = GestionnaireSauvegarde.charger(nomSauvegarde);
        // Les observateurs seront réattachés après le chargement
        return partie;
    }

    /**
     * Liste toutes les sauvegardes disponibles.
     * 
     * @return Liste des noms de sauvegardes
     */
    public static List<String> listerSauvegardes() {
        return GestionnaireSauvegarde.listerSauvegardes();
    }

    /**
     * Supprime une sauvegarde.
     * 
     * @param nomSauvegarde Nom de la sauvegarde à supprimer
     * @return true si supprimée avec succès
     */
    public static boolean supprimerSauvegarde(String nomSauvegarde) {
        return GestionnaireSauvegarde.supprimerSauvegarde(nomSauvegarde);
    }

    /**
     * Retourne la liste des joueurs.
     * 
     * @return Liste des joueurs
     */
    public List<Joueur> getJoueurs() {
        return new ArrayList<>(joueurs);
    }

    /**
     * Retourne le calculateur de score.
     * 
     * @return Calculateur
     */
    public CalculateurScore getCalculateur() {
        return calculateur;
    }

    /**
     * Retourne le tour actuel.
     * 
     * @return Numéro du tour actuel
     */
    public int getTourActuel() {
        return tourActuel;
    }

    /**
     * Retourne l'extension utilisée.
     * 
     * @return Extension
     */
    public Extension getExtension() {
        return extension;
    }

    /**
     * Retourne la variante utilisée.
     * 
     * @return Variante
     */
    public Variante getVariante() {
        return variante;
    }

    /**
     * Retourne la liste des trophées en jeu.
     * 
     * @return Liste des trophées
     */
    public List<Trophee> getTropheesEnJeu() {
        return new ArrayList<>(tropheesEnJeu);
    }

    /**
     * Retourne les cartes résiduelles.
     * 
     * @return Liste des cartes résiduelles
     */
    public List<Carte> getCartesResiduelles() {
        return cartesResiduelles != null ? cartesResiduelles : new ArrayList<>();
    }

    /**
     * Retourne les cartes restantes dans la pioche.
     * 
     * @return Liste des cartes de la pioche
     */
    public List<Carte> getCartesRestantesPioche() {
        return pioche.getCartes();
    }

    /**
     * Restaure l'état de la partie depuis une sauvegarde.
     * 
     * @param joueurs               Liste des joueurs
     * @param cartesRestantesPioche Cartes restantes dans la pioche
     * @param tropheesEnJeu         Trophées en jeu
     * @param tourActuel            Numéro du tour actuel
     * @param extension             Extension
     * @param cartesResiduelles     Cartes résiduelles
     */
    public void restaurerDepuisSauvegarde(
            List<Joueur> joueurs,
            List<Carte> cartesRestantesPioche,
            List<Trophee> tropheesEnJeu,
            int tourActuel,
            Extension extension,
            Variante variante,
            List<Carte> cartesResiduelles) {

        this.joueurs = joueurs;
        this.tourActuel = tourActuel;
        this.extension = extension;
        this.variante = variante;
        this.tropheesEnJeu = new ArrayList<>(tropheesEnJeu);
        this.cartesResiduelles = cartesResiduelles != null ? new ArrayList<>(cartesResiduelles) : new ArrayList<>();

        this.pioche = new Pioche(cartesRestantesPioche);

        // Réinitialiser le paquet (pas utilisé après l'initialisation)
        this.paquet = new Paquet();

        // Réinitialiser le calculateur
        this.calculateur = new CalculateurScore();
        
        // Réinitialiser les gestionnaires
        this.gestionnaireScores = new GestionnaireScores();
    }

}