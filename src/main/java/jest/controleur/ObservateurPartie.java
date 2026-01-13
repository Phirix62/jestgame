package jest.controleur;

import jest.modele.joueurs.Joueur;
import jest.modele.jeu.Offre;
import jest.modele.cartes.Carte;
import jest.modele.cartes.Trophee;
import jest.modele.extensions.Extension;
import java.util.List;
import java.util.Map;

/**
 * Interface Observer pour le pattern Observateur.
 * Les vues implémentent cette interface pour être notifiées des changements d'état du modèle.
 */
public interface ObservateurPartie {
    
    /**
     * Notifie qu'une nouvelle partie a été initialisée.
     * @param joueurs Liste des joueurs
     * @param nbTrophees Nombre de trophées en jeu
     */
    void notifierInitialisation(List<Joueur> joueurs, int nbTrophees);
    
    /**
     * Notifie le début d'un nouveau tour.
     * @param numeroTour Numéro du tour
     */
    void notifierDebutTour(int numeroTour);
    
    /**
     * Notifie que les cartes ont été distribuées.
     * @param mains Map des mains par joueur
     */
    void notifierDistribution(Map<Joueur, List<Carte>> mains);
    
    /**
     * Notifie que les offres ont été créées.
     * @param offres Map des offres par joueur
     */
    void notifierOffresCreees(Map<Joueur, Offre> offres);
    
    /**
     * Notifie qu'un joueur a pris une carte.
     * @param joueur Joueur ayant pris la carte
     * @param carte Carte prise
     * @param offreCible Offre d'où vient la carte
     */
    void notifierPriseCarte(Joueur joueur, Carte carte, Offre offreCible);
    
    /**
     * Notifie la fin d'un tour.
     * @param numeroTour Numéro du tour terminé
     */
    void notifierFinTour(int numeroTour);
    
    /**
     * Notifie la fin de la partie avec les scores finaux.
     * @param classement Liste des joueurs classés par score
     */
    void notifierFinPartie(List<Joueur> classement);
    
    /**
     * Notifie qu'une partie a été sauvegardée.
     * @param nomSauvegarde Nom de la sauvegarde
     */
    void notifierSauvegarde(String nomSauvegarde);
    
    /**
     * Notifie qu'une partie a été chargée.
     * @param nomSauvegarde Nom de la sauvegarde chargée
     */
    void notifierChargement(String nomSauvegarde);
    
    /**
     * Notifie les détails de l'initialisation (extension, trophées, pioche).
     * @param extension Extension utilisée (peut être null)
     * @param trophees Liste des trophées en jeu
     * @param taillepioche Nombre de cartes dans la pioche
     */
    void notifierDetailsInitialisation(Extension extension, List<Trophee> trophees, int taillepioche);
    
    /**
     * Notifie la récupération des dernières cartes.
     * @param recuperations Map joueur -> carte récupérée
     */
    void notifierRecuperationDernieresCartes(Map<Joueur, Carte> recuperations);
    
    /**
     * Notifie la révélation des Jests.
     * @param joueurs Liste des joueurs avec leurs Jests révélés
     */
    void notifierRevelationJests(List<Joueur> joueurs);
    
    /**
     * Notifie le calcul des scores de base (sans trophées).
     * @param scores Map joueur -> score de base
     * @param details Map joueur -> détail du calcul (peut être null)
     */
    void notifierScoresBase(Map<Joueur, Integer> scores, Map<Joueur, String> details);
    
    /**
     * Notifie l'attribution d'un trophée.
     * @param trophee Trophée attribué
     * @param gagnant Joueur gagnant (peut être null si personne)
     */
    void notifierAttributionTrophee(Trophee trophee, Joueur gagnant);
    
    /**
     * Notifie le calcul des scores finaux (avec trophées).
     * @param scores Map joueur -> score final
     * @param details Map joueur -> détail du calcul (peut être null)
     */
    void notifierScoresFinaux(Map<Joueur, Integer> scores, Map<Joueur, String> details);
}
