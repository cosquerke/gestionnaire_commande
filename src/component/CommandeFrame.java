package component;

import Appli.data.Commande;
import interfaces.CommandeLoaderInterface;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;


public class CommandeFrame extends JFrame {
    public static final int MIN_COLUMN_WIDTH = 30;
    public static final int MAX_COLUMN_WIDTH = 300;

    private final List<Commande> commandes;
    private final CommandeTableModel model;

    public CommandeFrame(CommandeLoaderInterface commandeLoader) {
        setTitle("Gestion commandes");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Récupération des commandes dans le CSV
        this.commandes = commandeLoader.load();

        // Création du modèle de tableau et du tableau
        this.model = new CommandeTableModel(commandes);
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
            AddCommandeDialog addDialog = new AddCommandeDialog(this);
            addDialog.setVisible(true);
        });

        JButton exportButton = new JButton("Exporter");
        buttonPanel.add(exportButton);

        // Create a panel for the bottom totals
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.add(new JLabel("Total coûts production"));
        totalPanel.add(new JLabel("Total coûts livraison"));
        totalPanel.add(new JLabel("Montant total"));
        totalPanel.add(new JLabel("Marge totale"));

        // Add the components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);
        add(totalPanel, BorderLayout.SOUTH);
    }

    public static void adjustColumnWidths(JTable table) {
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

    public void addCommande(Commande commande) {
        commandes.add(commande);
        model.fireTableDataChanged(); // Pour actualiser le tableau
    }
}