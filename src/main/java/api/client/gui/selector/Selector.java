//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.selector;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
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
import codex.xbit.api.client.gui.selector.tree.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class Selector extends JPanel implements ActionListener {
	private Debug debug;
    private Tab tab;
	private DefaultXbitTreeNode rootNode;
	private ClientConfiguration clientConfiguration;

	private JComboBox<String> serverList; 
	private Tree tree;

//-------------------------------------------------------------------------------------
    public Selector(Debug _debug, Tab _tab, DefaultXbitTreeNode _rootNode, ClientConfiguration _clientConfiguration) {
//		super(_rootNode);
        debug = _debug;
        tab = _tab;
        rootNode = _rootNode;
        clientConfiguration = _clientConfiguration;

        setLayout(new BorderLayout(0, 0));

		add(getServerList(), BorderLayout.PAGE_START);
		add(getTree(), BorderLayout.PAGE_END);
    }

//-------------------------------------------------------------------------------------
    private JComboBox getServerList() {
		serverList = new JComboBox<String>();
		java.util.List<ServerConfiguration> _serverConfigurations = clientConfiguration.getServerConfigurations();

		for(int i = 0; i < _serverConfigurations.size(); i++){
			serverList.addItem(_serverConfigurations.get(i).getAlias());
		}

		serverList.setSelectedItem(clientConfiguration.getLastServer());
		serverList.addActionListener(this);

		return serverList;
    }

//-------------------------------------------------------------------------------------
    private Tree getTree() {
		tree = new Tree(debug, tab, rootNode, clientConfiguration);
		return tree;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void actionPerformed(ActionEvent _e){

    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
