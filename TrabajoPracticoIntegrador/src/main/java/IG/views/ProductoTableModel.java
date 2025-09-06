package IG.views;

import IG.application.Dtos.TipoProductoDto;

import javax.swing.table.AbstractTableModel;

public class ProductoTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Id", "Descripción", "Cantidad por Unidad", "Unidad de Medida", "Stock", "Tipo Producto"};
    private Object[][] data = new Object[0][columnNames.length];

    public void addRow(Object row) {
        var temp = new Object[data.length + 1][columnNames.length];
        for (int i = 0; i < data.length; i++) {
            temp[i] = data[i];
        }
        temp[data.length] = (Object[]) row;
        data = temp;
        fireTableDataChanged();
    }

    public void removeRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= data.length) {
            throw new IndexOutOfBoundsException("Índice de fila fuera de rango.");
        }
        var temp = new Object[data.length - 1][columnNames.length];
        for (int i = 0, j = 0; i < data.length; i++) {
            if (i != rowIndex) {
                temp[j++] = data[i];
            }
        }
        data = temp;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var object = data[rowIndex][columnIndex];
        if (object instanceof TipoProductoDto tipoProducto) {
            return tipoProducto.descripcion();
        } else {
            return object;
        }
    }
}
