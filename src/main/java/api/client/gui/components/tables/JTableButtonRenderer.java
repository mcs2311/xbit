//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.components.tables;
//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
//-------------------------------------------------------------------------------------
public class JTableButtonRenderer implements TableCellRenderer {        
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            return button;  
        }
    }
//-------------------------------------------------------------------------------------
