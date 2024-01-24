package interfaces;

import Appli.data.Commande;

public interface ICost {
	public void update_ht(Commande c);
	public void update_ttc(Commande c, Float taxes);
	public Double get_ht(Commande c, Float taxes);
	public Double get_ttc(Commande c, Float taxes);
	
}
