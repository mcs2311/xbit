//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.users;

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
public class UsersLoader extends AbstractLoader {
	private ClientConfiguration clientConfiguration;
    private ServerConfiguration serverConfiguration;
    private NetClient netClient;
    private NetCommandRequester netCommandRequester;
    
//-------------------------------------------------------------------------------------
    public UsersLoader(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration, ServerConfiguration _serverConfiguration) {
    	super(_debug, _status, _tab, _parentNode);
    	clientConfiguration = _clientConfiguration;
        serverConfiguration = _serverConfiguration;
        debug.outln("UsersPanel.init...");
        start();
    }

@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    protected void load(){
//        debug.outln("UsersPanel.load..."+serverConfiguration.getAlias()+"...0");
    	netClient = new NetClient(debug, serverConfiguration);
    	netCommandRequester = netClient.getNetCommandRequester();

//        debug.outln("UsersPanel.load..."+serverConfiguration.getAlias()+"...1");
        netCommandRequester.send(new NetCommand(NetCommand.COMMAND_SHOW, NetCommand.COMMAND_SHOW_USERS, "all"));
//        debug.outln("UsersPanel.load..."+serverConfiguration.getAlias()+"...2");
        NetCommand _netCommand = (NetCommand)netCommandRequester.receive();
//        debug.outln("UsersPanel.load..."+serverConfiguration.getAlias()+"...3");
        java.util.List<UserConfiguration> _userConfigurations = null;
        if(_netCommand != null){
//            debug.out(Debug.IMPORTANT1, "_netCommand not null");
            _userConfigurations = (java.util.List<UserConfiguration>)_netCommand.getMessage();
        } else {
            debug.out(Debug.IMPORTANT1, "UsersPanel _netCommand null");            
        }

//		debug.outln(Debug.IMPORTANT1, "_userConfigurations.size=: "+_userConfigurations.size());

        for (int i = 0; i < _userConfigurations.size() ; i++) {
        	UserConfiguration _userConfiguration = _userConfigurations.get(i);
            debug.outln(Debug.IMPORTANT1, "server: "+serverConfiguration.getAlias()+", _user= "+_userConfiguration.getUserName());
        	String _tabName = _userConfiguration.getUserName();
        	DefaultXbitTreeNode _newNode = parentNode.add(_tabName);
    		UserPanel _userPanel = new UserPanel(debug, status, tab, _newNode, serverConfiguration, _userConfiguration);
            addTab(_newNode.getId(), _userPanel);
        }
    }
/*
//-------------------------------------------------------------------------------------
    protected void selectPanel(){
    	String _user = clientConfiguration.getLastUser();
    	debug.outln(Debug.INFO, "Select users="+_user);
    	selectUser(_user);
    }

//-------------------------------------------------------------------------------------
    public void selectUser(String _user){
    	int _index = getIndexOfTab(_user);
    	selectIndex(_index);
    }
*/
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
