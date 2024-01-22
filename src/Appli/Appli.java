package Appli;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Appli.data.Commande;
import interfaces.ICost;
import loader.loader;

public class Appli {
	private ICost commande;

	public Appli() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, IOException {
		commande = loader.getInstance().getCommande();
	}

	public void cmd(Commande c) {
		this.commande.update_ht(c);
	}
	
	public static List<Commande> lireCommandesDepuisCSV(String path) {
        List<Commande> commandes = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
        	String ligne;
            boolean premiereLigne = true;
            boolean end = false;
            
            // Lire chaque ligne du fichier
            while ((ligne = br.readLine()) != null || !end) {
                if (premiereLigne) {
                    premiereLigne = false;
                    continue; // Passer à la prochaine itération sans traiter cette ligne
                }
                
                String[] champs = ligne.split(";");
                if(champs[0].length() == 0) {
                	end = true;
                }else {
                // Construire un objet Commande et l'ajouter à la liste
                commandes.add(new Commande(
                        Integer.parseInt(champs[0]),
                        champs[1],
                        champs[2],
                        dateFormat.parse(champs[3]),
                        dateFormat.parse(champs[4]),
                        dateFormat.parse(champs[5]),
                        Float.parseFloat(champs[6]),
                        Float.parseFloat(champs[7]),
                        Integer.parseInt(champs[8]),
                        Float.parseFloat(champs[9]),
                        Boolean.parseBoolean(champs[10]),
                        champs[11],
                        champs[12],
                        champs[13]
                ));
                
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return commandes;
    }
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		Appli mon_app = new Appli();
		
		Commande c = new Commande(0, "IT", "Puce electronique", new Date(2024,2,2), new Date(2024,1,2), new Date(2024,1,10), (float) 10.0, (float) 1.0, 0, (float) 20.0, false, "Taiwan", "China", "Boat");
		
		
		System.out.println(c.getProduction_cost());
		mon_app.cmd(c);
		System.out.println(c.getProduction_cost());
		
		
		List<Commande> commandes = lireCommandesDepuisCSV("src/Appli/data/commandes.csv");

		for (Commande commande : commandes) {
            System.out.println(commande.toString());
        }

		
		
		
	}

}
