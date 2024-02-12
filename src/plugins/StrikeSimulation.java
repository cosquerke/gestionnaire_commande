package plugins;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Appli.data.Commande;
import interfaces.ICost;
import interfaces.IDeliveryDate;
import interfaces.IPluginInterface;
import interfaces.ISimulateEvent;

public class StrikeSimulation implements ISimulateEvent, IPluginInterface {

	@Override
	public List<Commande> executePlugin(List<Commande> listeCommande) {
		// TODO Auto-generated method stub
		List<Commande> returnList = new ArrayList<Commande>();
		
		for (Commande c : listeCommande) {
			if ( ! c.getIs_delivered()) {
				// Objet IDeliveryDate StormEvent
				IDeliveryDate strikeEvent = new StrikeEvent();
				// Objet ICost Transportation ou Production
				ICost transportation = new TransportationCost();
				ICost production = new ProductionCost();
				Commande cmdTmp = this.getEventImpact(c, strikeEvent, transportation, production, 0, 10);
				returnList.add(cmdTmp);
			}else {
				returnList.add(c);
			}
			
		}
		
		return returnList;
	}

	@Override
	public Commande getEventImpact(Commande commande, IDeliveryDate deliveryDate, ICost transportation,
			ICost production, Integer min, Integer max) {
		// TODO Auto-generated method stub
		Double newProductionCost;
		Double newTransportationCost;
		Float taxes;
		Commande newCommande = (Commande) commande.clone();
		
		Date newDeliveryDate = deliveryDate.get_delivery_date(commande, min, max);
		
		newCommande.setExpected_delivery_date(newDeliveryDate);
		
		long interval = Math.abs(commande.getExpected_delivery_date().getTime() - newDeliveryDate.getTime());
        float differenceInDays = (float) TimeUnit.DAYS.convert(interval, TimeUnit.MILLISECONDS);
        
        taxes = differenceInDays / (100.0f);
        
        newProductionCost = production.get_ht(commande, taxes);
    	newCommande.setProduction_cost(newProductionCost);
    	
    	newTransportationCost = transportation.get_ht(commande, taxes);
    	newCommande.setTransportation_cost(newTransportationCost);
        
		
		return newCommande;
	}

	@Override
	public void setEventImpact(Commande commande, IDeliveryDate deliveryDate, ICost cost, Integer min, Integer max) {
		// TODO Auto-generated method stub

	}

}
