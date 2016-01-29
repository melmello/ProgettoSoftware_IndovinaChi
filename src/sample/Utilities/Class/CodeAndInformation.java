package sample.Utilities.Class;

/** @author Giulio Melloni
 * Classe che serve per la costruzione di un codice comune di comunicazione tra server e client.
 * Questo codice è suddiviso in due parti: code e information.
 * Con code posso avere un comportamento determinato definito all'interno dello switch del thread del relativo Server o Client.
 * Con information posso portarmi dietro tra i due una variabile, oppure un oggetto (a patto che sia serializzato).
 */

import com.google.gson.Gson;
import java.io.Serializable;

public class CodeAndInformation implements Serializable{

    private String code;
    private String information;

    /** Costruttore di CodeAndInformation.
     * @param code è il codice vero e proprio che fa riferimento a ConstantCodes.
     * @param information è l'informazione (spesso serializzata).
     */
    public CodeAndInformation(String code, String information) {
        this.code = code;
        this.information = information;
    }

    /** Costruttore vuoto di CodeAndInformation.
     */
    public CodeAndInformation() {
    }

    /** toString utilizzato per l'implementazione della serializzazione.
     * @return serializzazione dell'oggetto.
     */
    @Override
    public String toString() {
        return "CodeAndInformation{" +
                "code='" + code + '\'' +
                ", information='" + information + '\'' +
                '}';
    }

    /** getter.
     * @return codice.
     */
    public String getCode() {
        return code;
    }

    /** setter.
     * @param code è il codice.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /** getter.
     * @return informazione.
     */
    public String getInformation() {
        return information;
    }

    /** metodo statico utilizzato per creare una serializzazione veloce.
     * @param firstParameter è il codice passato.
     * @param secondParameter è l'informazione passata.
     * @return serializzazione di quest'oggetto.
     */
    public static String serializeToJson(String firstParameter, String secondParameter){
        CodeAndInformation codeAndInformation = new CodeAndInformation(firstParameter, secondParameter);
        Gson gson = new Gson();
        String jsonString = gson.toJson(codeAndInformation);
        return jsonString;
    }

}
