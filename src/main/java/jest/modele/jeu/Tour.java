package jest.modele.jeu;

import jest.modele.cartes.Carte;
import jest.modele.joueurs.Joueur;
import java.util.*;

/**
 * Représente un tour de jeu complet (3 phases).
 * Un tour gère la distribution, la création des offres et les prises de cartes.
 */
public class Tour {
    private int numero;
    private Map<Joueur, Offre> offres;
    private List<Joueur> joueurs;
    private Pioche pioche;
    private List<Carte> cartesResiduelles;
    private Set<Joueur> joueursAyantJoue;
    
    /**
     * Constructeur de Tour.
     * @param numero Numéro du tour (commence à 1)
     * @param joueurs Liste des joueurs
     * @param pioche Pioche du jeu
     */
    public Tour(int numero, List<Joueur> joueurs, Pioche pioche) {
        this.numero = numero;
        this.joueurs = new ArrayList<>(joueurs);
        this.pioche = pioche;
        this.offres = new HashMap<>();
        this.cartesResiduelles = new ArrayList<>();
        this.joueursAyantJoue = new HashSet<>();
    }
    
    /**
     * Définit les cartes résiduelles du tour précédent.
     * @param cartes Cartes non prises du tour précédent
     */
    public void setCartesResiduelles(List<Carte> cartes) {
        this.cartesResiduelles = new ArrayList<>(cartes);
    }
    
    /**
     * PHASE 1 : Distribue les cartes aux joueurs.
     * Tour 1 : Piocher directement
     * Tours suivants : Récupérer résiduelles + compléter avec pioche + mélanger
     * @return Map associant chaque joueur à ses 2 cartes
     */
    public Map<Joueur, List<Carte>> distribuerCartes() {
        Map<Joueur, List<Carte>> mains = new HashMap<>();
        List<Carte> cartesADistribuer = new ArrayList<>();
        
        if (numero == 1) {
            // Premier tour : piocher 2 cartes par joueur
            int nbCartes = joueurs.size() * 2;
            cartesADistribuer = pioche.piocher(nbCartes);
        } else {
            // Tours suivants : récupérer résiduelles + compléter
            cartesADistribuer.addAll(cartesResiduelles);
            int nbCartesManquantes = joueurs.size();
            cartesADistribuer.addAll(pioche.piocher(nbCartesManquantes));
            
            // Mélanger
            Collections.shuffle(cartesADistribuer);
        }
        
        // Distribuer 2 cartes à chaque joueur
        int index = 0;
        for (Joueur joueur : joueurs) {
            List<Carte> main = new ArrayList<>();
            main.add(cartesADistribuer.get(index++));
            main.add(cartesADistribuer.get(index++));
            mains.put(joueur, main);
        }
        
        return mains;
    }
    
    /**
     * PHASE 2 : Crée les offres de tous les joueurs.
     * Chaque joueur choisit 1 carte face cachée, l'autre devient visible.
     * @param mains Map des mains distribuées
     */
    public void creerOffres(Map<Joueur, List<Carte>> mains) {
        for (Joueur joueur : joueurs) {
            List<Carte> main = mains.get(joueur);
            
            // Le joueur choisit quelle carte mettre face cachée
            Carte carteCachee = joueur.choisirCarteOffre(main);
            Carte carteVisible = main.get(0) == carteCachee ? main.get(1) : main.get(0);
            
            // Créer l'offre
            Offre offre = new Offre(joueur);
            offre.ajouterCarte(carteVisible, true);
            offre.ajouterCarte(carteCachee, false);
            
            offres.put(joueur, offre);
        }
        
        System.out.println("\n--- Offres créées ---");
        for (Offre offre : offres.values()) {
            System.out.println(offre);
        }
    }
    
    /**
     * Détermine le premier joueur du tour (carte visible la plus forte).
     * @return Premier joueur
     */
    public Joueur determinerPremierJoueur() {
        Joueur premier = null;
        Carte carteMax = null;
        
        for (Map.Entry<Joueur, Offre> entry : offres.entrySet()) {
            Carte carteVisible = entry.getValue().getCarteVisible();
            if (carteMax == null || carteVisible.comparerForce(carteMax) > 0) {
                carteMax = carteVisible;
                premier = entry.getKey();
            }
        }
        
        return premier;
    }
    
    /**
     * PHASE 3 : Execute les prises de cartes dans l'ordre.
     */
    public void executerPrisesCartes() {
        Joueur joueurActif = determinerPremierJoueur();
        System.out.println("\n--- Phase de prises ---");
        System.out.println("Premier joueur : " + joueurActif.getNom());
        
        // Boucle pour chaque joueur
        for (int i = 0; i < joueurs.size(); i++) {
            System.out.println("\n>> Tour de " + joueurActif.getNom());
            
            // Obtenir les offres disponibles (complètes, sauf sa propre offre)
            List<Offre> offresDisponibles = getOffresDisponibles(joueurActif);
            
            // Le joueur choisit une offre
            Offre offreChoisie = joueurActif.choisirOffreCible(offresDisponibles);
            
            // Le joueur choisit une carte dans l'offre
            Carte carteChoisie = joueurActif.choisirCarteDansOffre(offreChoisie);
            
            // Retirer la carte de l'offre
            offreChoisie.retirerCarte(carteChoisie);
            
            // Ajouter au Jest du joueur
            joueurActif.ajouterCarteAuJest(carteChoisie);
            System.out.println(joueurActif.getNom() + " ajoute " + carteChoisie.toStringCourt() + " à son Jest");
            
            // Marquer le joueur comme ayant joué
            joueursAyantJoue.add(joueurActif);
            
            // Déterminer le prochain joueur
            if (i < joueurs.size() - 1) {
                joueurActif = determinerJoueurSuivant(offreChoisie.getProprietaire());
            }
        }
    }
    
    /**
     * Retourne les offres disponibles pour un joueur.
     * Une offre est disponible si elle est complète et n'appartient pas au joueur
     * (sauf si c'est la seule offre complète restante).
     * @param joueur Joueur actif
     * @return Liste des offres disponibles
     */
    private List<Offre> getOffresDisponibles(Joueur joueur) {
        List<Offre> disponibles = new ArrayList<>();
        
        for (Offre offre : offres.values()) {
            if (offre.estComplete() && offre.getProprietaire() != joueur) {
                disponibles.add(offre);
            }
        }
        
        // Si aucune offre disponible, le joueur doit prendre dans sa propre offre
        if (disponibles.isEmpty()) {
            Offre offrePropre = offres.get(joueur);
            if (offrePropre.estComplete()) {
                disponibles.add(offrePropre);
            }
        }
        
        return disponibles;
    }
    
    /**
     * Détermine le joueur suivant selon les règles.
     * Si le propriétaire de l'offre piochée n'a pas encore joué : c'est lui.
     * Sinon : celui avec la carte visible la plus forte parmi ceux restants.
     * @param proprietaireOffre Propriétaire de l'offre qui vient d'être piochée
     * @return Joueur suivant
     */
    private Joueur determinerJoueurSuivant(Joueur proprietaireOffre) {
        // Si le propriétaire n'a pas encore joué, c'est lui
        if (!joueursAyantJoue.contains(proprietaireOffre)) {
            return proprietaireOffre;
        }
        
        // Sinon, trouver le joueur avec la carte visible la plus forte parmi ceux restants
        Joueur suivant = null;
        Carte carteMax = null;
        
        for (Joueur j : joueurs) {
            if (!joueursAyantJoue.contains(j)) {
                Offre offre = offres.get(j);
                if (offre != null && offre.getCarteVisible() != null) {
                    Carte carteVisible = offre.getCarteVisible();
                    if (carteMax == null || carteVisible.comparerForce(carteMax) > 0) {
                        carteMax = carteVisible;
                        suivant = j;
                    }
                }
            }
        }
        
        return suivant;
    }
    
    /**
     * Retourne les cartes résiduelles à la fin du tour.
     * @return Liste des cartes non prises (1 par offre)
     */
    public List<Carte> getCartesResiduelles() {
        List<Carte> residuelles = new ArrayList<>();
        for (Offre offre : offres.values()) {
            residuelles.addAll(offre.getCartesRestantes());
        }
        return residuelles;
    }
    
    /**
     * Retourne le numéro du tour.
     * @return Numéro
     */
    public int getNumero() {
        return numero;
    }
    
    /**
     * Retourne les offres du tour.
     * @return Map des offres
     */
    public Map<Joueur, Offre> getOffres() {
        return new HashMap<>(offres);
    }
}