//-------------------------------------------------------------------------------------
package codex.xbit.api.client.cli;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.client.net.*;

//-------------------------------------------------------------------------------------
public class Cli {
    private Debug debug;
    private CommandReader commandReader;
    private CommandExecutor commandExecutor;

//-------------------------------------------------------------------------------------
    public Cli(Debug _debug, List<String> _commands, ClientConfiguration _clientConfiguration) {
        debug = _debug;
//        debug.outln(Debug.IMPORTANT1, "Loading Xbit CLI..." + _command);
        commandExecutor = new CommandExecutor(_debug, _commands, _clientConfiguration);
        commandReader = new CommandReader(_debug, commandExecutor, _clientConfiguration);
        start();
    }

//-------------------------------------------------------------------------------------
    public void start() {
        while (true) {
            String _cmd = commandReader.readLine();
            commandExecutor.execute(_cmd);
        }
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	if(commandExecutor != null){
    		commandExecutor.save();
    	}
    }

//-------------------------------------------------------------------------------------

}
//-------------------------------------------------------------------------------------
