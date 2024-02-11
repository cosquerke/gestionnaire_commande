package plugins;

import java.util.List;
import java.util.Random;

import Appli.data.Commande;
import interfaces.ICost;
import interfaces.IMainAppPlugin;
import interfaces.IPluginInterface;

public class ProductionCost implements ICost, IPluginInterface {

    private String Name;
    private String Description;
    private List<String> DependenciesList;
    private int MaxInstanceNumber;
    private int MinInstanceNumber;
    private boolean Load;
    private String Emplacement;

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
        System.out.println(c.getProduction_cost() * (1 - taxes));
        return c.getProduction_cost() * (1 - taxes);
    }

    @Override
    public Double get_ttc(Commande c, Float taxes) {
        return c.getProduction_cost();
    }


	@Override
	public List<Commande> executePlugin(List<Commande> listeCommande) {
		// TODO Auto-generated method stub
		return null;
	}
}
