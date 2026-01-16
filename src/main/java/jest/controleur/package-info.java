/**
 * Contrôleur et orchestration de l'application.
 * 
 * <h2>Responsabilité</h2>
 * <p>Le contrôleur gère la communication entre le modèle et les vues.
 * Il orchestre le déroulement de l'application (menus, configuration, boucle de jeu).</p>
 * 
 * <h2>Classes principales</h2>
 * <ul>
 *   <li>{@link jest.controleur.ControleurJeu} - <b>Contrôleur principal</b> : délègue au modèle</li>
 *   <li>{@link jest.controleur.GestionnaireBoucleJeu} - Boucle de jeu et menus</li>
 *   <li>{@link jest.controleur.ConfigurateurPartie} - Configuration interactive (nombre joueurs, etc.)</li>
 *   <li>{@link jest.controleur.LecteurEntree} - Utilitaire de lecture des entrées utilisateur</li>
 * </ul>
 * 
 * <h2>Pattern Observer</h2>
 * <ul>
 *   <li>{@link jest.controleur.ObservateurPartie} - Interface pour observer les événements du modèle</li>
 * </ul>
 */
package jest.controleur;
