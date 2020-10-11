//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.envs.panels.single;

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
import codex.xbit.api.client.gui.tab.tabs.envs.panels.single.whalealert.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class SingleEnvPanel extends AbstractPanel {
    private ServerConfiguration serverConfiguration;

    private NetClient netClient;
    private NetCommandRequester netCommandRequester;


//-------------------------------------------------------------------------------------
    public SingleEnvPanel(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration, ServerConfiguration _serverConfiguration) {
    	super(_debug, _status, _tab);
        serverConfiguration = _serverConfiguration;
//		WhaleAlertPanel _whaleAlertPanel = new WhaleAlertPanel(debug, status, tab, _newNode, _serverConfiguration);
		WhaleAlertLoader _whaleAlertLoader = new WhaleAlertLoader(_debug, _status, _tab, _parentNode, _clientConfiguration, _serverConfiguration);
//        add(_newNode.getId(), _whaleAlertPanel);
//		start();   
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
