package entity;

import java.util.Date;

public class Commande {
    private int id;
    private String type;
    private String description;
    private Date orderDate;
    private Date expeditionDate;
    private Date expectedDeliveryDate;
    private Double productionCost;
    private Double transportationCost;
    private Double vatFees;
    private Double price;
    private Boolean isDelivered;
    private String departureCountry;
    private String arrivalCountry;
    private String transportationMode;

    public Commande(int id, String type, String description, Date orderDate, Date expeditionDate, Date expectedDeliveryDate, Double productionCost, Double transportationCost, Double vatFees, Double price, Boolean isDelivered, String departureCountry, String arrivalCountry, String transportationMode) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.orderDate = orderDate;
        this.expeditionDate = expeditionDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.productionCost = productionCost;
        this.transportationCost = transportationCost;
        this.vatFees = vatFees;
        this.price = price;
        this.isDelivered = isDelivered;
        this.departureCountry = departureCountry;
        this.arrivalCountry = arrivalCountry;
        this.transportationMode = transportationMode;
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

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getExpeditionDate() {
        return expeditionDate;
    }

    public void setExpeditionDate(Date expeditionDate) {
        this.expeditionDate = expeditionDate;
    }

    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Double getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(Double productionCost) {
        this.productionCost = productionCost;
    }

    public Double getTransportationCost() {
        return transportationCost;
    }

    public void setTransportationCost(Double transportationCost) {
        this.transportationCost = transportationCost;
    }

    public Double getVatFees() {
        return vatFees;
    }

    public void setVatFees(Double vatFees) {
        this.vatFees = vatFees;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(Boolean delivered) {
        isDelivered = delivered;
    }

    public String getDepartureCountry() {
        return departureCountry;
    }

    public void setDepartureCountry(String departureCountry) {
        this.departureCountry = departureCountry;
    }

    public String getArrivalCountry() {
        return arrivalCountry;
    }

    public void setArrivalCountry(String arrivalCountry) {
        this.arrivalCountry = arrivalCountry;
    }

    public String getTransportationMode() {
        return transportationMode;
    }

    public void setTransportationMode(String transportationMode) {
        this.transportationMode = transportationMode;
    }
}
