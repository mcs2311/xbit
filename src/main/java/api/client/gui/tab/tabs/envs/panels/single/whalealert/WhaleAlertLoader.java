//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.envs.panels.single.whalealert;

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
import codex.xbit.api.client.gui.tab.common.loaders.*;
import codex.xbit.api.client.gui.components.tables.models.*;
import codex.xbit.api.client.gui.status.*;
import codex.xbit.api.client.gui.tab.*;
import codex.xbit.api.client.gui.tab.tabs.users.*;
import codex.xbit.api.client.gui.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class WhaleAlertLoader extends AbstractLoader {
	private ClientConfiguration clientConfiguration;
    private ServerConfiguration serverConfiguration;

//-------------------------------------------------------------------------------------
    public WhaleAlertLoader(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration, ServerConfiguration _serverConfiguration) {
    	super(_debug, _status, _tab, _parentNode);
    	clientConfiguration = _clientConfiguration;
        serverConfiguration = _serverConfiguration;
    	start();
    }

//-------------------------------------------------------------------------------------
    public void load() {
    	String _tabName = "WhaleAlert";
    	DefaultXbitTreeNode _newNode = parentNode.add(_tabName);
		WhaleAlertPanel _whaleAlertPanel = new WhaleAlertPanel(debug, status, tab, _newNode, clientConfiguration, serverConfiguration);
        addTab(_newNode.getId(), _whaleAlertPanel);
    }
    
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
