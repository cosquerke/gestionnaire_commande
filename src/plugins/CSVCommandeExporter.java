package plugins;

import Appli.data.Commande;
import interfaces.CommandeExporterInterface;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

public class CSVCommandeExporter implements CommandeExporterInterface {
    @Override
    public void exportCommandes(List<Commande> commandes) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try (PrintWriter writer = new PrintWriter("src/Appli/data/commandes.csv")) {
            // En-tête du CSV
            writer.println("id;type;description;order_date;expedition_date;expected_delivery_date;production_cost;transportation_cost;vat_fees;price;is_delivered;departure_country;arrival_country;transportation_mode");

            // Parcourir chaque commande et écrire ses données dans le fichier CSV
            for (Commande commande : commandes) {
                String sb = commande.getId() + ";" +
                        commande.getType() + ";" +
                        commande.getDescription() + ";" +
                        dateFormat.format(commande.getOrder_date()) + ";" +
                        dateFormat.format(commande.getExpedition_date()) + ";" +
                        dateFormat.format(commande.getExpected_delivery_date()) + ";" +
                        commande.getProduction_cost() + ";" +
                        commande.getTransportation_cost() + ";" +
                        commande.getVat_fees() + ";" +
                        commande.getPrice() + ";" +
                        commande.getIs_delivered() + ";" +
                        commande.getDeparture_country() + ";" +
                        commande.getArrival_country() + ";" +
                        commande.getTransportation_mode();

                writer.println(sb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
