package Appli;

import Appli.data.Commande;
import interfaces.CommandeExporterInterface;
import interfaces.CommandeImporterInterface;
import interfaces.IPluginInterface;
import interfaces.IPluginMonitorFrame;
import loader.Loader;
import plugins.component.AddCommandeDialog;
import plugins.component.CommandeTableModel;
import plugins.component.DeleteButtonEditor;
import plugins.component.DeleteButtonRenderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

// Moniteur : app qui permet de monitorer les plugins installés, les mises à jour etc...
// 1. Appli principale qui charge des plugins
public class Main {
    private static CommandeImporterInterface importer;
    private static CommandeExporterInterface exporter;
    private static IPluginInterface feature;
    
    private static List<Commande> commandes;

    private static final int MIN_COLUMN_WIDTH = 30;
    private static final int MAX_COLUMN_WIDTH = 300;

    public static void main(String[] args) {
        Loader loader = Loader.getInstance();

        List<PluginDescriptor> monitoringFrameDescriptors = loader.getPluginDescriptorsForInterface(IPluginMonitorFrame.class);
        IPluginMonitorFrame monitorFrame = (IPluginMonitorFrame) loader.getPlugin(monitoringFrameDescriptors.get(0));
        
        monitorFrame.setPlugins();
        monitorFrame.display();
        
        SwingUtilities.invokeLater(() -> {
            JDialog importerDialog = new JDialog();
            importerDialog.setModal(true);
            importerDialog.setTitle("Choisir une méthode d'import");
            importerDialog.setSize(400, 100);
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
            importerDialog.setVisible(true);

            if (null == importer) {
                System.out.println("Il est nécessaire de choisir un import pour faire fonctionner l'application.");
                System.exit(0);
            }

            commandes = importer.importCommandes();

            JFrame frame = new JFrame("Gestion commandes");
            frame.setSize(1200, 800);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CommandeTableModel model = new CommandeTableModel(commandes);

            JTable table = new JTable(model);

            // Listener qui ajuste la largeur des colonnes en fonction du contenu
            table.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    adjustColumnWidths((JTable) e.getComponent());
                }
            });

            // Création du bouton supprimer et mapping des actions pour le render et supprimer les cellules
            DeleteButtonRenderer buttonRenderer = new DeleteButtonRenderer();
            table.getColumn("Supprimer").setCellRenderer(buttonRenderer);
            table.getColumn("Supprimer").setCellEditor(new DeleteButtonEditor(new JCheckBox(), table, commandes));

            JScrollPane scrollPane = new JScrollPane(table);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            JButton addButton = new JButton("Ajouter");
            buttonPanel.add(addButton);
            addButton.addActionListener(e -> {
                AddCommandeDialog addDialog = new AddCommandeDialog(frame);
                addDialog.setVisible(true);
            });

            JDialog exporterDialog = new JDialog();
            exporterDialog.setModal(true);
            exporterDialog.setTitle("Choisir une méthode d'export");
            exporterDialog.setSize(400, 100);
            exporterDialog.setLayout(new FlowLayout());
            exporterDialog.setLocationRelativeTo(null);
            exporterDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            List<PluginDescriptor> exporterDescriptors = loader.getPluginDescriptorsForInterface(CommandeExporterInterface.class);
            for (PluginDescriptor descriptor : exporterDescriptors) {
                JButton button = new JButton(descriptor.description());
                button.addActionListener(e -> {
                    exporter = (CommandeExporterInterface) loader.getPlugin(descriptor);

                    exporter.exportCommandes(commandes);
                    exporterDialog.dispose();
                });
                exporterDialog.add(button);
            }

            JButton exportButton = new JButton("Exporter");
            buttonPanel.add(exportButton);
            exportButton.addActionListener(e -> exporterDialog.setVisible(true));

            // Create a panel for the bottom totals
            JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));      
            
            JLabel label_min = new JLabel("Minimum delay :");
            totalPanel.add(label_min);
            JTextField min_delay = new JTextField();
            min_delay.setText("0");
            totalPanel.add(min_delay);
            
            JLabel label_max = new JLabel("Maximum delay :");
            totalPanel.add(label_max);
            JTextField max_delay = new JTextField();
            max_delay.setText("0");
            totalPanel.add(max_delay);
            
            List<PluginDescriptor> featuresDescriptors = loader.getPluginDescriptorsForInterface(IPluginInterface.class);
            for (PluginDescriptor descriptor : featuresDescriptors) {
                JButton button = new JButton(descriptor.name());
                button.addActionListener(e -> {
                	// appel des methodes execute 
                	feature = (IPluginInterface) loader.getPlugin(descriptor);
                	commandes = feature.executePlugin(commandes, Integer.parseInt(min_delay.getText()), Integer.parseInt(max_delay.getText()));
                	buidlPanel(table,commandes);
             
                });
                totalPanel.add(button);
            }
            

            // Add the components to the frame
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.NORTH);
            frame.add(totalPanel, BorderLayout.SOUTH);

            frame.setLocationRelativeTo(null); // Centrer la fenêtre
            frame.setVisible(true);
            
            Thread updateThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    monitorFrame.setPlugins();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            updateThread.start();
        });
        
    }

    private static void adjustColumnWidths(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = MIN_COLUMN_WIDTH;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > MAX_COLUMN_WIDTH)
                width = MAX_COLUMN_WIDTH;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    
    public static void buidlPanel(JTable table, List<Commande> commandes) {
    	CommandeTableModel new_model = new CommandeTableModel(commandes);
    	table.setModel(new_model);
    	DeleteButtonRenderer buttonRenderer = new DeleteButtonRenderer();
        table.getColumn("Supprimer").setCellRenderer(buttonRenderer);
        table.getColumn("Supprimer").setCellEditor(new DeleteButtonEditor(new JCheckBox(), table, commandes));
    }
}
