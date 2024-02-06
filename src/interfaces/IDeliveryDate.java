package interfaces;

import java.text.SimpleDateFormat;
import java.util.Date;

import Appli.data.Commande;
import util.DateFormat;

public interface IDeliveryDate {
	Integer maxDays = 200;
	Integer minDays = 1;
	public Date get_delivery_date(Commande c,  Integer min, Integer max);
	public void update_delivery_date(Commande c,  Integer min, Integer max);
}
