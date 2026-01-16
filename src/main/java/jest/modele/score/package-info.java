/**
 * Calcul des scores (pattern Visitor).
 * 
 * <h2>Pattern Visitor</h2>
 * <p>Le calcul des scores utilise le pattern Visitor pour séparer l'algorithme
 * de calcul des structures de données (cartes). Pour permettre d'ajouter de nouveaux
 * calculs sans modifier les classes de cartes.</p>
 * 
 * <h2>Classes principales</h2>
 * <ul>
 *   <li>{@link jest.modele.score.CalculateurScore} - Calcule le score total d'un Jest</li>
 *   <li>{@link jest.modele.score.VisiteurScore} - Interface Visitor pour le calcul</li>
 * </ul>
 * 
 * <h2>Règles de scoring</h2>
 * <p>Le score d'un Jest dépend :</p>
 * <ul>
 *   <li>De la valeur faciale des cartes</li>
 *   <li>De la couleur des cartes</li>
 *   <li>Des trophées obtenus</li>
 *   <li>Des cartes spéciales (Joker, As, extensions)</li>
 *   <li>Certaines règles spécifiques (paires noires)</li>
 * </ul>
 */
package jest.modele.score;
