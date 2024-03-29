package interfaces;

import Appli.data.Commande;

public interface ISimulateEvent {
	// Permet de simuler l'impact d'un évenement sur une commande
	public Commande getEventImpact(Commande commande, IDeliveryDate deliveryDate, ICost transportation, ICost production, Integer eventDuration);
}
