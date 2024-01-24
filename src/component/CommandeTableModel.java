package component;

import Appli.data.Commande;
import util.DateFormat;

import javax.swing.table.DefaultTableModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CommandeTableModel extends DefaultTableModel {
    private final List<Commande> commandes;
    private final SimpleDateFormat dateFormat = DateFormat.getInstance();

    private final String[] columnNames = {
            "ID", "Type", "Description", "Order Date", "Expedition Date", "Expected Delivery",
            "Production Cost", "Transportation Cost", "VAT", "Price", "Is Delivered",
            "Departure Country", "Arrival Country", "Transport Mode", "Supprimer"
    };

    public CommandeTableModel(List<Commande> commandes) {
        super(commandes.size(), 15);
        this.commandes = commandes;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Commande commande = commandes.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> commande.getId();
            case 1 -> commande.getType();
            case 2 -> commande.getDescription();
            case 3 -> dateFormat.format(commande.getOrder_date());
            case 4 -> dateFormat.format(commande.getExpedition_date());
            case 5 -> dateFormat.format(commande.getExpected_delivery_date());
            case 6 -> commande.getProduction_cost();
            case 7 -> commande.getTransportation_cost();
            case 8 -> commande.getVat_fees();
            case 9 -> commande.getPrice();
            case 10 -> commande.getIs_delivered();
            case 11 -> commande.getDeparture_country();
            case 12 -> commande.getArrival_country();
            case 13 -> commande.getTransportation_mode();
            case 14 -> "Supprimer";
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (commandes.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // All cells except the ID column are editable
        return columnIndex != 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Commande commande = commandes.get(rowIndex);
        System.out.println(rowIndex);
        try {
            switch (columnIndex) {
                case 0, 14 -> {
                }
                case 1 -> commande.setType((String) aValue);
                case 2 -> commande.setDescription((String) aValue);
                case 3 -> commande.setOrder_date(dateFormat.parse((String) aValue));
                case 4 -> commande.setExpedition_date(dateFormat.parse((String) aValue));
                case 5 -> commande.setExpected_delivery_date(dateFormat.parse((String) aValue));
                case 6 -> commande.setProduction_cost((Double) aValue);
                case 7 -> commande.setTransportation_cost((Double) aValue);
                case 8 -> commande.setVat_fees((Double) aValue);
                case 9 -> commande.setPrice((Double) aValue);
                case 10 -> commande.setIs_delivered((Boolean) aValue);
                case 11 -> commande.setDeparture_country((String) aValue);
                case 12 -> commande.setArrival_country((String) aValue);
                case 13 -> commande.setTransportation_mode((String) aValue);
                default -> System.out.println("Invalid column index");
            }
        } catch (NumberFormatException | ParseException e) {
            System.out.println(e.getLocalizedMessage());
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}

