package Appli;

import Appli.data.Commande;
import exception.MissingParameterException;
import interfaces.CommandeExporterInterface;
import interfaces.CommandeImporterInterface;
import interfaces.CommandePluginInterface;
import interfaces.PluginMonitorInterface;
import loader.Loader;
import plugins.component.CommandeTableModel;
import plugins.component.DeleteButtonEditor;
import plugins.component.DeleteButtonRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Moniteur : app qui permet de monitorer les plugins installés, les mises à jour, etc.
// 1. Appli principale qui charge des plugins
public class Main {
    private static CommandeImporterInterface importer;
    private static CommandeExporterInterface exporter;
    private static PluginMonitorInterface monitor;

    private static List<Commande> commandes;

    public static void main(String[] args) {
        Loader loader = Loader.getInstance();

        List<PluginDescriptor> monitorDescriptors = loader.getPluginDescriptorsForInterface(PluginMonitorInterface.class);
        if (!monitorDescriptors.isEmpty()) {
            JDialog monitorChoiceDialog = new JDialog();
            monitorChoiceDialog.setModal(true);
            monitorChoiceDialog.setTitle("Choisir un moniteur");
            monitorChoiceDialog.setLayout(new FlowLayout());
            monitorChoiceDialog.setLocationRelativeTo(null);
            monitorChoiceDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            for (PluginDescriptor descriptor : monitorDescriptors) {
                JButton button = new JButton(descriptor.name());
                button.addActionListener(e -> {
                    monitor = (PluginMonitorInterface) loader.getPlugin(descriptor);

                    monitorChoiceDialog.dispose();
                });
                monitorChoiceDialog.add(button);
            }
            monitorChoiceDialog.pack();
            monitorChoiceDialog.setVisible(true);

            monitor.setPlugins();
            monitor.display();
        }

        SwingUtilities.invokeLater(() -> {
            JDialog importerDialog = new JDialog();
            importerDialog.setModal(true);
            importerDialog.setTitle("Choisir une méthode d'import");
            importerDialog.setLayout(new FlowLayout());
            importerDialog.setLocationRelativeTo(null);
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
            importerDialog.pack();
            importerDialog.setVisible(true);

            if (null == importer) {
                System.out.println("Il est nécessaire de choisir un import pour faire fonctionner l'application.");
                System.exit(0);
            }

            try {
                commandes = importer.importCommandes();
            } catch (MissingParameterException e) {
                throw new RuntimeException(e);
            }

            JFrame frame = new JFrame("Gestion commandes");
            frame.setSize(1200, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CommandeTableModel model = new CommandeTableModel(commandes);

            JTable table = new JTable(model);

            // Création du bouton supprimer et mapping des actions pour le render et supprimer les cellules
            DeleteButtonRenderer buttonRenderer = new DeleteButtonRenderer();
            table.getColumn("Supprimer").setCellRenderer(buttonRenderer);
            table.getColumn("Supprimer").setCellEditor(new DeleteButtonEditor(new JCheckBox(), table, commandes));

            JScrollPane scrollPane = new JScrollPane(table);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JDialog exporterDialog = new JDialog();
            exporterDialog.setModal(true);
            exporterDialog.setTitle("Choisir une méthode d'export");
            exporterDialog.setLayout(new FlowLayout());
            exporterDialog.setLocationRelativeTo(null);
            exporterDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            List<PluginDescriptor> exporterDescriptors = loader.getPluginDescriptorsForInterface(CommandeExporterInterface.class);
            for (PluginDescriptor descriptor : exporterDescriptors) {
                JButton button = new JButton(descriptor.description());
                button.addActionListener(event -> {
                    exporter = (CommandeExporterInterface) loader.getPlugin(descriptor);

                    try {
                        exporter.exportCommandes(commandes);
                    } catch (MissingParameterException e) {
                        System.out.println(e.getMessage());
                    }
                    exporterDialog.dispose();
                });
                exporterDialog.add(button);
            }
            exporterDialog.pack();

            JButton exportButton = new JButton("Exporter");
            buttonPanel.add(exportButton);
            exportButton.addActionListener(e -> exporterDialog.setVisible(true));

            // Create a panel for the bottom totals
            JPanel pluginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            List<PluginDescriptor> featuresDescriptors = loader.getPluginDescriptorsForInterface(CommandePluginInterface.class);
            for (PluginDescriptor descriptor : featuresDescriptors) {
                JButton button = getPluginButton(descriptor, loader, table);
                pluginPanel.add(button);
            }

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.NORTH);
            frame.add(pluginPanel, BorderLayout.SOUTH);
            frame.setLocationRelativeTo(null); // Centrer la fenêtre
            frame.setVisible(true);

            if (monitor != null) {
                Thread updateThread = new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        monitor.setPlugins();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                updateThread.start();
            }
        });

    }

    private static JButton getPluginButton(PluginDescriptor descriptor, Loader loader, JTable table) {
        JButton button = new JButton(descriptor.name());
        button.addActionListener(event -> {
            // appel des méthodes execute
            CommandePluginInterface plugin = (CommandePluginInterface) loader.getPlugin(descriptor);

            if (null != plugin) {
                try {
                    commandes = plugin.executePlugin(commandes);
                } catch (MissingParameterException e) {
                    System.out.println(e.getMessage());
                }
                buildPanel(table,commandes);
            }
        });
        return button;
    }

    public static void buildPanel(JTable table, List<Commande> commandes) {
    	CommandeTableModel new_model = new CommandeTableModel(commandes);
    	table.setModel(new_model);
    	DeleteButtonRenderer buttonRenderer = new DeleteButtonRenderer();
        table.getColumn("Supprimer").setCellRenderer(buttonRenderer);
        table.getColumn("Supprimer").setCellEditor(new DeleteButtonEditor(new JCheckBox(), table, commandes));
    }
}
