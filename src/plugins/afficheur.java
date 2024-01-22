package plugins;

import Appli.data.Personne;
import interfaces.IAfficheur;

public class afficheur implements IAfficheur {

	@Override
	public void affiche(Personne p) {
		// TODO Auto-generated method stub
		System.out.println(p.toString());
	}

}
