//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.components.tables;
//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
//-------------------------------------------------------------------------------------

public class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable _table) {
            table = _table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
            int _height = table.getRowHeight();
            int row    = e.getY()/_height; //get the row of the button

                    /*Checking the row or column is valid or not*/
//            System.out.println("JTableButtonMouseListener.mouseClicked: _height="+_height);
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    /*perform a click event*/
                    ((JButton)value).doClick();
                }
            }
        }
    }
//-------------------------------------------------------------------------------------
