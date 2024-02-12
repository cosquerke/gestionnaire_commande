package interfaces;

import Appli.data.Commande;
import exception.MissingParameterException;

import java.util.List;

/**
* L'interface définit la méthode nécessaire pour exporter une liste de commandes.
*/
public interface CommandeExporterInterface {
   /**
    * Exporte la liste des commandes.
    * @param commandes La liste des commandes à exporter.
    * @throws MissingParameterException exception si un paramètre obligatoire n'est pas spécifié
    */
   void exportCommandes(List<Commande> commandes) throws MissingParameterException;
}
