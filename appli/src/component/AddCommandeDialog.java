package component;

import entity.Commande;
import util.DateFormat;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

public class AddCommandeDialog extends JDialog {

    public AddCommandeDialog(Frame owner) {
        super(owner, "Ajouter une Commande", true);
        initComponents(owner);
    }

    private void initComponents(Frame owner) {
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
                Commande newCommande = new Commande(
                        1000,
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

                ((CommandeFrame) owner).addCommande(newCommande);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            dispose();
        });

        setLayout(new GridLayout(0, 2));
        add(new JLabel("Type :"));
        add(typeField);
        add(new JLabel("Description :"));
        add(descriptionField);
        add(new JLabel("Date de commande :"));
        add(orderDateField);
        add(new JLabel("Date d'expédition :"));
        add(expeditionDateField);
        add(new JLabel("Date de livraison :"));
        add(deliveryDateField);
        add(new JLabel("Coût de production :"));
        add(productionCostField);
        add(new JLabel("Coût de transport :"));
        add(transportationCostField);
        add(new JLabel("Frais de TVA :"));
        add(vatFeesField);
        add(new JLabel("Prix :"));
        add(priceField);
        add(new JLabel("Est livré :"));
        add(isDeliveredCheckBox);
        add(new JLabel("Pays de départ :"));
        add(departureCountryField);
        add(new JLabel("Pays d'arrivée :"));
        add(arrivalCountryField);
        add(new JLabel("Mode de transport :"));
        add(transportationModeField);
        add(saveButton);

        pack();
        setLocationRelativeTo(owner);
    }
}