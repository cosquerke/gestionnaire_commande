package plugins;

import Appli.data.Commande;
import interfaces.CommandeImporterInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVCommandeImporter implements CommandeImporterInterface {
    @Override
    public List<Commande> importCommandes() {
        List<Commande> commandes = new ArrayList<>();

        String filepath = "src/Appli/data/commandes.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;

            // Ignorer la première ligne (en-tête)
            br.readLine();
            
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");

                // Assurez-vous que le tableau à la bonne longueur (nombre de fields attendu)
                if (fields.length == 14) {
                    int id = Integer.parseInt(fields[0]);
                    String type = fields[1];
                    String description = fields[2];

                    // Parsing des dates
                    Date orderDate = parseDate(fields[3]);
                    Date expeditionDate = parseDate(fields[4]);
                    Date expectedDeliveryDate = parseDate(fields[5]);

                    Double productionCost = Double.parseDouble(fields[6]);
                    Double transportationCost = Double.parseDouble(fields[7]);
                    Double vatFees = Double.parseDouble(fields[8]);
                    Double price = Double.parseDouble(fields[9]);
                    Boolean isDelivered = Boolean.parseBoolean(fields[10]);
                    String departureCountry = fields[11];
                    String arrivalCountry = fields[12];
                    String transportationMode = fields[13];

                    // Créer une instance de la classe Commande avec le constructeur approprié
                    Commande commande = new Commande(id, type, description, orderDate, expeditionDate, expectedDeliveryDate,
                            productionCost, transportationCost, vatFees, price, isDelivered,
                            departureCountry, arrivalCountry, transportationMode);

                    // Ajouter la commande à la liste
                    commandes.add(commande);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commandes;
    }

    private static Date parseDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
