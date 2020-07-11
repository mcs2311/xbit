//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.components.tables;
//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
//-------------------------------------------------------------------------------------
public class JTableListRenderer extends JList<String> implements TableCellRenderer {        
        private boolean alreadyChangedTheHeight;
		public JTableListRenderer(int _alignement){
            super(new String[0]);
//			setHorizontalAlignment(JLabel.LEFT);
			DefaultListCellRenderer renderer =  (DefaultListCellRenderer)getCellRenderer();  
			renderer.setHorizontalAlignment(_alignement);  
            alreadyChangedTheHeight = false;
		}
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//		if (value instanceof String[]) {
            setListData((String[]) value);
/*        } else {
            setListData((Integer[]) value);            
        }*/
        //cell backgroud color when selected
        if (isSelected) {
            setBackground(UIManager.getColor("Table.selectionBackground"));
        } else {
            setBackground(UIManager.getColor("Table.background"));
        }

        if(alreadyChangedTheHeight){
            return this;
        }

		//updateData(value);
		try{
        	//this sets the component's width to the column width (therwise my JPanel would not properly fill the width - I am not sure if you want this)
        	int _width = table.getColumnModel().getColumn(column).getWidth();
        	int _height = (int) getPreferredSize().getHeight();
        	setSize(_width, _height);
        }catch(Exception _e){

        }

        //I used to have revalidate() call here, but it has proven redundant

        int height = getHeight();
        // the only thing that prevents infinite cell repaints is this
        // condition
        if (table.getRowHeight() < height){
        	if(height > 1){
//                System.out.println("JTableListRenderer.setRowHeight: _height="+height);
            	table.setRowHeight(height);
                alreadyChangedTheHeight = true;
        	}
        }
        return this;        }
    }
//-------------------------------------------------------------------------------------
