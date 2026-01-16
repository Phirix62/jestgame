/**
 * Modèle du jeu Jest (couche métier).
 * 
 * <h2>Responsabilité</h2>
 * <p>Contient toute la logique métier du jeu Jest. Le modèle est complètement indépendant
 * de la vue et du contrôleur, garantissant ainsi une séparation stricte des préoccupations.</p>
 * 
 * <h2>Sous-packages</h2>
 * <ul>
 *   <li>{@link jest.modele.cartes} - Cartes, paquet, trophées</li>
 *   <li>{@link jest.modele.jeu} - Moteur de jeu (parties, tours, offres)</li>
 *   <li>{@link jest.modele.joueurs} - Joueurs et stratégies IA</li>
 *   <li>{@link jest.modele.score} - Calcul des scores (pattern Visitor)</li>
 *   <li>{@link jest.modele.extensions} - Extensions et variantes</li>
 *   <li>{@link jest.modele.utilitaires} - Utilitaires (sauvegarde, classement)</li>
 * </ul>
 */
package jest.modele;
