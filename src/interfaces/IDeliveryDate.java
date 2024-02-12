package interfaces;

import java.util.Date;

import Appli.data.Commande;

public interface IDeliveryDate {
	Integer maxDays = 200;
	Integer minDays = 1;
	public Date get_delivery_date(Commande c,  Integer min, Integer max);
	public void update_delivery_date(Commande c,  Integer min, Integer max);
}
