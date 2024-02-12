package plugins;

import java.util.Calendar;
import java.util.Date;

import Appli.data.Commande;
import interfaces.IDeliveryDate;

public class StrikeEvent implements IDeliveryDate {
	
	public static Integer minDays = 30;

	@Override
	public Date get_delivery_date(Commande c, Integer eventDuration) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(c.getExpected_delivery_date());
		
		calendar.add(Calendar.DAY_OF_MONTH, eventDuration);
		
		return calendar.getTime();
	}

	@Override
	public void update_delivery_date(Commande c, Integer eventDuration) {
		c.setExpected_delivery_date(this.get_delivery_date(c, eventDuration));
	}
}
