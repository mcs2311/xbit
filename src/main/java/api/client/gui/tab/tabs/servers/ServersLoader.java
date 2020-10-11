//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.servers;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.client.net.*;

import codex.xbit.api.client.gui.components.panels.*;
import codex.xbit.api.client.gui.tab.common.loaders.*;
import codex.xbit.api.client.gui.status.*;
import codex.xbit.api.client.gui.tab.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class ServersLoader extends AbstractLoader {
    private ClientConfiguration clientConfiguration;

    
//-------------------------------------------------------------------------------------
    public ServersLoader(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration) {
    	super(_debug, _status, _tab, _parentNode);
        clientConfiguration = _clientConfiguration;
//        start();
    }

//-------------------------------------------------------------------------------------
    protected void load(){
//        debug.outln("ServersContainer.updateTasksList...size="+_netRequesters.size());
    	java.util.List<ServerConfiguration> serverConfigurations = clientConfiguration.getServerConfigurations();
        for (int i = 0; i < serverConfigurations.size() ; i++) {
            ServerConfiguration _serverConfiguration = serverConfigurations.get(i);
        	String _tabName = _serverConfiguration.getAlias();
        	DefaultXbitTreeNode _newNode = parentNode.add(_tabName);
    		ServerPanel _serverPanel = new ServerPanel(debug, status, tab, _newNode, clientConfiguration, _serverConfiguration);
            addTab(_newNode.getId(), _serverPanel);
        }
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
