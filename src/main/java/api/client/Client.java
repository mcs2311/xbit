//-------------------------------------------------------------------------------------
package codex.xbit.api.client;
//-------------------------------------------------------------------------------------
import java.util.*;

import org.apache.commons.cli.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.common.utils.*;
import codex.xbit.api.client.cli.*;

//-------------------------------------------------------------------------------------
public class Client extends Thread {
	private Debug debug;
	private ClientConfigurationLoader clientConfigurationLoader;
    private Cli cli;

//-------------------------------------------------------------------------------------
    public Client(CommandLine _cmd) {
        String _home = SystemUtils.getHomeDirectory();
        debug = new Debug(_home + "/.xbit/logs/xcli.log", Debug.INFO);

        clientConfigurationLoader = new ClientConfigurationLoader(debug);

        List<String> _commands = new ArrayList<String>();
        if(_cmd.hasOption("use")) {
            _commands.add("use " + _cmd.getOptionValue("use"));
        } else {
            _commands.add("ping");
        }
        if(_cmd.hasOption("gui")) {
            _commands.add("gui");
        }

		try {
//            Runtime.getRuntime().runFinalizersOnExit​(true);
//            Runtime.getRuntime().runFinalizersOnExit​(false);
            Runtime.getRuntime().addShutdownHook(this);
        } catch (Throwable _t) {
            debug.outln("[Main thread] Could not add Shutdown hook");
        }
        ClientConfiguration _clientConfiguration = clientConfigurationLoader.getClientConfiguration();
        cli = new Cli(debug, _commands, _clientConfiguration);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
        shutDown();
    }

//-------------------------------------------------------------------------------------
    private void shutDown() {
        debug.outln("Shutdown hook...");
        if(cli != null){
        	cli.save();
        }
//        executeExit(false);
        if(clientConfigurationLoader != null){
        	clientConfigurationLoader.save();
        }
    }


//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
