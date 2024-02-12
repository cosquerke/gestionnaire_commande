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

public class StormSimulation implements ISimulateEvent, IPluginInterface {

	private IDeliveryDate stormEvent;
	private ICost transportationCost;
	private ICost productionCost;
	
	@Override
	public Commande getEventImpact(Commande commande, IDeliveryDate deliveryDate, ICost transportation, ICost production, Integer eventDuration) {
		Double newProductionCost;
		Double newTransportationCost;
		Float taxes;
		Commande newCommande = (Commande) commande.clone();
		
		Date newDeliveryDate = deliveryDate.get_delivery_date(commande, eventDuration);
		
		newCommande.setExpected_delivery_date(newDeliveryDate);
		
		long interval = Math.abs(commande.getExpected_delivery_date().getTime() - newDeliveryDate.getTime());
        float differenceInDays = (float) TimeUnit.DAYS.convert(interval, TimeUnit.MILLISECONDS);
        
        taxes = differenceInDays / (100.0f);
        
        newProductionCost = production.get_ttc(commande, taxes) + interval/10000000;
    	newCommande.setProduction_cost(newProductionCost);
    	
    	newTransportationCost = transportation.get_ttc(commande, taxes)  + interval/10000000;
    	newCommande.setTransportation_cost(newTransportationCost);
		
		return newCommande;
	}

	@Override
	public List<Commande> executePlugin(List<Commande> listeCommande, Integer eventDuration) {
		List<Commande> returnList = new ArrayList<Commande>();
		
		for (Commande c : listeCommande) {
			if( ! c.getIs_delivered()) {
				Commande cmdTmp = this.getEventImpact(c, stormEvent, transportationCost, productionCost, eventDuration);
				returnList.add(cmdTmp);
			}else {
				returnList.add(c);
			}
		}
		
		return returnList;
	}

	public void setStormEvent(IDeliveryDate stormEvent) {
		this.stormEvent = stormEvent;
	}

	public void setTransportationCost(ICost transportationCost) {
		this.transportationCost = transportationCost;
	}

	public void setProductionCost(ICost productionCost) {
		this.productionCost = productionCost;
	}
}
