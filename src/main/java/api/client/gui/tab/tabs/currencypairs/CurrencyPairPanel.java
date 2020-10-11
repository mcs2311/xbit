//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.currencypairs;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.knowm.xchange.currency.*;


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
import codex.xbit.api.client.gui.selector.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class CurrencyPairPanel extends AbstractPanel {
    private ServerConfiguration serverConfiguration;
    private UserConfiguration userConfiguration;
    private TacticConfiguration tacticConfiguration;
    private CurrencyPair currencyPair;

    private NetClient netClient;
    private NetCommandRequester netCommandRequester;


//-------------------------------------------------------------------------------------
    public CurrencyPairPanel(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ServerConfiguration _serverConfiguration, UserConfiguration _userConfiguration, TacticConfiguration _tacticConfiguration, CurrencyPair _currencyPair) {
    	super(_debug, _status, _tab);
        serverConfiguration = _serverConfiguration;
        userConfiguration = _userConfiguration;
        tacticConfiguration = _tacticConfiguration;
        currencyPair = _currencyPair;
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
