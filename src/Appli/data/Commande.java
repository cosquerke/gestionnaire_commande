package Appli.data;

import java.util.Date;

public class Commande implements Cloneable{
	private int id;
	private String type;
	private String description;
	private Date order_date;
	private Date expedition_date;
	private Date expected_delivery_date;
	private Double production_cost;
	private Double transportation_cost;
	private Double vat_fees;
	private Double price;
	private Boolean is_delivered;
	private String departure_country;
	private String arrival_country;
	private String transportation_mode;
	
	
	public Commande(int id, String type, String description, Date order_date, Date expedition_date,
			Date expected_delivery_date, Double production_cost, Double transportation_cost, Double vat_fees, Double price,
			Boolean is_delivered, String departure_country, String arrival_country, String transportation_mode) {
		
		this.id = id;
		this.type = type;
		this.description = description;
		this.order_date = order_date;
		this.expedition_date = expedition_date;
		this.expected_delivery_date = expected_delivery_date;
		this.production_cost = production_cost;
		this.transportation_cost = transportation_cost;
		this.vat_fees = vat_fees;
		this.price = price;
		this.is_delivered = is_delivered;
		this.departure_country = departure_country;
		this.arrival_country = arrival_country;
		this.transportation_mode = transportation_mode;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getOrder_date() {
		return order_date;
	}


	public void setOrder_date(Date order_date) {
		this.order_date = order_date;
	}


	public Date getExpedition_date() {
		return expedition_date;
	}


	public void setExpedition_date(Date expedition_date) {
		this.expedition_date = expedition_date;
	}


	public Date getExpected_delivery_date() {
		return expected_delivery_date;
	}


	public void setExpected_delivery_date(Date expected_delivery_date) {
		this.expected_delivery_date = expected_delivery_date;
	}


	public Double getProduction_cost() {
		return production_cost;
	}


	public void setProduction_cost(Double production_cost) {
		this.production_cost = production_cost;
	}


	public Double getTransportation_cost() {
		return transportation_cost;
	}


	public void setTransportation_cost(Double transportation_cost) {
		this.transportation_cost = transportation_cost;
	}


	public Double getVat_fees() {
		return vat_fees;
	}


	public void setVat_fees(Double vat_fees) {
		this.vat_fees = vat_fees;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
	}


	public Boolean getIs_delivered() {
		return is_delivered;
	}


	public void setIs_delivered(Boolean is_delivered) {
		this.is_delivered = is_delivered;
	}


	public String getDeparture_country() {
		return departure_country;
	}


	public void setDeparture_country(String departure_country) {
		this.departure_country = departure_country;
	}


	public String getArrival_country() {
		return arrival_country;
	}


	public void setArrival_country(String arrival_country) {
		this.arrival_country = arrival_country;
	}


	public String getTransportation_mode() {
		return transportation_mode;
	}


	public void setTransportation_mode(String transportation_mode) {
		this.transportation_mode = transportation_mode;
	}
	
	public Object clone() {
        Object clone = null;
        try {
        	clone = super.clone();
        } catch(CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }
        return clone;
    }


	@Override
	public String toString() {
		return "Commande [id=" + id + ", type=" + type + ", description=" + description + ", order_date=" + order_date
				+ ", expedition_date=" + expedition_date + ", expected_delivery_date=" + expected_delivery_date
				+ ", production_cost=" + production_cost + ", transportation_cost=" + transportation_cost
				+ ", vat_fees=" + vat_fees + ", price=" + price + ", is_delivered=" + is_delivered
				+ ", departure_country=" + departure_country + ", arrival_country=" + arrival_country
				+ ", transportation_mode=" + transportation_mode + "]";
	}
	
	
	



}
