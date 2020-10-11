//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.users;

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
import codex.xbit.api.client.gui.tab.tabs.tactics.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class UserPanel extends AbstractPanel {
    private ServerConfiguration serverConfiguration;
    private UserConfiguration userConfiguration;

    private TacticsLoader tacticsLoader;
    private NetClient netClient;
    private NetCommandRequester netCommandRequester;


//-------------------------------------------------------------------------------------
    public UserPanel(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ServerConfiguration _serverConfiguration, UserConfiguration _userConfiguration) {
    	super(_debug, _status, _tab);
        serverConfiguration = _serverConfiguration;
        userConfiguration = _userConfiguration;
/*
        currencyPairsPanel = new CurrencyPairsPanel(_debug, this, _status, _serverConfiguration, _user);
        setLayout(new BorderLayout(0, 0));
        add(currencyPairsPanel, BorderLayout.CENTER);
        */
        tacticsLoader = new TacticsLoader(_debug, _status, _tab, _parentNode, _serverConfiguration, _userConfiguration);
//        setLayout(new BorderLayout(0, 0));
//        add(tacticsPanel, BorderLayout.CENTER);
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
