//-------------------------------------------------------------------------------------
package codex.xbit.api.server.net;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;

import codex.xbit.api.server.trader.*;

//-------------------------------------------------------------------------------------
public class NetCommandResponder extends Thread {
    private Debug debug;
    private NetStream netStream;
//    private NetServer netServer;
    private Trader trader;

    private ServerConfiguration serverConfiguration;
    private PilotConfiguration  pilotConfiguration;
    private TraderConfiguration traderConfiguration;

//-------------------------------------------------------------------------------------
    public NetCommandResponder(Debug _debug, Socket _socket, Trader _trader) {
        debug = _debug;
        netStream = new NetStream(_debug, _socket);
//        serverConfiguration = _serverConfiguration;
        trader = _trader;
//        debug.outln("Creating NetCommandResponder..._taskId=");
        serverConfiguration = trader.getResolver().getServerConfiguration();
        pilotConfiguration = trader.getResolver().getPilotConfiguration();
        traderConfiguration = trader.getResolver().getTraderConfiguration();
        start();
    }

//-------------------------------------------------------------------------------------
    public void run() {
        NetCommand _netCommand;
//        debug.outln("******NetCommandResponder.run....isNonBlocking:");
//        processCommand(new NetCommand(NetCommand.COMMAND_PING));
        while (netStream.isConnected()) {
//            debug.outln("Creating NetCommandResponder.run....0");
            _netCommand = (NetCommand)netStream.readPacket();
            if (_netCommand == null) {
                break;
            }
            processCommand(_netCommand);
        }
    }

//-------------------------------------------------------------------------------------
    private void processCommand(NetCommand _netCommand) {
        int _command = _netCommand.getCommand();
//        debug.outln("NetCommandResponder command = "+_command);
        _netCommand.setStartTime(serverConfiguration.getStartTime());
        switch (_command) {
        case NetCommand.COMMAND_NONE: {
            break;
        }

        case NetCommand.COMMAND_BUY: {
            trader.sendSignal((String[])_netCommand.getMessage());
            break;
        }

        case NetCommand.COMMAND_DEAL: {
            trader.changeOrderbook((String[])_netCommand.getMessage());
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
//            _netCommand.setMessage(traderConfiguration);
            netStream.writePacket(_netCommand);
            break;
        }

        case NetCommand.COMMAND_PING: {
//        	debug.outln("NetCommandResponder.COMMAND_PING.0: ");
            _netCommand.setCommand(NetCommand.COMMAND_PING);
            setConfiguration(_netCommand);
//            _netCommand.setMessage(traderConfiguration);
            netStream.writePacket(_netCommand);
//        	debug.outln("NetCommandResponder.COMMAND_PING.1: ");
            break;
        }

        case NetCommand.COMMAND_PROFIT: {
            trader.changeProfit((String[])_netCommand.getMessage());
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
//            _netCommand.setMessage(traderConfiguration);
            netStream.writePacket(_netCommand);
            break;
        }

        case NetCommand.COMMAND_CHANGE_PILOT_MODE: {
            trader.getResolver().getPilots().setPilotMode((int)_netCommand.getParameter(), _netCommand.getMessage());
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
            netStream.writePacket(_netCommand);
            break;
        }

        case NetCommand.COMMAND_CHANGE_PILOT_TYPE: {
            trader.getResolver().getPilots().setPilotType(_netCommand.getMessage());
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
            netStream.writePacket(_netCommand);
            break;
        }

        case NetCommand.COMMAND_CHANGE_TRADER_MODE: {
//            debug.outln(Debug.ERROR, "NetCommandResponder.COMMAND_CHANGE_STATE:...0: ");
//            traderConfiguration.setState((int)_netCommand.getParameter());
        	trader.getResolver().getPilots().setTraderMode((int)_netCommand.getParameter(), _netCommand.getMessage());
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
//            _netCommand.setMessage(traderConfiguration);
            netStream.writePacket(_netCommand);
            break;
        }


        case NetCommand.COMMAND_CHANGE_TRADER_STATE: {
//            debug.outln(Debug.ERROR, "NetCommandResponder.COMMAND_CHANGE_STATE:...0: ");
//            traderConfiguration.setState((int)_netCommand.getParameter());
        	trader.getResolver().getPilots().setTraderState((int)_netCommand.getParameter(), _netCommand.getMessage());
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
//            _netCommand.setMessage(traderConfiguration);
            netStream.writePacket(_netCommand);
            break;
        }

        case NetCommand.COMMAND_RELOAD: {
//            trader.reload();
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
//            _netCommand.setMessage(traderConfiguration);
            netStream.writePacket(_netCommand);
            break;
        }

        case NetCommand.COMMAND_SELL: {
            trader.sendSignal((String[])_netCommand.getMessage());
            break;
        }


        case NetCommand.COMMAND_SHOW: {
//            List<String> _messages = null;//trader.getInfo((int)_netCommand.getParameter());
            Object _message = trader.getInfo((int)_netCommand.getParameter(), _netCommand.getMessage());
            _netCommand.setMessage(_message);
            debug.outln(Debug.ERROR, "NetCommandResponder.COMMAND_SHOW:...0: "+(int)_netCommand.getParameter());
            netStream.writePacket(_netCommand);
            debug.outln(Debug.ERROR, "NetCommandResponder.COMMAND_SHOW:...1");
            break;
        }

        case NetCommand.COMMAND_STRATEGY: {
            trader.changeStrategy((String[])_netCommand.getMessage());
            _netCommand.setCommand(NetCommand.COMMAND_OK);
            setConfiguration(_netCommand);
//            _netCommand.setMessage(traderConfiguration);
            netStream.writePacket(_netCommand);
            break;
        }

        case NetCommand.COMMAND_QUIT: {
            System.exit(0);
            break;
        }
        default: {
            debug.outln(Debug.ERROR, "Unknown command: " + _command);
            break;
        }

        }
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private void setConfiguration(NetCommand _netCommand){
		List<Object> _list = new ArrayList<Object>();
		_list.add(pilotConfiguration);
		_list.add(traderConfiguration);
        _netCommand.setMessage(_list);
	}

//-------------------------------------------------------------------------------------
    public void close() {
        netStream.close();
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------