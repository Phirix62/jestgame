package jest.vue.graphique;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Classe utilitaire pour construire les composants de l'interface graphique.
 * Centralise la création des panneaux et des composants Swing.
 */
public class ConstructeurInterfaceGraphique {
    
    /**
     * Crée le panneau principal avec le layout de base.
     * 
     * @return Panneau principal configuré
     */
    public static JPanel creerPanneauPrincipal() {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setBorder(new EmptyBorder(10, 10, 10, 10));
        panneau.setBackground(new Color(0, 100, 0)); // Vert comme table de jeu
        return panneau;
    }

    /**
     * Crée le panneau supérieur avec les informations de la partie.
     * 
     * @param titre Label du titre
     * @param labelTour Label du tour
     * @param labelStatut Label du statut
     * @return Panneau supérieur configuré
     */
    public static JPanel creerPanneauHaut(JLabel titre, JLabel labelTour, JLabel labelStatut) {
        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setBackground(new Color(50, 50, 50));
        panneau.setBorder(new EmptyBorder(10, 10, 10, 10));

        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);

        labelTour.setFont(new Font("Arial", Font.PLAIN, 18));
        labelTour.setForeground(Color.YELLOW);

        labelStatut.setFont(new Font("Arial", Font.PLAIN, 14));
        labelStatut.setForeground(Color.LIGHT_GRAY);

        panneau.add(titre, BorderLayout.WEST);
        panneau.add(labelTour, BorderLayout.CENTER);
        panneau.add(labelStatut, BorderLayout.EAST);

        return panneau;
    }

    /**
     * Crée le panneau central avec les joueurs et les offres.
     * 
     * @param panneauJoueurs Panneau des joueurs à intégrer
     * @param panneauOffres Panneau des offres à intégrer
     * @param panneauTrophees Panneau des trophées à intégrer
     * @return Panneau central configuré
     */
    public static JPanel creerPanneauCentre(JPanel panneauJoueurs, JPanel panneauOffres, JPanel panneauTrophees) {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setOpaque(false);

        // Configuration du panneau des joueurs
        panneauJoueurs.setLayout(new GridLayout(1, 4, 10, 10));
        panneauJoueurs.setOpaque(false);
        panneauJoueurs.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Joueurs et leurs Jests",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE
        ));

        // Configuration du panneau des offres
        panneauOffres.setLayout(new GridLayout(1, 4, 10, 10));
        panneauOffres.setOpaque(false);
        panneauOffres.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                "Offres du tour",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE
        ));

        // Configuration du panneau des trophées
        panneauTrophees.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
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
     * 
     * @param zoneLog Zone de texte pour le log
     * @return Panneau de log configuré
     */
    public static JPanel creerPanneauLog(JTextArea zoneLog) {
        JPanel panneau = new JPanel(new BorderLayout());
        panneau.setPreferredSize(new Dimension(300, 0));
        panneau.setBackground(Color.WHITE);

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
     * Crée le panneau de contrôles avec les boutons.
     * 
     * @param actionProchainTour Action pour le bouton "Prochain Tour"
     * @param actionSauvegarder Action pour le bouton "Sauvegarder"
     * @param actionQuitter Action pour le bouton "Quitter"
     * @return Panneau de contrôles configuré
     */
    public static JPanel creerPanneauControles(Runnable actionProchainTour, 
                                                Runnable actionSauvegarder, 
                                                Runnable actionQuitter) {
        JPanel panneau = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panneau.setBackground(new Color(50, 50, 50));

        JButton btnProchainTour = new JButton("Prochain Tour");
        btnProchainTour.addActionListener(e -> actionProchainTour.run());

        JButton btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.setEnabled(false); // Désactivé par défaut
        btnSauvegarder.addActionListener(e -> actionSauvegarder.run());

        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.addActionListener(e -> actionQuitter.run());

        panneau.add(btnProchainTour);
        panneau.add(btnSauvegarder);
        panneau.add(btnQuitter);

        // Stocker la référence pour pouvoir activer/désactiver
        panneau.putClientProperty("btnSauvegarder", btnSauvegarder);

        return panneau;
    }
}
