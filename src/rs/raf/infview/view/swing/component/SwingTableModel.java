package rs.raf.infview.view.swing.component;

import rs.raf.infview.view.component.TableModel;
import javax.swing.table.AbstractTableModel;

class SwingTableModel extends AbstractTableModel {

    private TableModel model;

    SwingTableModel(TableModel model) {
        this.model = model;
    }

    @Override
    public int getColumnCount() {
        return model.getColumns().length;
    }

    @Override
    public int getRowCount() {
        return model.getRows().length;
    }

    @Override
    public String getColumnName(int column) {
        return model.getColumns()[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return model.getRows()[rowIndex][columnIndex];
    }
}