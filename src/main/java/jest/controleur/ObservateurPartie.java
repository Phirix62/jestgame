package jest.controleur;

import jest.modele.joueurs.Joueur;
import jest.modele.jeu.Offre;
import jest.modele.cartes.Carte;
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
}
