//-------------------------------------------------------------------------------------
package codex.xbit.api.client.cli;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.time.*;
import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.client.net.*;
import codex.xbit.api.client.gui.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.server.trader.*;

//-------------------------------------------------------------------------------------
public class CommandImplementor extends Thread {
    protected Debug debug;
    protected ClientConfiguration clientConfiguration;
    protected NetClient netClient;
    protected Gui gui;
    protected PilotConfiguration pilotConfiguration;
    protected TraderConfiguration traderConfiguration;

    private static final int DEFAULT_PING_TIMEOUT = 10;

//-------------------------------------------------------------------------------------
    public CommandImplementor(Debug _debug, ClientConfiguration _clientConfiguration) {
        debug = _debug;
        clientConfiguration = _clientConfiguration;
        pilotConfiguration = new PilotConfiguration(_debug);
        traderConfiguration = new TraderConfiguration(_debug);
        netClient = new NetClient(_debug, _clientConfiguration);
    }
/*
//-------------------------------------------------------------------------------------
    public String getZoneId() {
        return netClient.getZoneId();
    }

//-------------------------------------------------------------------------------------
    public String getLastUsedUser() {
        return netClient.getLastUsedUser();
    }

//-------------------------------------------------------------------------------------
    public String getLastUsedServer() {
        return netClient.getLastUsedServer();
    }

//-------------------------------------------------------------------------------------
    public String getServerName() {
        return netClient.getServerName();
    }

//-------------------------------------------------------------------------------------
    public int getServerPort() {
        return netClient.getServerPort();
    }

//-------------------------------------------------------------------------------------
    public long getStartTime() {
        return netClient.getStartTime();
    }
*/

//-------------------------------------------------------------------------------------
    public PilotConfiguration getPilotConfiguration() {
        return pilotConfiguration;
    }

//-------------------------------------------------------------------------------------
    public TraderConfiguration getTraderConfiguration() {
        return traderConfiguration;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected void executeChangePilotMode(int _mode, String[] _cmdArray) {
    	if(_cmdArray.length < 1){
    		return;
    	}
//    	int _mode = PilotConfiguration.getModeAsInt(_cmdArray[0]);
        int _prefixColorLevel = PilotConfiguration.getModeColor(_mode);
        debug.out(Debug.IMPORTANT1, "Change pilot to [");
        debug.out(_prefixColorLevel, PilotConfiguration.getModeAsString(_mode), false);
        debug.out(Debug.IMPORTANT1, "] mode ... ", false);
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
//        String _parameter = (_cmdArray.length => 2) ? : null
        NetCommand _netCommand = new NetCommand(NetCommand.COMMAND_CHANGE_PILOT_MODE, _mode, _cmdArray);
        _netCommandRequester.send(_netCommand);
        _netCommand = (NetCommand)_netCommandRequester.receive();
        if((_netCommand != null) && (_netCommand.getCommand() == NetCommand.COMMAND_OK)){
        	getConfiguration(_netCommand);
            debug.outln(Debug.IMPORTANT1, "OK", false);
        } else {
            debug.outln(Debug.IMPORTANT1, "ERROR", false);
        }
    }
//-------------------------------------------------------------------------------------
    protected void executeChangePilotType(String[] _cmdArray) {
    	if(_cmdArray.length < 2){
        	debug.out(Debug.IMPORTANT1, "Please specify pilot type!");
    		return;
    	}
        debug.out(Debug.IMPORTANT1, "Change pilot to [");
        debug.out(_cmdArray[1], false);
        debug.out(Debug.IMPORTANT1, "] type ... ", false);
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
//        String _parameter = (_cmdArray.length => 2) ? : null
        NetCommand _netCommand = new NetCommand(NetCommand.COMMAND_CHANGE_PILOT_TYPE, -1, _cmdArray);
        _netCommandRequester.send(_netCommand);
        _netCommand = (NetCommand)_netCommandRequester.receive();
        if((_netCommand != null) && (_netCommand.getCommand() == NetCommand.COMMAND_OK)){
        	getConfiguration(_netCommand);
            debug.outln(Debug.IMPORTANT1, "OK", false);
        } else {
            debug.outln(Debug.IMPORTANT1, "ERROR", false);
        }
    }

//-------------------------------------------------------------------------------------
    public void executeBuy(String[] _cmdArray) {
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        _netCommandRequester.send(new NetCommand(NetCommand.COMMAND_BUY, _cmdArray));
//        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
	}

//-------------------------------------------------------------------------------------
    public void executeGuiStart() {
        gui = new Gui(this, clientConfiguration);
    }

//-------------------------------------------------------------------------------------
    public void executeGuiStop() {
        gui.dispose();
        gui = null;
    }

//-------------------------------------------------------------------------------------
    protected void executeHelp() {
    	
    }

//-------------------------------------------------------------------------------------
    protected void executeOrderbook(String[] _args) {
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        _netCommandRequester.send(new NetCommand(NetCommand.COMMAND_DEAL, _args));
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
        getConfiguration(_netCommand);
    }

//-------------------------------------------------------------------------------------
    protected boolean executePing() {
    	return executePing(DEFAULT_PING_TIMEOUT);
    }

//-------------------------------------------------------------------------------------
    protected boolean executePing(int _timeout) {
//        debug.outln(Debug.ERROR, "executePing...");

        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        if(_netCommandRequester == null){
        	return false;
        }
        _netCommandRequester.send(new NetCommand(NetCommand.COMMAND_PING), _timeout);
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive(_timeout);        
        getConfiguration(_netCommand);
        return (_netCommand != null);
    }

//-------------------------------------------------------------------------------------
    protected void executeProfit(String[] _args) {
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        _netCommandRequester.send(new NetCommand(NetCommand.COMMAND_PROFIT, _args));
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
        getConfiguration(_netCommand);
    }

//-------------------------------------------------------------------------------------
    protected void executeReload() {
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        _netCommandRequester.send(new NetCommand(NetCommand.COMMAND_RELOAD));
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
        getConfiguration(_netCommand);
    }

//-------------------------------------------------------------------------------------
    protected void executeSwitchToMode(int _mode, String[] _cmdArray) {
        int _prefixColorLevel = TraderConfiguration.getModeColor(_mode);
        debug.out(Debug.IMPORTANT1, "Switching to the [");
        debug.out(_prefixColorLevel, TraderConfiguration.getModeAsString(_mode), false);
        debug.out(Debug.IMPORTANT1, "] mode ... ", false);
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
//        String _parameter = (_cmdArray.length => 2) ? : null
        NetCommand _netCommand = new NetCommand(NetCommand.COMMAND_CHANGE_TRADER_MODE, _mode, _cmdArray);
        _netCommandRequester.send(_netCommand);
        _netCommand = (NetCommand)_netCommandRequester.receive();
        if((_netCommand != null) && (_netCommand.getCommand() == NetCommand.COMMAND_OK)){
        	getConfiguration(_netCommand);
            debug.outln(Debug.IMPORTANT1, "OK", false);
        } else {
            debug.outln(Debug.IMPORTANT1, "ERROR", false);
        }
    }

//-------------------------------------------------------------------------------------
    protected void executeSwitchToState(int _state, String[] _cmdArray) {
        int _prefixColorLevel = TraderConfiguration.getStateColor(_state);
        debug.out(Debug.IMPORTANT1, "Switching to the [");
        debug.out(_prefixColorLevel, TraderConfiguration.getStateAsString(_state), false);
        debug.out(Debug.IMPORTANT1, "] state ... ", false);
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        NetCommand _netCommand = new NetCommand(NetCommand.COMMAND_CHANGE_TRADER_STATE, _state, _cmdArray);
        _netCommandRequester.send(_netCommand);
        _netCommand = (NetCommand)_netCommandRequester.receive();
        if((_netCommand != null) && (_netCommand.getCommand() == NetCommand.COMMAND_OK)){
        	getConfiguration(_netCommand);
            debug.outln(Debug.IMPORTANT1, "OK", false);
        } else {
            debug.outln(Debug.IMPORTANT1, "ERROR", false);
        }
    }


//-------------------------------------------------------------------------------------
    public void executeChangePort(String[] _cmdArray) {
        if (_cmdArray.length < 2) {
            debug.outln(Debug.ERROR, "No port specified!");
            return;
        }
//        netClient.setServerPort(Integer.parseInt(_cmdArray[1]));
    }

//-------------------------------------------------------------------------------------
    public void executeSave() {
        debug.outln(Debug.ERROR, "Not implemented!");
    }


//-------------------------------------------------------------------------------------
    public void executeShowExchanges() {
        debug.outln("Show exchanges...");
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        NetCommand _netCommand0 = new NetCommand(NetCommand.COMMAND_SHOW, NetCommand.COMMAND_SHOW_EXCHANGES, clientConfiguration.getLastUser());
        _netCommandRequester.send(_netCommand0);
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
        if(_netCommand != null) {
            @SuppressWarnings("unchecked")
            List<String> _messages = (List<String>)_netCommand.getMessage();
            if(_messages == null){
                debug.outln(Debug.IMPORTANT1, "ERROR in show command");
            } else {
            	debug.outln(_messages);            	
            }
        }
//        debug.outln(Debug.IMPORTANT1, _message);
    }

//-------------------------------------------------------------------------------------
    public void executeShowOrderbooks() {
        debug.outln("Show orderbooks...");
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        NetCommand _netCommand0 = new NetCommand(NetCommand.COMMAND_SHOW, NetCommand.COMMAND_SHOW_ORDERBOOKS, clientConfiguration.getLastUser());
        _netCommandRequester.send(_netCommand0);
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
        if(_netCommand != null) {
            @SuppressWarnings("unchecked")
            List<String> _messages = (List<String>)_netCommand.getMessage();
            if(_messages == null){
                debug.outln(Debug.IMPORTANT1, "ERROR in show command");
            } else {
            	debug.outln(_messages);            	
            }
        }
//        debug.outln(Debug.IMPORTANT1, _message);
    }


//-------------------------------------------------------------------------------------
    public void executeShowWhales() {
        debug.outln("Show whales...");
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        NetCommand _netCommand0 = new NetCommand(NetCommand.COMMAND_SHOW, NetCommand.COMMAND_SHOW_WHALES);
        _netCommandRequester.send(_netCommand0);
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
        if(_netCommand != null) {
            @SuppressWarnings("unchecked")
            List<String> _messages = (List<String>)_netCommand.getMessage();
            if(_messages == null){
                debug.outln(Debug.IMPORTANT1, "ERROR in show command");
            } else {
            	debug.outln(_messages);            	
            }
        }
//        debug.outln(Debug.IMPORTANT1, _message);
    }

//-------------------------------------------------------------------------------------
    public void executeSell(String[] _cmdArray) {
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        _netCommandRequester.send(new NetCommand(NetCommand.COMMAND_SELL, _cmdArray));
//        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
	}

//-------------------------------------------------------------------------------------
    public void executeStrategy(String[] _cmdArray) {
        NetCommandRequester _netCommandRequester = netClient.getNetCommandRequester();
        _netCommandRequester.send(new NetCommand(NetCommand.COMMAND_STRATEGY, _cmdArray));
        NetCommand _netCommand = (NetCommand)_netCommandRequester.receive();
        getConfiguration(_netCommand);
	}

//-------------------------------------------------------------------------------------
    public void executeUse(String[] _cmdArray) {
    	String _alias;
    	
/*    	for (int i = 0; i < _cmdArray.length ; i++) {
            debug.outln(Debug.ERROR, "_cmdArray["+i+"]="+_cmdArray[i]);
    	}*/

        if ((_cmdArray.length < 2) || (_cmdArray[1] == null) || (_cmdArray[1].trim().isEmpty())){
//            debug.outln(Debug.ERROR, "Expected argument missing!");
	        _alias = clientConfiguration.getLastUserAndServer();
//	        execute(new String[] {"use", _lastUsedServer});
//            return;
        } else {
        	_alias = _cmdArray[1];
        }

        String[] _str = _alias.split("@");
        String _user;
        String _server;
        if(_str.length == 1){
        	_user = clientConfiguration.getLastUser();
        	_server = _str[0];
        } else {
        	_user = _str[0];
        	_server = _str[1];
        }
        int _index = 0;
        do{
        	debug.outln("Using [" + _user + "@" + _server + "] ... ");
        	clientConfiguration.setLastUserAndServer(_user, _server);
        	_server = clientConfiguration.getNextServer(_server);
        	if(_server == null){
        		break;
        	}
//        	executePing();
        	_index++;
        } while(!executePing(_index + DEFAULT_PING_TIMEOUT));
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void executeExit(boolean _forceExit) {
        netClient.save();
        if (_forceExit) {
            debug.outln(Debug.IMPORTANT1, "Exiting...1");
            Runtime.getRuntime().removeShutdownHook(this);
//            netClient.disconnect();
            System.exit(0);
//        } else if(netClient.isTransmitting()) {
//            debug.outln("Dettaching from server...");
//            netClient.disconnect();
        } else {
            debug.outln(Debug.IMPORTANT1, "Exiting...0");
//            Runtime.getRuntime().removeShutdownHook(this);
            System.exit(0);
        }
    }

@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    protected void getConfiguration(NetCommand _netCommand) {
        if(_netCommand != null){
//        	debug.outln(Debug.INFO, "getConfiguration...");
			List<Object> _list = (List<Object>)_netCommand.getMessage();
			pilotConfiguration 	= (PilotConfiguration) _list.get(0);
            traderConfiguration = (TraderConfiguration)_list.get(1);
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

}
//-------------------------------------------------------------------------------------
