/**
 * Moteur du jeu Jest.
 * 
 * <h2>Responsabilité</h2>
 * <p>Orchestre le déroulement complet d'une partie de Jest : initialisation,
 * exécution des tours, distribution des cartes, attribution des trophées et calcul des scores.</p>
 * 
 * <h2>Classes principales</h2>
 * <ul>
 *   <li>{@link jest.modele.jeu.Partie} - <b>Façade principale</b> : orchestre une partie complète</li>
 *   <li>{@link jest.modele.jeu.Tour} - Représente un tour de jeu (distribution, offres, prises)</li>
 *   <li>{@link jest.modele.jeu.Offre} - Offre d'un joueur (cartes visibles + cachée)</li>
 *   <li>{@link jest.modele.jeu.Pioche} - Pioche de cartes</li>
 * </ul>
 */
package jest.modele.jeu;
