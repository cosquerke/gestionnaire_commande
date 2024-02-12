package interfaces;

import java.util.List;

import Appli.data.Commande;

/**
 * L'interface définit une méthode pour l'exécution d'un plugin sur une liste de commandes.
 */
public interface IPluginInterface {
    /**
     * Exécute le plugin sur la liste de commandes spécifiée.
     * @param listeCommande La liste de commandes sur laquelle exécuter le plugin.
     * @param eventDuration La durée de l'événement en jours.
     * @return La liste de commandes mise à jour après l'exécution du plugin.
     */
    public List<Commande> executePlugin(List<Commande> listeCommande, Integer eventDuration);
}
