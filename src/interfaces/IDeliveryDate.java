package interfaces;

import java.util.Date;

import Appli.data.Commande;

/**
 * L'interface définit les méthodes pour la récupération et la mise à jour
 * de la date de livraison d'une commande en fonction de la durée de l'événement.
 */
public interface IDeliveryDate {
    /**
     * Récupère la date de livraison prévue pour la commande spécifiée en tenant compte de la durée de l'événement.
     * @param c La commande pour laquelle récupérer la date de livraison
     * @param eventDuration La durée de l'événement en jours.
     * @return La date de livraison prévue.
     */
    public Date get_delivery_date(Commande c, Integer eventDuration);

    /**
     * Met à jour la date de livraison de la commande spécifiée en tenant compte de la durée de l'événement.
     * @param c La commande à mettre à jour.
     * @param eventDuration La durée de l'événement en jours.
     */
    public void update_delivery_date(Commande c, Integer eventDuration);
}
