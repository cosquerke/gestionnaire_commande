package interfaces;

import Appli.data.Commande;

import java.util.List;

/**
 * L'interface définit la méthode nécessaire pour importer une liste de commandes.
 */
public interface CommandeImporterInterface {
    /**
     * Importe une liste de commandes.
     * @return La liste des commandes importées.
     */
    List<Commande> importCommandes();
}
