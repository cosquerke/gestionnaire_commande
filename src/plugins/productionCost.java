package plugins;

import java.util.Random;

import Appli.data.Commande;
import interfaces.IProductionCost;

public class productionCost implements IProductionCost {

	@Override
	public void update_ht(Commande c) {
		// TODO Auto-generated method stub
		Random r = new Random();
		c.setProduction_cost(r.nextFloat());
		
	}

	@Override
	public void update_ttc(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		c.setProduction_cost(c.getProduction_cost() * (1+taxes));
	}

	@Override
	public Float get_ht(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		System.out.println(c.getProduction_cost()*(1-taxes));
		return c.getProduction_cost()*(1-taxes);
	}

	@Override
	public Float get_ttc(Commande c, Float taxes) {
		// TODO Auto-generated method stub
		return c.getProduction_cost();
		
	}

}
