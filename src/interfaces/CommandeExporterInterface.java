package interfaces;

import Appli.data.Commande;

import java.util.List;

/**
* L'interface définit la méthode nécessaire pour exporter une liste de commandes.
*/
public interface CommandeExporterInterface {
   /**
    * Exporte la liste des commandes.
    * @param commandes La liste des commandes à exporter.
    */
   void exportCommandes(List<Commande> commandes);
}
