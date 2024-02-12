package interfaces;

import java.util.List;

import Appli.data.Commande;

public interface IPluginInterface {
	public List<Commande> executePlugin(List<Commande> listeCommande, Integer eventDuration);
}
