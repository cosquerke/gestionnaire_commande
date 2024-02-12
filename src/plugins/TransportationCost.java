package plugins;

import java.util.List;
import java.util.Random;

import Appli.data.Commande;
import interfaces.ICost;
import interfaces.IPluginInterface;

public class TransportationCost implements ICost {

	@Override
	public void update_ht(Commande c) {
		// TODO Auto-generated method stub
		Random r = new Random();
		c.setTransportation_cost(r.nextDouble());
		
	}

	@Override
	public void update_ttc(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		c.setTransportation_cost(c.getProduction_cost() * (1+taxes));
	}

	@Override
	public Double get_ht(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		return c.getTransportation_cost()*(1-taxes);
	}

	@Override
	public Double get_ttc(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		return c.getTransportation_cost();
		
	}


}
