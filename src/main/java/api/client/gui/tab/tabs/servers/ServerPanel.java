//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.servers;

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
import codex.xbit.api.client.gui.status.*;
import codex.xbit.api.client.gui.tab.*;
import codex.xbit.api.client.gui.tab.tabs.envs.*;
import codex.xbit.api.client.gui.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class ServerPanel extends AbstractPanel {
    private ServerConfiguration serverConfiguration;

    private NetClient netClient;
    private NetCommandRequester netCommandRequester;
    private EnvsLoader envsLoader;

//-------------------------------------------------------------------------------------
    public ServerPanel(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration, ServerConfiguration _serverConfiguration) {
    	super(_debug,  _status, _tab);
        serverConfiguration = _serverConfiguration;
        envsLoader = new EnvsLoader(_debug, _status, _tab, _parentNode, _clientConfiguration, _serverConfiguration);
//        setLayout(new BorderLayout(0, 0));
//        add(usersPanel, BorderLayout.CENTER);
		start();   
    }

//-------------------------------------------------------------------------------------
    public void run() {
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
