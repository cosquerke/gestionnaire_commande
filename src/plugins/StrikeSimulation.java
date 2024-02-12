package plugins;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Appli.Plugin;
import Appli.data.Commande;
import exception.MissingParameterException;
import interfaces.ICost;
import interfaces.IDeliveryDate;
import interfaces.IPluginInterface;
import interfaces.ISimulateEvent;

public class StrikeSimulation extends Plugin implements ISimulateEvent, IPluginInterface {

	private IDeliveryDate strikeEvent;
	private ICost transportationCost;
	private ICost productionCost;

	@Override
	public List<Commande> executePlugin(List<Commande> listeCommande) throws MissingParameterException {
		List<Commande> returnList = new ArrayList<Commande>();

		if (!this.parameters.containsKey("eventDuration")) {
			throw new MissingParameterException("Le paramètre eventDuration est nécessaire pour exécuter le plugin.");
		}

		Integer eventDuration = Integer.parseInt(this.parameters.get("eventDuration"));
		
		for (Commande c : listeCommande) {
			if ( ! c.getIs_delivered()) {
				Commande cmdTmp = this.getEventImpact(c, strikeEvent, transportationCost, productionCost, eventDuration);
				returnList.add(cmdTmp);
			}else {
				returnList.add(c);
			}	
		}
		
		return returnList;
	}

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
    	
    	newTransportationCost = transportation.get_ttc(commande, taxes) + interval/10000000;
    	newCommande.setTransportation_cost(newTransportationCost);
        
		return newCommande;
	}
	
	public void setStrikeEvent(IDeliveryDate strikeEvent) {
		this.strikeEvent = strikeEvent;
	}

	public void setTransportationCost(ICost transportationCost) {
		this.transportationCost = transportationCost;
	}

	public void setProductionCost(ICost productionCost) {
		this.productionCost = productionCost;
	}
}
