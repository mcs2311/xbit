//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.tactics;

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
import codex.xbit.api.client.gui.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class TacticsLoader extends AbstractLoader {
    private ServerConfiguration serverConfiguration;
    private UserConfiguration userConfiguration;

    private NetClient netClient;
    private NetCommandRequester netCommandRequester;
    
//-------------------------------------------------------------------------------------
    public TacticsLoader(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ServerConfiguration _serverConfiguration, UserConfiguration _userConfiguration) {
    	super(_debug, _status, _tab, _parentNode);
        serverConfiguration = _serverConfiguration;
        userConfiguration = _userConfiguration;
        start();
    }

@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    protected void load(){
//        debug.outln("ServersContainer.updateTasksList...size="+_netRequesters.size());
    	netClient = new NetClient(debug, serverConfiguration);
    	netCommandRequester = netClient.getNetCommandRequester();

        netCommandRequester.send(new NetCommand(NetCommand.COMMAND_SHOW, NetCommand.COMMAND_SHOW_TACTICS, userConfiguration.getUserName()));
        NetCommand _netCommand = (NetCommand)netCommandRequester.receive();
        java.util.List<TacticConfiguration> _tacticConfigurations = null;
        if(_netCommand != null){
            _tacticConfigurations = (java.util.List<TacticConfiguration>)_netCommand.getMessage();
            if(_tacticConfigurations != null){
            	debug.outln(Debug.IMPORTANT1, "_netCommand not null: _tacticConfigurations.size="+_tacticConfigurations.size());
            } else {
            	debug.outln(Debug.IMPORTANT1, "_tacticConfigurations is null");
            	return;
            }
        } else {
            debug.outln(Debug.IMPORTANT1, "_netCommand null");            
        }


        for (int i = 0; i < _tacticConfigurations.size() ; i++) {
        	TacticConfiguration _tacticConfiguration = _tacticConfigurations.get(i);
            debug.outln(Debug.IMPORTANT1, "server: "+serverConfiguration.getAlias()+", _user= "+userConfiguration.getUserName()+", _tactic="+_tacticConfiguration.getName());
        	String _tabName = _tacticConfiguration.getName();
        	DefaultXbitTreeNode _newNode = parentNode.add(_tabName);
    		TacticPanel _tacticPanel = new TacticPanel(debug, status, tab, _newNode, serverConfiguration, userConfiguration, _tacticConfiguration);
            addTab(_newNode.getId(), _tacticPanel); 
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
