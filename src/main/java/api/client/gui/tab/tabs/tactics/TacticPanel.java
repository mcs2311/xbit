//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.tactics;

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
import codex.xbit.api.client.gui.tab.tabs.currencypairs.*;
import codex.xbit.api.client.gui.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class TacticPanel extends AbstractPanel {
    private ServerConfiguration serverConfiguration;
    private UserConfiguration userConfiguration;
    private TacticConfiguration tacticConfiguration;

    private CurrencyPairsLoader currencyPairsLoader;
    private NetClient netClient;
    private NetCommandRequester netCommandRequester;


//-------------------------------------------------------------------------------------
    public TacticPanel(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ServerConfiguration _serverConfiguration, UserConfiguration _userConfiguration, TacticConfiguration _tacticConfiguration) {
    	super(_debug, _status, _tab);
        serverConfiguration = _serverConfiguration;
        userConfiguration = _userConfiguration;
        tacticConfiguration = _tacticConfiguration;

        currencyPairsLoader = new CurrencyPairsLoader(_debug, _status, _tab, _parentNode, _serverConfiguration, _userConfiguration, _tacticConfiguration);
//        setLayout(new BorderLayout(0, 0));
//        add(currencyPairsPanel, BorderLayout.CENTER);
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
