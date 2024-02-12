package plugins;

import java.util.Random;

import Appli.data.Commande;
import interfaces.ICost;

public class ProductionCost implements ICost {

    @Override
    public void update_ht(Commande c) {
        Random r = new Random();
        c.setProduction_cost(r.nextDouble());
    }

    @Override
    public void update_ttc(Commande c, Float taxes) {
        c.setProduction_cost(c.getProduction_cost() * (1 + taxes));
    }

    @Override
    public Double get_ht(Commande c, Float taxes) {
        return c.getProduction_cost() * (1 - taxes);
    }

    @Override
    public Double get_ttc(Commande c, Float taxes) {
        return c.getProduction_cost();
    }
}
