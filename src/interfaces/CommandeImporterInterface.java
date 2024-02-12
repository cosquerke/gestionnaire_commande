package interfaces;

import Appli.data.Commande;
import exception.MissingParameterException;

import java.util.List;

/**
 * L'interface définit la méthode nécessaire pour importer une liste de commandes.
 */
public interface CommandeImporterInterface {
    /**
     * Importe une liste de commandes.
     * @return La liste des commandes importées.
     * @throws MissingParameterException exception si un paramètre obligatoire n'est pas spécifié
     */
    List<Commande> importCommandes() throws MissingParameterException;
}
