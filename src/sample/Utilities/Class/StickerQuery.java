package sample.Utilities.Class;

/** @author Giulio Melloni
 * Classe che mi permette di creare un oggetto utile per la serializzazione e deserializzazione tra server e client.
 * Con essa identifico attributo e tuple dal database Sticker.
 */

import java.io.Serializable;

public class StickerQuery implements Serializable{

    String firstParameter;
    String secondParameter;

    /** Costruttore di StickerQuery.
     * @param firstParameter è il parametro con cui identifico l'attributo del mio database.
     * @param secondParameter è il parametro con cui identifico la tupla del mio database.
     */
    public StickerQuery(String firstParameter, String secondParameter) {
        this.firstParameter = firstParameter;
        this.secondParameter = secondParameter;
    }

    /** Costruttore vuoto di StickerQuery.
     */
    public StickerQuery() {
    }

    /** toString di StickerQuery per la sua serializzazione.
     * @return la serializzazione.
     */
    @Override
    public String toString() {
        return "StickerQuery{" +
                "firstParameter='" + firstParameter + '\'' +
                ", secondParameter='" + secondParameter + '\'' +
                '}';
    }

    /** getter.
     * @return nome dell'attributo del database Sticker.
     */
    public String getFirstParameter() {
        return firstParameter;
    }

    /** getter.
     * @return nome della tupla del database Sticker.
     */
    public String getSecondParameter() {
        return secondParameter;
    }

}
