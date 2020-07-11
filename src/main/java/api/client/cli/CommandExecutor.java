//-------------------------------------------------------------------------------------
package codex.xbit.api.client.cli;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import codex.common.utils.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.server.trader.*;

//-------------------------------------------------------------------------------------
public class CommandExecutor extends CommandImplementor {

//-------------------------------------------------------------------------------------
    public CommandExecutor(Debug _debug, List<String> _commands, ClientConfiguration _clientConfiguration) {
        super(_debug, _clientConfiguration);
//        String[] _comandArray = new String[] {"use", _command};
        execute(_commands);
    }

//-------------------------------------------------------------------------------------
    private void execute(List<String> _commands) {
    	for (int i = 0; i < _commands.size(); i++) {
    		execute(_commands.get(i));
    	}
    }

//-------------------------------------------------------------------------------------
    public void execute(String _cmd) {
        if(_cmd == null) {
            executeExit(true);
            return;
        }
//        debug.out("_cmd = ["+_cmd+"]");
        String[] _cmdArray = _cmd.split(" ");
        for (int i = 0; i < _cmdArray.length; i++) {
            _cmdArray[i] = _cmdArray[i].trim();
//            debug.out("["+_cmdArray[i]+"]");
        }
        execute(_cmdArray);
    }

//-------------------------------------------------------------------------------------
    public void execute(String[] _cmdArray) {
        String _cmd = _cmdArray[0];
//        debug.outln(Debug.INFO, "Execute command: [" + _cmd + "]...");

        switch(_cmd) {
        case "?":
        case "help": {
            executeHelp();
            return;
        }
        case "a": 
        case "auto": {
            executeChangePilotMode(PilotConfiguration.MODE_AUTO, _cmdArray);
            return;
        }

        case "bear": {
            executeSwitchToState(TraderConfiguration.STATE_BEAR, _cmdArray);
            return;
        }
        case "bull": {
            executeSwitchToState(TraderConfiguration.STATE_BULL, _cmdArray);
            return;
        }
        case "buy": {
            executeBuy(_cmdArray);
            return;
        }
        case "d": 
        case "demo": {
            executeSwitchToMode(TraderConfiguration.MODE_DEMO, _cmdArray);
            return;
        }
        case "g": 
        case "gui": {
            executeGui(_cmdArray);
            return;
        }
        case "orderbook": {
            executeOrderbook(_cmdArray);
            return;
        }
        case "l": 
        case "live": {
            executeSwitchToMode(TraderConfiguration.MODE_LIVE, _cmdArray);
            return;
        }
        case "m": 
        case "manual": {
            executeChangePilotMode(PilotConfiguration.MODE_MANUAL, _cmdArray);
            return;
        }

        case "p": 
        case "pause": {
            executeSwitchToState(TraderConfiguration.STATE_PAUSED, _cmdArray);
            return;
        }

        case "pilot": {
            executeChangePilotType(_cmdArray);
            return;
        }

        case "p!":
        case "panic!": {
            executeSwitchToState(TraderConfiguration.STATE_PANIC, _cmdArray);
            return;
        }

        case "profit": {
            executeProfit(_cmdArray);
            return;
        }
        case "reload": {
            executeReload();
            return;
        }
        case "r": 
        case "resume": 
        case "n": 
        case "normal": {
            executeSwitchToState(TraderConfiguration.STATE_NORMAL, _cmdArray);
            return;
        }
        case "save": {
            executeSave();
            return;
        }
        case "s":
        case "show": {
            executeShow(_cmdArray);
            return;
        }
        case "sell": {
            executeSell(_cmdArray);
            return;
        }
        case "strategy": {
            executeStrategy(_cmdArray);
            return;
        }
        case "u":
        case "use": {
            executeUse(_cmdArray);
            return;
        }
        case "q":
        case "exit":
        case "quit": {
            executeExit(true);
            return;
        }
        case "": 
        case "ping": {
            executePing();
            return;
        }
        default: {
            debug.outln(Debug.ERROR, "Cannot find command: [" + _cmd + "]");
            return;
        }
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void executeGui(String[] _cmdArray) {
        String _cmd = "start";
        if (_cmdArray.length > 1) {
            _cmd = _cmdArray[1];
        }
        switch (_cmd) {
        case "start": {
            if (gui == null) {
                executeGuiStart();
            } else {
                debug.outln(Debug.ERROR, "GUI already started");
            }
            return;
        }
        case "stop": {
            if (gui == null) {
                debug.outln(Debug.ERROR, "GUI not started");
            } else {
                executeGuiStop();
            }
            return;
        }
        case "restart": {
            if (gui == null) {
                debug.outln(Debug.WARNING, "GUI not started");
                executeGuiStart();
            } else {
                executeGuiStop();
                executeGuiStart();
            }
            return;
        }
        }
    }

//-------------------------------------------------------------------------------------
    public void executeShow(String[] _cmdArray) {
        String _cmd = "a";
        if (_cmdArray.length > 1) {
            _cmd = _cmdArray[1];
        }
        switch (_cmd) {
            case "a":
            case "all": {
                executeShowExchanges();
                executeShowOrderbooks();
                executeShowWhales();
                return;
            }
            case "e":
            case "exchanges": {
                executeShowExchanges();
                return;
            }
            case "o":
            case "orderbooks": {
                executeShowOrderbooks();
                return;
            }
            case "w":
            case "whales": {
                executeShowWhales();
                return;
            }
            default: {
                debug.outln(Debug.ERROR, "Unknown option:" + _cmd);
            }
        }
    }

//-------------------------------------------------------------------------------------
    public void save() {
        executeExit(false);
    }

//-------------------------------------------------------------------------------------

}
//-------------------------------------------------------------------------------------
