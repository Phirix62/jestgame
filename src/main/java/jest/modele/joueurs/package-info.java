/**
 * Joueurs et stratégies IA.
 * 
 * <h2>Types de joueurs</h2>
 * <ul>
 *   <li>{@link jest.modele.joueurs.Joueur} - Classe abstraite pour tous les joueurs</li>
 *   <li>{@link jest.modele.joueurs.JoueurPhysique} - Joueur humain (utilise les vues)</li>
 *   <li>{@link jest.modele.joueurs.JoueurVirtuel} - Joueur IA (utilise une stratégie)</li>
 * </ul>
 * 
 * <h2>Pattern Strategy</h2>
 * <p>Les stratégies IA sont interchangeables grâce au pattern Strategy.
 * Chaque IA implémente l'interface {@link jest.modele.joueurs.Strategie}.</p>
 * 
 * <h3>Stratégies disponibles</h3>
 * <ul>
 *   <li>{@link jest.modele.joueurs.StrategieAleatoire} - Choix aléatoires</li>
 *   <li>{@link jest.modele.joueurs.StrategieGloutonne} - Privilégie les hautes valeurs faciales</li>
 *   <li>{@link jest.modele.joueurs.StrategieDefensive} - Évite les carreaux négatifs</li>
 * </ul>
 * 
 * <h2>Collection de cartes</h2>
 * <ul>
 *   <li>{@link jest.modele.joueurs.Jest} - Collection de cartes d'un joueur</li>
 * </ul>
 */
package jest.modele.joueurs;
