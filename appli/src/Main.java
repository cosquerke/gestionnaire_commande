import component.CommandeFrame;
import loader.CSVLoader;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CommandeFrame cm = new CommandeFrame(new CSVLoader("data/commandes.csv"));
            cm.setVisible(true);
        });
    }
}
