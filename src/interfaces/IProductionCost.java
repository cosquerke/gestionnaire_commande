package interfaces;

import Appli.data.Commande;

public interface IProductionCost {
	public void update_ht(Commande c);
	public void update_ttc(Commande c, Float taxes);
	public Float get_ht(Commande c, Float taxes);
	public Float get_ttc(Commande c, Float taxes);
	
}
