//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tree.nodes;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import codex.common.utils.*;


//-------------------------------------------------------------------------------------
public class DefaultXbitTreeNode extends DefaultMutableTreeNode {
	protected Debug debug;
	protected String name;
	protected String id;
	protected int index;

//-------------------------------------------------------------------------------------
    public DefaultXbitTreeNode(Debug _debug, String _name) {
    	super(_name);
    	debug = _debug;
    	name = _name;
    	id = name;
//    	index = _index;
    }

//-------------------------------------------------------------------------------------
    public String getId() {
//    	debug.outln("getId---->_id= " + id);
    	return id;
    }

//-------------------------------------------------------------------------------------
    public int getIndex() {
//    	debug.outln("getId---->_id= " + id);
    	return index;
    }

//-------------------------------------------------------------------------------------
    public DefaultXbitTreeNode add(String _name) {
    	DefaultXbitTreeNode _newNode = new DefaultXbitTreeNode(debug, _name);
    	add(_newNode);
    	_newNode.computeId();
    	return _newNode;
	}

//-------------------------------------------------------------------------------------
    private void computeId() {
    	TreeNode _treeNode = getParent();
    	if(_treeNode != null){
    		String _parentId = ((DefaultXbitTreeNode)_treeNode).getId(); 
    		if(_parentId.equals("")){
    			id = name;
    		} else {
    			id = name + "@" + _parentId;    			
    		}
    	} else {
    		id = name;
    	}
//    	debug.outln("computeId---->_id= " + id);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
