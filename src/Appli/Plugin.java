package Appli;

import java.util.Map;

/**
 * Classe abstraite regroupant tous les plugins implémentables par l'application
 */
public abstract class Plugin {
    protected Map<String, String> parameters;

    /**
     * Ajoute les paramètres au plugin
     *
     * @param parameters les paramètres compris dans le JSON du plugin
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
