//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tree;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import codex.common.utils.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;
import codex.xbit.api.common.packets.*;

import codex.xbit.api.client.net.*;
import codex.xbit.api.client.cli.*;

import codex.xbit.api.client.gui.tab.*;
import codex.xbit.api.client.gui.tree.nodes.*;
import codex.xbit.api.client.gui.components.trees.*;


//-------------------------------------------------------------------------------------
public class Tree extends JTree {
	private Debug debug;
    private Tab tab;
    private boolean expandingIsDisabled;
    private static int counter;
//-------------------------------------------------------------------------------------
    public Tree(Debug _debug, Tab _tab, DefaultXbitTreeNode _rootNode) {
		super(_rootNode);
        debug = _debug;
        tab = _tab;
        expandingIsDisabled = false;
        setRootVisible(false);
//		TreeNode xbit = new TreeNode("xbit"); 
        setCellRenderer(new XTreeCellRenderer());
    	setOpaque(false);
        addTreeSelectionListener(new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
		    	expandingIsDisabled = true;
		        DefaultMutableTreeNode _node = (DefaultMutableTreeNode)
		                           getLastSelectedPathComponent();

		    /* if nothing is selected */ 
		        if (_node == null) return;

		    /* retrieve the node that was selected */ 
		        DefaultXbitTreeNode _xbitNode = (DefaultXbitTreeNode)_node;//.getUserObject();
		    /* React to the node selection. */
		    	debug.outln("Selected tab= " + _xbitNode.getId());
		    	tab.selectTab(_xbitNode.getId());
		    }
		});
		counter = 0;
    }

//-------------------------------------------------------------------------------------
	public void treeDidChange() { 
	    super.treeDidChange(); 
	    if(!expandingIsDisabled){
	    	expandAllNodes(this, 0, getRowCount());
	    }
	} 

//-------------------------------------------------------------------------------------
	private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
	    for(int i=startingIndex;i<rowCount;++i){
	        tree.expandRow(i);
	    }

	    if(tree.getRowCount()!=rowCount){
	        expandAllNodes(tree, rowCount, tree.getRowCount());
	    }
	}

//-------------------------------------------------------------------------------------
	@Override 
	public void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setColor(getBackground());
      g2.fillRect(0, 0, getWidth(), getHeight());
      for (int i = 0; i < getRowCount(); i++) {
        Object o = getPathForRow(i).getLastPathComponent();
        g2.setColor(getNodeColor(o));
        Rectangle r = getRowBounds(i);
        g2.fillRect(0, r.y, getWidth(), r.height);
      }
      g2.dispose();
      super.paintComponent(g);
    }

//-------------------------------------------------------------------------------------
  public static Color getNodeColor(Object value) {
//  	int _index = getRowOfNode(value);
  	int _index = counter++;
    switch (_index%2) {
    case 0:
      return Color.LIGHT_GRAY;
    case 1:
      return Color.WHITE;
    default:
      return Color.YELLOW;
    }
  }


//-------------------------------------------------------------------------------------
  public static int getRowOfNode(Object value) {
    if (value instanceof DefaultXbitTreeNode) {
      DefaultXbitTreeNode node = (DefaultXbitTreeNode) value;
//      return node.getPath().length;
      return node.getParent().getIndex(node);
    }
    return -1;
  }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
