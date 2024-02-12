package interfaces;

import Appli.data.Commande;

/**
 * L'interface définit les méthodes pour la mise à jour et la récupération des coûts HT et TTC d'une commande.
 */
public interface ICost {
    /**
     * Met à jour le montant HT de la commande spécifiée.
     * @param c La commande à mettre à jour
     */
    public void update_ht(Commande c);

    /**
     * Met à jour le montant TTC de la commande spécifiée en tenant compte des taxes.
     * @param c La commande à mettre à jour.
     * @param taxes Les taxes à appliquer.
     */
    public void update_ttc(Commande c, Float taxes);

    /**
     * Récupère le montant HT de la commande spécifiée en tenant compte des taxes.
     *
     * @param c La commande pour laquelle récupérer le montant HT.
     * @param taxes Les taxes à appliquer.
     * @return Le montant HT de la commande.
     */
    public Double get_ht(Commande c, Float taxes);

    /**
     * Récupère le montant TTC de la commande spécifiée en tenant compte des taxes.
     *
     * @param c La commande pour laquelle récupérer le montant TTC.
     * @param taxes  Les taxes à appliquer.
     * @return Le montant TTC de la commande.
     */
    public Double get_ttc(Commande c, Float taxes);
}
