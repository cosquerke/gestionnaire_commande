package plugins;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import Appli.data.Commande;
import interfaces.IDeliveryDate;
import interfaces.IPluginInterface;
import util.DateFormat;

public class StrikeEvent implements IDeliveryDate, IPluginInterface {

	@Override
	public Date get_delivery_date(Commande c, Integer min, Integer max) {
		// TODO Auto-generated method stub
		Integer nbDays;
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(c.getExpected_delivery_date());
        
		if ((min == 0) && (max == 0)) {
			// Mode aléatoire
			nbDays = (int) (Math.random() * (maxDays - minDays + 1) + minDays);
		}else {
			// Mode aléatoire borné
			nbDays = (int) (Math.random() * (max - min + 1) + min);
		}
		
		calendar.add(Calendar.DAY_OF_MONTH, nbDays);
		
		return calendar.getTime();
	}

	@Override
	public void update_delivery_date(Commande c, Integer min, Integer max) {
		// TODO Auto-generated method stub
		c.setExpected_delivery_date(this.get_delivery_date(c, min, max));
		
	}

	@Override
	public List<Commande> executePlugin(List<Commande> listeCommande) {
		// TODO Auto-generated method stub
		return null;
	}


}
