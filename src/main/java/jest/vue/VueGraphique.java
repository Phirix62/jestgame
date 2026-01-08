package jest.vue;

import jest.controleur.ControleurJeu;
import jest.controleur.ObservateurPartie;
import jest.modele.joueurs.*;
import jest.modele.extensions.*;
import jest.modele.cartes.Carte;
import jest.modele.cartes.Couleur;
import jest.modele.jeu.Offre;

import javax.swing.*;
import javax.swing.border.*;
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
    private JPanel panneauPrincipal;
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
    private int nombreTrophees;

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

        // Layout principal
        panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        panneauPrincipal.setBackground(new Color(0, 100, 0)); // Vert comme table de jeu

        // Panneau supérieur (info et tour)
        JPanel panneauHaut = creerPanneauHaut();
        panneauPrincipal.add(panneauHaut, BorderLayout.NORTH);

        // Panneau central (joueurs et offres)
        JPanel panneauCentre = creerPanneauCentre();
        panneauPrincipal.add(panneauCentre, BorderLayout.CENTER);

        // Panneau droit (log)
        JPanel panneauDroit = creerPanneauLog();
        panneauPrincipal.add(panneauDroit, BorderLayout.EAST);

        // Panneau bas (contrôles)
        panneauControles = creerPanneauControles();
        panneauPrincipal.add(panneauControles, BorderLayout.SOUTH);

        add(panneauPrincipal);
    }

    /**
     * Crée le panneau supérieur avec les informations de la partie.
     */
    private JPanel creerPanneauHaut() {
        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setBackground(new Color(50, 50, 50));
        panneau.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titre = new JLabel("JEST", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);

        labelTour = new JLabel("Tour: -", SwingConstants.CENTER);
        labelTour.setFont(new Font("Arial", Font.PLAIN, 18));
        labelTour.setForeground(Color.YELLOW);

        labelStatut = new JLabel("En attente...", SwingConstants.CENTER);
        labelStatut.setFont(new Font("Arial", Font.PLAIN, 14));
        labelStatut.setForeground(Color.LIGHT_GRAY);

        panneau.add(titre, BorderLayout.WEST);
        panneau.add(labelTour, BorderLayout.CENTER);
        panneau.add(labelStatut, BorderLayout.EAST);

        return panneau;
    }

    /**
     * Crée le panneau central avec les joueurs et les offres.
     */
    private JPanel creerPanneauCentre() {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setOpaque(false);

        // Panneau des joueurs (haut)
        panneauJoueurs = new JPanel(new GridLayout(1, 4, 10, 10));
        panneauJoueurs.setOpaque(false);
        panneauJoueurs.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Joueurs et leurs Jests",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE
        ));

        // Panneau des offres (centre)
        panneauOffres = new JPanel(new GridLayout(1, 4, 10, 10));
        panneauOffres.setOpaque(false);
        panneauOffres.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Offres du tour",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE
        ));
        
        // Panneau des trophées (bas)
        panneauTrophees = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panneauTrophees.setOpaque(false);
        panneauTrophees.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.YELLOW, 2),
                "Trophées à gagner",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.YELLOW
        ));

        panneau.add(panneauJoueurs, BorderLayout.NORTH);
        panneau.add(panneauOffres, BorderLayout.CENTER);
        panneau.add(panneauTrophees, BorderLayout.SOUTH);

        return panneau;
    }

    /**
     * Crée le panneau de log.
     */
    private JPanel creerPanneauLog() {
        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setPreferredSize(new Dimension(300, 0));
        panneau.setBackground(Color.WHITE);

        zoneLog = new JTextArea();
        zoneLog.setEditable(false);
        zoneLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        zoneLog.setLineWrap(true);
        zoneLog.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(zoneLog);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Journal de la partie"));

        panneau.add(scrollPane, BorderLayout.CENTER);

        return panneau;
    }

    /**
     * Crée le panneau de contrôles.
     */
    private JPanel creerPanneauControles() {
        JPanel panneau = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panneau.setBackground(new Color(50, 50, 50));

        JButton btnProchainTour = new JButton("Prochain Tour");
        btnProchainTour.addActionListener(e -> executerProchainTour());

        JButton btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.setEnabled(false); // Désactivé par défaut
        btnSauvegarder.addActionListener(e -> sauvegarderPartie());

        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(e -> quitter());

        panneau.add(btnProchainTour);
        panneau.add(btnSauvegarder);
        panneau.add(btnQuitter);
        
        // Stocker la référence pour pouvoir activer/désactiver
        panneau.putClientProperty("btnSauvegarder", btnSauvegarder);

        return panneau;
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
        // Cette méthode n'est pas utilisée par la GUI - 
        // La configuration se fait via la vue terminal
        return null;
    }

    @Override
    public String choisirSauvegarde(List<String> sauvegardes) {
        // Non utilisé par la GUI
        return null;
    }

    @Override
    public String demanderNomSauvegarde() {
        // Non utilisé par la GUI
        return null;
    }

    @Override
    public void afficherMessage(String message) {
        ajouterLog("[INFO] " + message);
        SwingUtilities.invokeLater(() -> {
            labelStatut.setText(message);
        });
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
            
            this.nombreTrophees = nbTrophees;

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
        SwingUtilities.invokeLater(() -> {
            ajouterLog("Distribution des cartes...");
        });
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
            JPanel ancienPanneau = panneauxJoueurs.get(joueur);
            if (ancienPanneau != null) {
                JPanel nouveauPanneau = PanneauFactory.creerPanneauJoueur(joueur);
                panneauxJoueurs.put(joueur, nouveauPanneau);

                // Remplacer le panneau dans l'interface
                panneauJoueurs.removeAll();
                for (Joueur j : controleur.getJoueurs()) {
                    JPanel panneauJ = panneauxJoueurs.get(j);
                    if (panneauJ != null) {
                        panneauJoueurs.add(panneauJ);
                    }
                }
                panneauJoueurs.revalidate();
                panneauJoueurs.repaint();
            }
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
            ajouterLog(" Partie sauvegardée: " + nomSauvegarde);
            JOptionPane.showMessageDialog(this, "Partie sauvegardée avec succès!", 
                    "Sauvegarde", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Override
    public void notifierChargement(String nomSauvegarde) {
        SwingUtilities.invokeLater(() -> {
            ajouterLog(" Partie chargée: " + nomSauvegarde);
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
}
