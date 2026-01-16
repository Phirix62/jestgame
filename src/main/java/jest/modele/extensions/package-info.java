/**
 * Extensions et variantes de règles.
 * 
 * <h2>Extensions</h2>
 * <p>Les extensions ajoutent de nouvelles cartes spéciales au jeu de base.</p>
 * <ul>
 *   <li>{@link jest.modele.extensions.Extension} - Interface des extensions</li>
 *   <li>{@link jest.modele.extensions.ExtensionMagique} - Cartes magiques (Multiplicateur, Bouclier, etc.)</li>
 * </ul>
 * 
 * <h2>Variantes</h2>
 * <p>Les variantes modifient les règles de déroulement de la partie.</p>
 * <ul>
 *   <li>{@link jest.modele.extensions.Variante} - Interface des variantes</li>
 *   <li>{@link jest.modele.extensions.VarianteStandard} - Règles classiques (2 cartes/main)</li>
 *   <li>{@link jest.modele.extensions.VarianteTactique} - Règles tactiques (3 cartes/main)</li>
 *   <li>{@link jest.modele.extensions.VarianteRapide} - Partie rapide (3 tours max)</li>
 * </ul>
 */
package jest.modele.extensions;
