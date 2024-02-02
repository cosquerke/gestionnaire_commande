package Appli;

import interfaces.CommandeFrameInterface;
import interfaces.CommandeImporterInterface;
import loader.Loader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Moniteur : app qui permet de monitorer les plugins installés, les mises à jour etc...
// 1. Appli principale qui charge des plugins
public class Main {
    private static CommandeImporterInterface importer;

    public static void main(String[] args) {
        Loader loader = Loader.getInstance();

        SwingUtilities.invokeLater(() -> {
            JDialog importerDialog = new JDialog();
            importerDialog.setModal(true);
            importerDialog.setTitle("Choisir une méthode d'import");
            importerDialog.setSize(400, 100);
            importerDialog.setLayout(new FlowLayout());
            importerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            List<PluginDescriptor> importerDescriptors = loader.getPluginDescriptorsForInterface(CommandeImporterInterface.class);

            for (PluginDescriptor descriptor : importerDescriptors) {
                JButton button = new JButton(descriptor.description());
                button.addActionListener(e -> {
                    importer = (CommandeImporterInterface) loader.getPlugin(descriptor);

                    importerDialog.dispose();
                });
                importerDialog.add(button);
            }
            importerDialog.setLocationRelativeTo(null);
            importerDialog.setVisible(true);

            if (null == importer) {
                System.out.println("Il est nécessaire de choisir un import pour faire fonctionner l'application.");
                System.exit(0);
            }

            List<PluginDescriptor> frameDescriptors = loader.getPluginDescriptorsForInterface(CommandeFrameInterface.class);
            CommandeFrameInterface frame = (CommandeFrameInterface) loader.getPlugin(frameDescriptors.get(0));

            frame.setCommandes(importer.importCommandes());
            frame.display();
        });
    }

    private static JButton getButton(PluginDescriptor descriptor, Loader loader) {
        JButton button = new JButton(descriptor.description());
        button.addActionListener(e -> {
            CommandeImporterInterface importer = (CommandeImporterInterface) loader.getPlugin(descriptor);

            List<PluginDescriptor> frameDescriptors = loader.getPluginDescriptorsForInterface(CommandeFrameInterface.class);
            CommandeFrameInterface frame = (CommandeFrameInterface) loader.getPlugin(frameDescriptors.get(0));

            frame.setCommandes(importer.importCommandes());
            frame.display();
        });
        return button;
    }
}
