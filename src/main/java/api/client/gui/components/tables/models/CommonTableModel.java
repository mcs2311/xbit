//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.components.tables.models;
//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.*;
import javax.swing.*;
import javax.swing.table.*;

import codex.xbit.api.common.packets.*;

import codex.common.utils.*;
import codex.xbit.api.client.net.*;
import codex.xbit.api.common.packets.*;

//-------------------------------------------------------------------------------------
public class CommonTableModel extends AbstractTableModel {
    protected Debug debug;

    protected java.util.List<String> COLUMN_NAMES;
    protected java.util.List<Class<?>> COLUMN_TYPES;
    protected java.util.List<java.util.List<Object>> rows;

//-------------------------------------------------------------------------------------
    public CommonTableModel(Debug _debug){
        super();
        debug = _debug;

//        COLUMN_NAMES = new ArrayList<String>(Arrays.asList("Index", "Id", "Directions", "Values", "Functions", "Paths", "Color", "Remove"));
//        COLUMN_TYPES = new ArrayList<Class<?>>(Arrays.asList(Integer.class, Integer.class, String[].class, String[].class, String[].class, String[].class, Color.class, JButton.class));

        rows = new ArrayList<java.util.List<Object>>();
    }

//-------------------------------------------------------------------------------------
        @Override public int getColumnCount() {
//            debug.outln("WavesTableModel.getColumnCount..."+COLUMN_NAMES.size());
//            System.exit(0);
            return COLUMN_NAMES.size();
        }

        @Override public int getRowCount() {
//            debug.outln("WavesTableModel.getRowCount..."+rows.size());
            return rows.size();
        }

        @Override public String getColumnName(int columnIndex) {
            return COLUMN_NAMES.get(columnIndex);
        }

        @Override public Class<?> getColumnClass(int columnIndex) {
//            debug.outln("WavesTableModel.COLUMN_TYPES..."+COLUMN_TYPES.size()+" , columnIndex="+columnIndex);
            return COLUMN_TYPES.get(columnIndex);
        }

        @Override public Object getValueAt(final int rowIndex, final int columnIndex) {
//            debug.outln("WavesTableModel.getValueAt "+rowIndex+" , "+columnIndex+"...."+rows.size());
            if(rowIndex >= rows.size()){
                return null;
            }
            java.util.List<Object> _row = rows.get(rowIndex);
//            debug.outln("WavesTableModel.getValueAt _row"+_row.size());
            if(columnIndex >= _row.size()){
                return null;
            }
            return _row.get(columnIndex);
        }   

//-------------------------------------------------------------------------------------
    public void reload() {
        fireTableDataChanged();
    }

//-------------------------------------------------------------------------------------
public void clear() {
    rows.clear();
}   

//-------------------------------------------------------------------------------------
public void addRow(java.util.List<Object> _row) {
    rows.add(_row);
}   

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

}
//-------------------------------------------------------------------------------------
