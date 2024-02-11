package plugins;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Appli.data.Commande;
import interfaces.ICost;
import interfaces.IDeliveryDate;
import interfaces.IPluginInterface;
import interfaces.ISimulateEvent;

public class StormSimulation implements ISimulateEvent, IPluginInterface {

	@Override
	public Commande getEventImpact(Commande commande, IDeliveryDate deliveryDate, ICost cost, Integer min, Integer max) {
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
        
        switch (cost.getClass().getName()) {
        case "ProductionCost":
        	newProductionCost = cost.get_ht(commande, taxes);
        	newCommande.setProduction_cost(newProductionCost);
        	break;
        case "TransportationCost":
        	newTransportationCost = cost.get_ht(commande, taxes);
        	newCommande.setTransportation_cost(newTransportationCost);
        	break;
        }
		
		return newCommande;
	}

	@Override
	public void setEventImpact(Commande commande, IDeliveryDate deliveryDate, ICost cost, Integer min, Integer max) {
		// TODO Auto-generated method stub
		Double newProductionCost;
		Double newTransportationCost;
		Float taxes;
		String simulation;
		
		Date newDeliveryDate = deliveryDate.get_delivery_date(commande, min, max);
		
		commande.setExpected_delivery_date(newDeliveryDate);
		
		long interval = Math.abs(commande.getExpected_delivery_date().getTime() - newDeliveryDate.getTime());
        float differenceInDays = (float) TimeUnit.DAYS.convert(interval, TimeUnit.MILLISECONDS);
        
        taxes = differenceInDays / (100.0f);
        
        switch (cost.getClass().getName()) {
        case "ProductionCost":
        	newProductionCost = cost.get_ht(commande, taxes);
        	commande.setProduction_cost(newProductionCost);
        	break;
        case "TransportationCost":
        	newTransportationCost = cost.get_ht(commande, taxes);
        	commande.setTransportation_cost(newTransportationCost);
        	break;
        }
	}

	@Override
	public List<Commande> executePlugin(List<Commande> listeCommande) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
