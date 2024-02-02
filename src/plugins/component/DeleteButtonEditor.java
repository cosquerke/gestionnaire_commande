package plugins.component;

import Appli.data.Commande;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeleteButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private boolean isPushed;
    private final JTable table;
    private final List<Commande> commandes;

    public DeleteButtonEditor(JCheckBox checkBox, JTable table, List<Commande> commandes) {
        super(checkBox);
        this.table = table;
        this.commandes = commandes;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        button.setText((value == null) ? "" : value.toString());
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            int rowToRemove = table.convertRowIndexToModel(table.getEditingRow());
            ((DefaultTableModel) table.getModel()).removeRow(rowToRemove);
            commandes.remove(rowToRemove);
        }
        isPushed = false;
        return "Supprimer";
    }
}
