package jest.utilitaires;

import jest.modele.jeu.Partie;
import jest.modele.joueurs.*;
import jest.modele.cartes.*;
import jest.modele.jeu.Pioche;

import java.io.*;
import java.util.*;

/**
 * Gestionnaire de sauvegarde/chargement de parties.
 * Utilise la sérialisation Java native (simple, sans dépendance externe).
 */
public class GestionnaireSauvegarde {
    
    private static final String REPERTOIRE_SAUVEGARDES = "sauvegardes/";
    private static final String EXTENSION = ".jest";
    
    /**
     * Sauvegarde une partie dans un fichier.
     * @param partie Partie à sauvegarder
     * @param nomFichier Nom du fichier (sans extension)
     * @throws IOException Si erreur d'écriture
     */
    public static void sauvegarder(Partie partie, String nomFichier) throws IOException {
        File repertoire = new File(REPERTOIRE_SAUVEGARDES);
        if (!repertoire.exists()) {
            repertoire.mkdirs();
        }
        
        SauvegardePartie sauvegarde = new SauvegardePartie(partie);
        
        // Sérialiser
        String cheminComplet = REPERTOIRE_SAUVEGARDES + nomFichier + EXTENSION;
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(cheminComplet))) {
            oos.writeObject(sauvegarde);
        }
        
        System.out.println("Partie sauvegardée : " + cheminComplet);
    }
    
    /**
     * Charge une partie depuis un fichier.
     * @param nomFichier Nom du fichier (sans extension)
     * @return Partie chargée
     * @throws IOException Si erreur de lecture
     * @throws ClassNotFoundException Si classe non trouvée
     */
    public static Partie charger(String nomFichier) throws IOException, ClassNotFoundException {
        String cheminComplet = REPERTOIRE_SAUVEGARDES + nomFichier + EXTENSION;
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(cheminComplet))) {
            SauvegardePartie sauvegarde = (SauvegardePartie) ois.readObject();
            Partie partie = sauvegarde.restaurerPartie();
            System.out.println("Partie chargée : " + cheminComplet);
            return partie;
        }
    }
    
    /**
     * Liste toutes les sauvegardes disponibles.
     * @return Liste des noms de fichiers (sans extension)
     */
    public static List<String> listerSauvegardes() {
        List<String> sauvegardes = new ArrayList<>();
        File repertoire = new File(REPERTOIRE_SAUVEGARDES);
        
        if (repertoire.exists() && repertoire.isDirectory()) {
            File[] fichiers = repertoire.listFiles((dir, name) -> name.endsWith(EXTENSION));
            if (fichiers != null) {
                for (File f : fichiers) {
                    String nom = f.getName().replace(EXTENSION, "");
                    sauvegardes.add(nom);
                }
            }
        }
        
        return sauvegardes;
    }
    
    /**
     * Supprime une sauvegarde.
     * @param nomFichier Nom du fichier (sans extension)
     * @return true si supprimé avec succès
     */
    public static boolean supprimerSauvegarde(String nomFichier) {
        String cheminComplet = REPERTOIRE_SAUVEGARDES + nomFichier + EXTENSION;
        File fichier = new File(cheminComplet);
        return fichier.delete();
    }
}

/**
 * Classe interne représentant une sauvegarde sérialisable
 * Contient toutes les données nécessaires pour restaurer une partie
 */
class SauvegardePartie implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Données de la partie
    private List<DonneesJoueur> joueurs;
    private List<Carte> cartesRestantesPioche;
    private List<Trophee> tropheesEnJeu;
    private int tourActuel;
    private boolean extensionActive;
    private List<Carte> cartesResiduelles;
    
    /**
     * Constructeur créant une sauvegarde depuis une Partie.
     * @param partie Partie à sauvegarder
     */
    public SauvegardePartie(Partie partie) {
        this.joueurs = new ArrayList<>();
        
        // Sauvegarder les joueurs
        for (Joueur j : partie.getJoueurs()) {
            joueurs.add(new DonneesJoueur(j));
        }
        
        this.cartesRestantesPioche = partie.getCartesRestantesPioche();
        
        this.tropheesEnJeu = new ArrayList<>(partie.getTropheesEnJeu());
        this.tourActuel = partie.getTourActuel();
        this.extensionActive = partie.isExtensionActive();
        this.cartesResiduelles = partie.getCartesResiduelles();
    }
    
    /**
     * Restaure une Partie depuis cette sauvegarde.
     * @return Partie restaurée
     */
    public Partie restaurerPartie() {
        Partie partie = new Partie();
        
        // Recréer les joueurs
        List<Joueur> joueursRestaures = new ArrayList<>();
        for (DonneesJoueur dj : joueurs) {
            joueursRestaures.add(dj.restaurerJoueur());
        }
        
        // Réinitialiser la partie avec les joueurs
        partie.restaurerDepuisSauvegarde(
            joueursRestaures, 
            cartesRestantesPioche, 
            tropheesEnJeu, 
            tourActuel, 
            extensionActive, 
            cartesResiduelles
        );
    
    return partie;
    }
}

/**
 * Représente les données sérialisables d'un joueur.
 */
class DonneesJoueur implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nom;
    private String type;
    private int score;
    private List<Carte> cartesJest;
    private List<Trophee> tropheesJest;
    
    public DonneesJoueur(Joueur joueur) {
        this.nom = joueur.getNom();
        this.score = joueur.getScore();
        this.cartesJest = new ArrayList<>(joueur.getJest().getCartes());
        this.tropheesJest = new ArrayList<>(joueur.getJest().getTrophees());
        
        // Déterminer le type
        if (joueur instanceof JoueurPhysique) {
            this.type = "PHYSIQUE";
        } else if (joueur instanceof JoueurVirtuel) {
            JoueurVirtuel jv = (JoueurVirtuel) joueur;
            if (jv.getStrategie() instanceof StrategieAleatoire) {
                this.type = "VIRTUEL_ALEATOIRE";
            } else {
                this.type = "VIRTUEL_AUTRE"; // TODO: Autres stratégies
            }
        }
    }
    
    public Joueur restaurerJoueur() {
        Joueur joueur;
        
        switch (type) {
            case "PHYSIQUE":
                joueur = new JoueurPhysique(nom);
                break;
            case "VIRTUEL_ALEATOIRE":
                joueur = new JoueurVirtuel(nom, new StrategieAleatoire());
                break;
            default:
                joueur = new JoueurVirtuel(nom, new StrategieAleatoire());
        }
        
        // Restaurer le Jest
        for (Carte c : cartesJest) {
            joueur.ajouterCarteAuJest(c);
        }
        for (Trophee t : tropheesJest) {
            joueur.getJest().ajouterTrophee(t);
        }
        
        joueur.setScore(score);
        
        return joueur;
    }
}