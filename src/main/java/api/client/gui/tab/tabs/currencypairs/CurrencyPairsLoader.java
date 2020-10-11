//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.currencypairs;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import org.knowm.xchange.currency.*;


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
public class CurrencyPairsLoader extends AbstractLoader {
    private ServerConfiguration serverConfiguration;
    private UserConfiguration userConfiguration;
    private TacticConfiguration tacticConfiguration;

    private NetClient netClient;
    private NetCommandRequester netCommandRequester;
    
//-------------------------------------------------------------------------------------
    public CurrencyPairsLoader(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ServerConfiguration _serverConfiguration, UserConfiguration _userConfiguration, TacticConfiguration _tacticConfiguration) {
    	super(_debug, _status, _tab, _parentNode);
        serverConfiguration = _serverConfiguration;
        userConfiguration = _userConfiguration;
        tacticConfiguration = _tacticConfiguration;
        start();
    }

@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    protected void load(){
//        debug.outln("ServersContainer.updateTasksList...size="+_netRequesters.size());
    	netClient = new NetClient(debug, serverConfiguration);
    	netCommandRequester = netClient.getNetCommandRequester();

    	java.util.List<String> _userAndTactic = new ArrayList<String>();
    	_userAndTactic.add(userConfiguration.getName());
    	_userAndTactic.add(tacticConfiguration.getName());
        netCommandRequester.send(new NetCommand(NetCommand.COMMAND_SHOW, NetCommand.COMMAND_SHOW_CURRENCYPAIRS, (Object)_userAndTactic));
        NetCommand _netCommand = (NetCommand)netCommandRequester.receive();
        java.util.List<CurrencyPair> _currencyPairs = null;
        if(_netCommand != null){
            debug.out(Debug.IMPORTANT1, "_netCommand not null");
            _currencyPairs = (java.util.List<CurrencyPair>)_netCommand.getMessage();
        } else {
            debug.out(Debug.IMPORTANT1, "_netCommand null");            
        }


        for (int i = 0; i < _currencyPairs.size() ; i++) {
        	CurrencyPair _currencyPair = _currencyPairs.get(i);
            debug.outln(Debug.IMPORTANT1, "server: "+serverConfiguration.getAlias()+", _currencyPair= "+_currencyPair);
        	String _tabName = _currencyPair.toString();
        	DefaultXbitTreeNode _newNode = parentNode.add(_tabName);
    		CurrencyPairPanel _currencyPairPanel = new CurrencyPairPanel(debug, status, tab, _newNode, serverConfiguration, userConfiguration, tacticConfiguration, _currencyPair);
            addTab(_newNode.getId(), _currencyPairPanel);
        }
    }
/*
//-------------------------------------------------------------------------------------
    protected void selectPanel(){
//    	String _user = clientConfiguration.getLastUser();
//    	debug.outln(Debug.INFO, "Select users="+_user);
    	selectCurrencyPair(0);
    }

//-------------------------------------------------------------------------------------
    public void selectCurrencyPair(int _index){
//    	int _index = getIndexOfTab(_user);
    	selectIndex(_index);
    }
*/
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
