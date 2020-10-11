//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.envs;

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
import codex.xbit.api.client.gui.tab.tabs.envs.panels.single.*;
import codex.xbit.api.client.gui.tab.tabs.envs.panels.users.*;


//-------------------------------------------------------------------------------------
public class EnvsLoader extends AbstractLoader {
	private ClientConfiguration clientConfiguration;
    private ServerConfiguration serverConfiguration;

    
//-------------------------------------------------------------------------------------
    public EnvsLoader(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration, ServerConfiguration _serverConfiguration) {
    	super(_debug, _status, _tab, _parentNode);
    	clientConfiguration = _clientConfiguration;
        serverConfiguration = _serverConfiguration;
        debug.outln("EnvsPanel.init...");
        start();
    }

//-------------------------------------------------------------------------------------
    protected void load(){
    	String _tabName0 = "single";
    	DefaultXbitTreeNode _newNode0 = parentNode.add(_tabName0);
		SingleEnvPanel _singleEnvPanel = new SingleEnvPanel(debug, status, tab, _newNode0, clientConfiguration, serverConfiguration);
        addTab(_newNode0.getId(), _singleEnvPanel);

    	String _tabName1 = "users";
    	DefaultXbitTreeNode _newNode1 = parentNode.add(_tabName1);
		UsersEnvPanel _usersEnvPanel = new UsersEnvPanel(debug, status, tab, _newNode1, clientConfiguration, serverConfiguration);
        addTab(_newNode1.getId(), _usersEnvPanel);

    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
