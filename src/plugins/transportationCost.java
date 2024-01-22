package plugins;

import java.util.Random;

import Appli.data.Commande;
import interfaces.ICost;

public class transportationCost implements ICost {

	@Override
	public void update_ht(Commande c) {
		// TODO Auto-generated method stub
		Random r = new Random();
		c.setTransportation_cost(r.nextFloat());
		
	}

	@Override
	public void update_ttc(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		c.setTransportation_cost(c.getProduction_cost() * (1+taxes));
	}

	@Override
	public Float get_ht(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		return c.getTransportation_cost()*(1-taxes);
	}

	@Override
	public Float get_ttc(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		return c.getTransportation_cost();
		
	}

}
