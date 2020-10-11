//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab;

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

import codex.xbit.api.client.gui.components.panels.*;
//import codex.xbit.api.client.gui.selector.tree.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;
import codex.xbit.api.client.gui.status.*;
import codex.xbit.api.client.gui.tab.tabs.servers.*;


//-------------------------------------------------------------------------------------
public class Tab extends AbstractPanel {
	private DefaultXbitTreeNode treeNode;
	private ClientConfiguration clientConfiguration;
	private ServersLoader serversLoader;
    private JTabbedPane tabbedPane;

//-------------------------------------------------------------------------------------
    public Tab(Debug _debug, Status _status, DefaultXbitTreeNode _treeNode, ClientConfiguration _clientConfiguration) {
    	super(_debug, _status, null);
    	treeNode = _treeNode;
    	clientConfiguration = _clientConfiguration;

        setLayout(new BorderLayout(0, 0));
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        add(tabbedPane, BorderLayout.CENTER);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        start();
    }

//-------------------------------------------------------------------------------------
    public void run(){
        serversLoader = new ServersLoader(debug, status, this, treeNode, clientConfiguration);
    	serversLoader.run();
    }

//-------------------------------------------------------------------------------------
    public void addTab(String _id, AbstractPanel _panel){
    	tabbedPane.addTab(_id, _panel);
	}

//-------------------------------------------------------------------------------------
    public void selectTab(String _id){
    	selectIndex(getIndexOfTab(_id));
	}

//-------------------------------------------------------------------------------------
    protected void selectIndex(int _index){
    	debug.outln("Selecting index = " + _index);
    	if(_index < tabbedPane.getTabCount()){
        	tabbedPane.setSelectedIndex(_index);
    	}
    }

//-------------------------------------------------------------------------------------
    protected int getIndexOfTab(String _tabName){
//    	int count = tabbedPane.getTabCount();
    	return tabbedPane.indexOfTab(_tabName);
    	/*
		debug.outln("title = " + _title+", _tabName="+_tabName);
    	for (int i = 0; i < tabbedPane.getTabCount(); i++) {
    		String _title = ((JLabel)tabbedPane.getTabComponentAt(i)).getText();
    		if(_title.equals(_tabName)){
    			return i;
    		}
    	}
    	return -1;*/
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
