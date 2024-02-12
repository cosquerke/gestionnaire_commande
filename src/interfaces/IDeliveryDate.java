package interfaces;

import java.util.Date;

import Appli.data.Commande;

public interface IDeliveryDate {
	public Date get_delivery_date(Commande c,  Integer eventDuration);
	public void update_delivery_date(Commande c,  Integer eventDuration);
}
