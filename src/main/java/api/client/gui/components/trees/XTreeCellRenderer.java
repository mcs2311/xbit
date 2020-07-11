//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.components.trees;
//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.client.net.*;
import codex.xbit.api.client.gui.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class XTreeCellRenderer extends DefaultTreeCellRenderer {
    private final Color color = new Color(0x0, true);
//    private final Color color = Color.BLACK;
    @Override public Color getBackgroundSelectionColor() {
      return color;
    }
    @Override public Color getBackgroundNonSelectionColor() {
      return color;
    }
    @Override public Component getTreeCellRendererComponent(
        JTree tree, Object value, boolean selected, boolean expanded,
        boolean leaf, int row, boolean hasFocus) {
      JLabel l = (JLabel) super.getTreeCellRendererComponent(
          tree, value, selected, expanded, leaf, row, hasFocus);
      if (getRowOfNode(value) == 2) {
//        l.setForeground(Color.WHITE);
      }
      return l;
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
}
//-------------------------------------------------------------------------------------
