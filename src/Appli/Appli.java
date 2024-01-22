package Appli;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import Appli.data.Commande;
import interfaces.IProductionCost;
import loader.loader;

public class Appli {
	private IProductionCost commande;

	public Appli() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, IOException {
		commande = loader.getInstance().getCommande();
	}

	public void cmd(Commande c) {
		this.commande.get_ht(c, (float) 0.1);
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		Appli mon_app = new Appli();
		Commande c = new Commande(0, "IT", "Puce electronique", new Date(2024,2,2), new Date(2024,1,2), new Date(2024,1,10), (float) 10.0, (float) 1.0, 0, (float) 20.0, false, "Taiwan", "China", "Boat");
		
		mon_app.cmd(c);
	}

}
