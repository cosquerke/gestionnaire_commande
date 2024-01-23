package plugins;

import java.util.List;
import java.util.Random;

import Appli.data.Commande;
import interfaces.ICost;
import interfaces.IMainAppPlugin;

public class ProductionCost implements ICost, IMainAppPlugin {

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
        c.setProduction_cost(r.nextFloat());
    }

    @Override
    public void update_ttc(Commande c, Float taxes) {
        c.setProduction_cost(c.getProduction_cost() * (1 + taxes));
    }

    @Override
    public Float get_ht(Commande c, Float taxes) {
        System.out.println(c.getProduction_cost() * (1 - taxes));
        return c.getProduction_cost() * (1 - taxes);
    }

    @Override
    public Float get_ttc(Commande c, Float taxes) {
        return c.getProduction_cost();
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public void setName(String name) {
        this.Name = name;
    }

    @Override
    public String getDescription() {
        return Description;
    }

    @Override
    public void setDescription(String description) {
        this.Description = description;
    }

    @Override
    public List<String> getDependencyList() {
        return DependenciesList;
    }

    @Override
    public int getMaxInstanceNumber() {
        return MaxInstanceNumber;
    }

    @Override
    public void setMaxInstanceNumber(int maxInstanceNumber) {
        this.MaxInstanceNumber = maxInstanceNumber;
    }

    @Override
    public int getMinInstanceNumber() {
        return MinInstanceNumber;
    }

    @Override
    public void setMinInstanceNumber(int minInstanceNumber) {
        this.MinInstanceNumber = minInstanceNumber;
    }

    @Override
    public boolean isLoad() {
        return Load;
    }

    @Override
    public void setLoad(boolean load) {
        this.Load = load;
    }

	@Override
	public String getEmplacement() {
		return this.Emplacement;
	}

	@Override
	public void setEmplacement(String emplacement) {
		this.Emplacement = emplacement;	
	}
}
