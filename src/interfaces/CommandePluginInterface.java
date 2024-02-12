package interfaces;

import java.util.List;

import Appli.data.Commande;
import exception.MissingParameterException;

/**
 * L'interface définit une méthode pour l'exécution d'un plugin sur une liste de commandes.
 */
public interface CommandePluginInterface {
    /**
     * Exécute le plugin sur la liste de commandes spécifiée.
     * @param listeCommande La liste de commandes sur laquelle exécuter le plugin.
     * @return La liste de commandes mise à jour après l'exécution du plugin.
     * @throws MissingParameterException exception si un paramètre obligatoire n'est pas spécifié
     */
    public List<Commande> executePlugin(List<Commande> listeCommande) throws MissingParameterException;
}
