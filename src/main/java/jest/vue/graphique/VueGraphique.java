package jest.vue.graphique;

import jest.controleur.ControleurJeu;
import jest.controleur.ObservateurPartie;
import jest.modele.joueurs.*;
import jest.modele.extensions.*;
import jest.modele.cartes.Carte;
import jest.modele.cartes.Trophee;
import jest.modele.jeu.Offre;
import jest.vue.Vue;
import jest.vue.GestionnaireInteraction;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Vue graphique Swing pour le jeu Jest.
 * Affiche l'état du jeu en temps réel et permet l'interaction.
 */
public class VueGraphique extends JFrame implements Vue, ObservateurPartie, GestionnaireInteraction {
    private ControleurJeu controleur;
    private boolean actif;
    private DialogueChoix dialogueChoix;

    // Composants principaux
    private JPanel panneauJoueurs;
    private JPanel panneauOffres;
    private JPanel panneauControles;
    private JPanel panneauTrophees;
    private JTextArea zoneLog;
    private JLabel labelTour;
    private JLabel labelStatut;

    // Données actuelles
    private Map<Joueur, JPanel> panneauxJoueurs;
    private Map<Joueur, Offre> offresActuelles;

    /**
     * Constructeur de VueGraphique.
     */
    public VueGraphique() {
        super("Jest - Jeu de Cartes");
        this.actif = false;
        this.panneauxJoueurs = new HashMap<>();
        this.offresActuelles = new HashMap<>();
        this.dialogueChoix = new DialogueChoix(this);
        initialiserInterface();
    }

    /**
     * Initialise l'interface graphique.
     */
    private void initialiserInterface() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Initialisation des composants
        labelTour = new JLabel("Tour: -", SwingConstants.CENTER);
        labelStatut = new JLabel("En attente...", SwingConstants.CENTER);
        JLabel titre = new JLabel("JEST", SwingConstants.CENTER);
        zoneLog = new JTextArea();
        panneauJoueurs = new JPanel();
        panneauOffres = new JPanel();
        panneauTrophees = new JPanel();

        // Construction des panneaux avec le constructeur
        JPanel panneauPrincipal = ConstructeurInterfaceGraphique.creerPanneauPrincipal();
        JPanel panneauHaut = ConstructeurInterfaceGraphique.creerPanneauHaut(titre, labelTour, labelStatut);
        JPanel panneauCentre = ConstructeurInterfaceGraphique.creerPanneauCentre(panneauJoueurs, panneauOffres, panneauTrophees);
        JPanel panneauLog = ConstructeurInterfaceGraphique.creerPanneauLog(zoneLog);
        panneauControles = ConstructeurInterfaceGraphique.creerPanneauControles(
            this::executerProchainTour,
            this::sauvegarderPartie,
            this::quitter
        );

        // Assemblage
        panneauPrincipal.add(panneauHaut, BorderLayout.NORTH);
        panneauPrincipal.add(panneauCentre, BorderLayout.CENTER);
        panneauPrincipal.add(panneauLog, BorderLayout.EAST);
        panneauPrincipal.add(panneauControles, BorderLayout.SOUTH);

        add(panneauPrincipal);
    }

    /**
     * Affiche les offres du tour actuel.
     */
    private void afficherOffres() {
        panneauOffres.removeAll();
        for (Map.Entry<Joueur, Offre> entry : offresActuelles.entrySet()) {
            panneauOffres.add(PanneauFactory.creerPanneauOffre(entry.getKey(), entry.getValue()));
        }
        panneauOffres.revalidate();
        panneauOffres.repaint();
    }

    /**
     * Exécute le prochain tour via le contrôleur.
     */
    private void executerProchainTour() {
        if (controleur != null) {
            new Thread(() -> {
                try {
                    if (!controleur.estPartieTerminee()) {
                        controleur.executerProchainTour();
                    } else {
                        controleur.afficherResultatsFinaux();
                    }
                } catch (Exception e) {
                    afficherErreur("Erreur lors de l'exécution du tour: " + e.getMessage());
                }
            }).start();
        }
    }

    /**
     * Sauvegarde la partie.
     */
    private void sauvegarderPartie() {
        String nom = JOptionPane.showInputDialog(this, "Nom de la sauvegarde:", "Sauvegarder", 
                JOptionPane.QUESTION_MESSAGE);
        if (nom != null && !nom.trim().isEmpty()) {
            try {
                controleur.sauvegarderPartie(nom);
            } catch (Exception e) {
                afficherErreur("Erreur lors de la sauvegarde: " + e.getMessage());
            }
        }
    }

    /**
     * Quitte l'application.
     */
    private void quitter() {
        int choix = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment quitter?", 
                "Quitter", JOptionPane.YES_NO_OPTION);
        if (choix == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Ajoute un message au log.
     */
    private void ajouterLog(String message) {
        SwingUtilities.invokeLater(() -> {
            zoneLog.append(message + "\n");
            zoneLog.setCaretPosition(zoneLog.getDocument().getLength());
        });
    }

    // Implémentation de l'interface Vue

    @Override
    public void initialiser(ControleurJeu controleur) {
        this.controleur = controleur;
        controleur.ajouterObservateur(this);
    }

    @Override
    public void demarrer() {
        this.actif = true;
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            ajouterLog("=== Interface graphique démarrée ===");
        });
    }

    @Override
    public void arreter() {
        this.actif = false;
        dispose();
    }

    @Override
    public Object[] configurerNouvellePartie() {
        return null; // Non utilisé par la GUI
    }

    @Override
    public String choisirSauvegarde(List<String> sauvegardes) {
        return null; // Non utilisé par la GUI
    }

    @Override
    public String demanderNomSauvegarde() {
        return null; // Non utilisé par la GUI
    }

    @Override
    public void afficherMessage(String message) {
        ajouterLog("[INFO] " + message);
        SwingUtilities.invokeLater(() -> labelStatut.setText(message));
    }

    @Override
    public void afficherErreur(String erreur) {
        ajouterLog("[ERREUR] " + erreur);
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, erreur, "Erreur", JOptionPane.ERROR_MESSAGE);
        });
    }

    // Implémentation de ObservateurPartie

    @Override
    public void notifierInitialisation(List<Joueur> joueurs, int nbTrophees) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("=== PARTIE INITIALISÉE ===");
            ajouterLog("Joueurs: " + joueurs.size());
            for (Joueur j : joueurs) {
                ajouterLog("  - " + j.getNom());
            }
            ajouterLog("Trophées: " + nbTrophees);

            // Créer les panneaux pour chaque joueur
            panneauJoueurs.removeAll();
            panneauxJoueurs.clear();

            for (Joueur joueur : joueurs) {
                JPanel panneau = PanneauFactory.creerPanneauJoueur(joueur);
                panneauxJoueurs.put(joueur, panneau);
                panneauJoueurs.add(panneau);
            }

            // Afficher les trophées
            panneauTrophees.removeAll();
            if (controleur != null && controleur.getPartie() != null) {
                var trophees = controleur.getPartie().getTropheesEnJeu();
                for (var trophee : trophees) {
                    Carte carteTrophee = trophee.getCarteAssociee();
                    carteTrophee.reveler();
                    JLabel labelTrophee = CarteRenderer.creerMiniCarte(carteTrophee);
                    JLabel legendeTrophee = new JLabel(" (" + trophee.getCondition() + ")");
                    panneauTrophees.add(labelTrophee);
                    panneauTrophees.add(legendeTrophee);
                }
            }
            panneauTrophees.revalidate();
            panneauTrophees.repaint();

            panneauJoueurs.revalidate();
            panneauJoueurs.repaint();

            labelStatut.setText("Partie initialisée - " + joueurs.size() + " joueurs");
        });
    }

    @Override
    public void notifierDebutTour(int numeroTour) {
        SwingUtilities.invokeLater(() -> {
            labelTour.setText("Tour: " + numeroTour);
            ajouterLog("\n=== TOUR " + numeroTour + " ===");
            labelStatut.setText("Tour " + numeroTour + " en cours...");

            // Désactiver la sauvegarde pendant le tour
            JButton btnSauvegarder = (JButton) panneauControles.getClientProperty("btnSauvegarder");
            if (btnSauvegarder != null) {
                btnSauvegarder.setEnabled(false);
            }
        });
    }

    @Override
    public void notifierDistribution(Map<Joueur, List<Carte>> mains) {
        SwingUtilities.invokeLater(() -> ajouterLog("Distribution des cartes..."));
    }

    @Override
    public void notifierOffresCreees(Map<Joueur, Offre> offres) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("Offres créées:");
            for (Map.Entry<Joueur, Offre> entry : offres.entrySet()) {
                ajouterLog("  " + entry.getKey().getNom() + ": " + 
                          entry.getValue().getNombreCartesVisibles() + " visible(s)");
            }

            offresActuelles = new HashMap<>(offres);
            afficherOffres();
        });
    }

    @Override
    public void notifierPriseCarte(Joueur joueur, Carte carte, Offre offreCible) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog(joueur.getNom() + " prend " + carte.toStringCourt() + 
                      " de l'offre de " + offreCible.getProprietaire().getNom());

            // Recréer le panneau du joueur avec les cartes mises à jour
            JPanel nouveauPanneau = PanneauFactory.creerPanneauJoueur(joueur);
            panneauxJoueurs.put(joueur, nouveauPanneau);

            // Remplacer dans l'interface
            panneauJoueurs.removeAll();
            for (Joueur j : controleur.getJoueurs()) {
                JPanel panneauJ = panneauxJoueurs.get(j);
                if (panneauJ != null) {
                    panneauJoueurs.add(panneauJ);
                }
            }
            panneauJoueurs.revalidate();
            panneauJoueurs.repaint();
        });
    }

    @Override
    public void notifierFinTour(int numeroTour) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("--- Fin du tour " + numeroTour + " ---");
            labelStatut.setText("Tour " + numeroTour + " terminé");

            // Effacer les offres
            panneauOffres.removeAll();
            panneauOffres.revalidate();
            panneauOffres.repaint();
            offresActuelles.clear();

            // Activer la sauvegarde entre les tours
            JButton btnSauvegarder = (JButton) panneauControles.getClientProperty("btnSauvegarder");
            if (btnSauvegarder != null) {
                btnSauvegarder.setEnabled(true);
            }
        });
    }

    @Override
    public void notifierFinPartie(List<Joueur> classement) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("\n=== RÉSULTATS FINAUX ===");
            for (int i = 0; i < classement.size(); i++) {
                Joueur j = classement.get(i);
                ajouterLog((i + 1) + ". " + j.getNom() + ": " + j.getScore() + " points");
            }

            if (!classement.isEmpty()) {
                ajouterLog("\n** VAINQUEUR: " + classement.get(0).getNom() + " **");
                labelStatut.setText("Partie terminée - Vainqueur: " + classement.get(0).getNom());
            }

            // Afficher dialogue de fin
            StringBuilder message = new StringBuilder("Classement final:\n\n");
            for (int i = 0; i < classement.size(); i++) {
                Joueur j = classement.get(i);
                message.append(i + 1).append(". ").append(j.getNom())
                       .append(": ").append(j.getScore()).append(" points\n");
            }

            JOptionPane.showMessageDialog(this, message.toString(), "Partie terminée", 
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void notifierSauvegarde(String nomSauvegarde) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("✓ Partie sauvegardée: " + nomSauvegarde);
            JOptionPane.showMessageDialog(this, "Partie sauvegardée avec succès!", 
                    "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void notifierChargement(String nomSauvegarde) {
        SwingUtilities.invokeLater(() -> ajouterLog("✓ Partie chargée: " + nomSauvegarde));
    }

    @Override
    public void notifierDetailsInitialisation(Extension extension, List<Trophee> trophees, int taillePioche) {
        SwingUtilities.invokeLater(() -> {
            if (extension != null) {
                ajouterLog("Extension: " + extension.getNom());
            }
            ajouterLog("Pioche: " + taillePioche + " cartes");
        });
    }

    @Override
    public void notifierRecuperationDernieresCartes(Map<Joueur, Carte> recuperations) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("Récupération des dernières cartes...");
            for (Map.Entry<Joueur, Carte> entry : recuperations.entrySet()) {
                ajouterLog("  " + entry.getKey().getNom() + " récupère " + 
                          entry.getValue().toStringCourt());
            }
        });
    }

    @Override
    public void notifierRevelationJests(List<Joueur> joueurs) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("\n--- Révélation des Jests ---");
            // Mettre à jour les panneaux des joueurs
            panneauJoueurs.removeAll();
            for (Joueur joueur : joueurs) {
                JPanel panneau = PanneauFactory.creerPanneauJoueur(joueur);
                panneauxJoueurs.put(joueur, panneau);
                panneauJoueurs.add(panneau);
            }
            panneauJoueurs.revalidate();
            panneauJoueurs.repaint();
        });
    }

    @Override
    public void notifierScoresBase(Map<Joueur, Integer> scores, Map<Joueur, String> details) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("\n--- Calcul des scores de base ---");
            for (Map.Entry<Joueur, Integer> entry : scores.entrySet()) {
                ajouterLog(entry.getKey().getNom() + ": " + entry.getValue() + " points");
            }
        });
    }

    @Override
    public void notifierAttributionTrophee(Trophee trophee, Joueur gagnant) {
        SwingUtilities.invokeLater(() -> {
            String message = "Trophée " + trophee.getCondition().getDescription();
            if (gagnant != null) {
                message += " -> " + gagnant.getNom();
            } else {
                message += " -> Aucun gagnant";
            }
            ajouterLog(message);
        });
    }

    @Override
    public void notifierScoresFinaux(Map<Joueur, Integer> scores, Map<Joueur, String> details) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog("\n--- Scores finaux (avec trophées) ---");
            for (Map.Entry<Joueur, Integer> entry : scores.entrySet()) {
                ajouterLog(entry.getKey().getNom() + ": " + entry.getValue() + " points");
            }
        });
    }

    @Override
    public Carte choisirCarteOffre(String joueurNom, List<Carte> main) {
        return dialogueChoix.choisirCarteOffre(joueurNom, main);
    }

    @Override
    public Offre choisirOffreCible(String joueurNom, List<Offre> offres) {
        return dialogueChoix.choisirOffreCible(joueurNom, offres);
    }

    @Override
    public Carte choisirCarteDansOffre(String joueurNom, Offre offre) {
        return dialogueChoix.choisirCarteDansOffre(joueurNom, offre);
    }
    
    /**
     * Annule les dialogues en cours.
     * Utilis\u00e9 pour la concurrence entre vues.
     */
    public void annulerDialogues() {
        dialogueChoix.annulerDialogue();
    }
}
