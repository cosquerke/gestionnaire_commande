package plugins;

import Appli.data.Commande;
import interfaces.IPluginInterface;
import util.DateFormat;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

public class DialogCommandeAdder implements IPluginInterface {
    @Override
    public List<Commande> executePlugin(List<Commande> listeCommande, Integer eventDuration) {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Choisir une méthode d'export");
        dialog.setLayout(new GridLayout(0, 2));
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        JTextField typeField = new JTextField(10);
        JTextField descriptionField = new JTextField(10);
        JTextField orderDateField = new JTextField(10);
        JTextField expeditionDateField = new JTextField(10);
        JTextField deliveryDateField = new JTextField(10);
        JTextField productionCostField = new JTextField(10);
        JTextField transportationCostField = new JTextField(10);
        JTextField vatFeesField = new JTextField(10);
        JTextField priceField = new JTextField(10);
        JCheckBox isDeliveredCheckBox = new JCheckBox();
        JTextField departureCountryField = new JTextField(10);
        JTextField arrivalCountryField = new JTextField(10);
        JTextField transportationModeField = new JTextField(10);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            try {
                Commande commande = new Commande(
                        listeCommande.size() + 1,
                        typeField.getText(),
                        descriptionField.getText(),
                        DateFormat.getInstance().parse(orderDateField.getText()),
                        DateFormat.getInstance().parse(expeditionDateField.getText()),
                        DateFormat.getInstance().parse(deliveryDateField.getText()),
                        Double.parseDouble(productionCostField.getText()),
                        Double.parseDouble(transportationCostField.getText()),
                        Double.parseDouble(vatFeesField.getText()),
                        Double.parseDouble(priceField.getText()),
                        isDeliveredCheckBox.isSelected(),
                        departureCountryField.getText(),
                        arrivalCountryField.getText(),
                        transportationCostField.getText()
                );

                listeCommande.add(commande);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            dialog.dispose();
        });

        dialog.setLayout(new GridLayout(0, 2));
        dialog.add(new JLabel("Type :"));
        dialog.add(typeField);
        dialog.add(new JLabel("Description :"));
        dialog.add(descriptionField);
        dialog.add(new JLabel("Date de commande :"));
        dialog.add(orderDateField);
        dialog.add(new JLabel("Date d'expédition :"));
        dialog.add(expeditionDateField);
        dialog.add(new JLabel("Date de livraison :"));
        dialog.add(deliveryDateField);
        dialog.add(new JLabel("Coût de production :"));
        dialog.add(productionCostField);
        dialog.add(new JLabel("Coût de transport :"));
        dialog.add(transportationCostField);
        dialog.add(new JLabel("Frais de TVA :"));
        dialog.add(vatFeesField);
        dialog.add(new JLabel("Prix :"));
        dialog.add(priceField);
        dialog.add(new JLabel("Est livré :"));
        dialog.add(isDeliveredCheckBox);
        dialog.add(new JLabel("Pays de départ :"));
        dialog.add(departureCountryField);
        dialog.add(new JLabel("Pays d'arrivée :"));
        dialog.add(arrivalCountryField);
        dialog.add(new JLabel("Mode de transport :"));
        dialog.add(transportationModeField);
        dialog.add(saveButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return listeCommande;
    }
}