package plugins;

import Appli.Plugin;
import Appli.data.Commande;
import interfaces.CommandeImporterInterface;
import util.DateFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class StaticCommandeImporter extends Plugin implements CommandeImporterInterface {
    @Override
    public List<Commande> importCommandes() {
        return List.of(
                new Commande(1, "Électronique", "Smartphone", parseDate("10/01/2023"), parseDate("15/01/2023"), parseDate("30/01/2023"), 250.0, 20.0, 50.0, 400.0, true, "Chine", "États-Unis", "Aérien"),
                new Commande(2, "Vêtements", "Veste en cuir", parseDate("05/02/2023"), parseDate("10/02/2023"), parseDate("25/02/2023"), 80.0, 15.0, 20.0, 150.0, false, "Italie", "France", "Maritime"),
                new Commande(3, "Électroménager", "Machine à laver", parseDate("20/03/2023"), parseDate("25/03/2023"), parseDate("10/04/2023"), 350.0, 30.0, 70.0, 500.0, false, "Allemagne", "Royaume-Uni", "Terrestre"),
                new Commande(4, "Livres", "Collection de poésie", parseDate("12/04/2023"), parseDate("15/04/2023"), parseDate("30/04/2023"), 40.0, 10.0, 5.0, 70.0, true, "Espagne", "Mexique", "Aérien"),
                new Commande(5, "Sport", "Équipement de camping", parseDate("08/05/2023"), parseDate("12/05/2023"), parseDate("28/05/2023"), 120.0, 25.0, 15.0, 180.0, false, "Canada", "Australie", "Maritime"),
                new Commande(6, "Électronique", "Casque sans fil", parseDate("03/06/2023"), parseDate("10/06/2023"), parseDate("25/06/2023"), 120.0, 10.0, 30.0, 200.0, false, "Corée du Sud", "Brésil", "Aérien"),
                new Commande(7, "Vêtements", "Robe de soirée", parseDate("15/07/2023"), parseDate("20/07/2023"), parseDate("05/08/2023"), 150.0, 20.0, 25.0, 220.0, false, "France", "Canada", "Terrestre"),
                new Commande(8, "Électroménager", "Réfrigérateur", parseDate("02/08/2023"), parseDate("08/08/2023"), parseDate("25/08/2023"), 400.0, 40.0, 80.0, 550.0, false, "Japon", "Allemagne", "Maritime"),
                new Commande(9, "Livres", "Roman policier", parseDate("18/09/2023"), parseDate("25/09/2023"), parseDate("10/10/2023"), 30.0, 5.0, 3.0, 50.0, true, "Italie", "États-Unis", "Aérien"),
                new Commande(10, "Sport", "Raquettes de tennis", parseDate("12/10/2023"), parseDate("18/10/2023"), parseDate("02/11/2023"), 90.0, 15.0, 10.0, 120.0, false, "Australie", "France", "Terrestre"),
                new Commande(11, "Électronique", "Caméra DSLR", parseDate("25/11/2023"), parseDate("02/12/2023"), parseDate("18/12/2023"), 300.0, 25.0, 60.0, 450.0, false, "États-Unis", "Chine", "Aérien"),
                new Commande(12, "Vêtements", "Chaussures de sport", parseDate("10/12/2023"), parseDate("15/12/2023"), parseDate("30/12/2023"), 60.0, 12.0, 15.0, 100.0, true, "Royaume-Uni", "Australie", "Maritime"),
                new Commande(13, "Électroménager", "Cafetière", parseDate("05/01/2024"), parseDate("10/01/2024"), parseDate("25/01/2024"), 70.0, 8.0, 20.0, 100.0, false, "Brésil", "Espagne", "Terrestre"),
                new Commande(14, "Livres", "Guide de voyage", parseDate("20/02/2024"), parseDate("25/02/2024"), parseDate("10/03/2024"), 20.0, 5.0, 2.0, 30.0, true, "Canada", "Japon", "Aérien"),
                new Commande(15, "Sport", "Planche de surf", parseDate("15/03/2024"), parseDate("20/03/2024"), parseDate("05/04/2024"), 150.0, 30.0, 25.0, 220.0, false, "Australie", "États-Unis", "Maritime"),
                new Commande(16, "Électronique", "Enceinte Bluetooth", parseDate("08/04/2024"), parseDate("15/04/2024"), parseDate("30/04/2024"), 80.0, 10.0, 15.0, 120.0, false, "Chine", "France", "Aérien"),
                new Commande(17, "Vêtements", "Costume formel", parseDate("20/05/2024"), parseDate("25/05/2024"), parseDate("10/06/2024"), 100.0, 18.0, 30.0, 160.0, true, "Italie", "Royaume-Uni", "Terrestre"),
                new Commande(18, "Électroménager", "Aspirateur robot", parseDate("03/06/2024"), parseDate("10/06/2024"), parseDate("25/06/2024"), 200.0, 20.0, 40.0, 300.0, false, "Allemagne", "Canada", "Maritime"),
                new Commande(19, "Livres", "Biographie", parseDate("15/07/2024"), parseDate("20/07/2024"), parseDate("05/08/2024"), 25.0, 5.0, 3.0, 40.0, true, "Espagne", "Brésil", "Aérien"),
                new Commande(20, "Sport", "Tapis de yoga", parseDate("02/08/2024"), parseDate("08/08/2024"), parseDate("25/08/2024"), 40.0, 8.0, 5.0, 60.0, false, "Japon", "Corée du Sud", "Terrestre")
        );
    }

    private Date parseDate(String date) {
        try {
            return DateFormat.getInstance().parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
