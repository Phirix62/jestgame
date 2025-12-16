package jest.modele.cartes;

/**
 * Énumération représentant les quatre couleurs des cartes.
 * Hiérarchie de force : PIQUE > TREFLE > CARREAU > COEUR
 */
public enum Couleur {
    PIQUE(4, "_PI"),
    TREFLE(3, "_TR"),
    CARREAU(2, "_CA"),
    COEUR(1, "_CO"),
    SPECIALE(0, "_SP"); // Pour les cartes spéciales (Joker et extensions)
    
    private final int force;
    private final String symbole;
    
    /**
     * Constructeur de Couleur.
     * @param force Force relative de la couleur (4 = plus forte)
     * @param symbole Symbole Unicode pour l'affichage
     */
    Couleur(int force, String symbole) {
        this.force = force;
        this.symbole = symbole;
    }
    
    /**
     * Retourne la force de la couleur.
     * @return Force (1-4, 4 étant la plus forte)
     */
    public int getForce() {
        return force;
    }
    
    /**
     * Retourne le symbole de la couleur.
     * @return Symbole Unicode
     */
    public String getSymbole() {
        return symbole;
    }
    
    /**
     * Compare cette couleur avec une autre selon la hiérarchie.
     * @param autre Couleur à comparer
     * @return Valeur négative si this < autre, 0 si égale, positive si this > autre
     */
    public int compareForce(Couleur autre) {
        return Integer.compare(this.force, autre.force);
    }
    
    @Override
    public String toString() {
        return this.name();
    }
}