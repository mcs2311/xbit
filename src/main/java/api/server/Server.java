//-------------------------------------------------------------------------------------
package codex.xbit.api.server;
//-------------------------------------------------------------------------------------
import java.io.*;
import org.apache.commons.cli.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;

import codex.common.utils.*;
import codex.xbit.api.server.net.*;
import codex.xbit.api.server.trader.*;

//-------------------------------------------------------------------------------------
public class Server extends Thread {
	private Debug debug;
	private ServerConfigurationLoader serverConfigurationLoader;
    private NetServer netServer;
    private Trader trader;

//-------------------------------------------------------------------------------------
    public Server(CommandLine _cmd) {
        String _home = SystemUtils.getHomeDirectory();
        debug = new Debug(_home + "/.xbit/logs/xserver.log", Debug.INFO);

		serverConfigurationLoader = new ServerConfigurationLoader(debug);
		ServerConfiguration _serverConfiguration = serverConfigurationLoader.getServerConfiguration();

        if(_cmd.hasOption("port")) { 
            int _port = StringUtils.getNumber(_cmd.getOptionValue("port"));
            _serverConfiguration.setPort(_port);
        }
        if(_cmd.hasOption("maxNumberOfThreads")) { 
            int _maxNumberOfThreads = StringUtils.getNumber(_cmd.getOptionValue("maxNumberOfThreads"));
            _serverConfiguration.setMaxNumberOfThreads(_maxNumberOfThreads);
        }

        try {
            Runtime.getRuntime().addShutdownHook(this);
        } catch (Throwable _t) {
            debug.outln("[Main thread] Could not add Shutdown hook");
        }

        trader = new Trader(debug, _serverConfiguration);        
    	netServer = new NetServer(debug, _serverConfiguration, trader);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
//        debug.outln(Debug.WARNING, "Shutdown hook called...");
        try{
        	shutDown();
        }catch(Exception _e){
        	debug.outln(Debug.WARNING, "Shutdown hook called...."+_e.getMessage());
        	_e.printStackTrace();
        }
    }

//-------------------------------------------------------------------------------------
    private void shutDown() {
//        debug.outln("Exiting...0");
        if(netServer != null){
            netServer.save();
        }
        if(trader != null){
            trader.save();
        }
        if(serverConfigurationLoader != null){
        	serverConfigurationLoader.save();
        }
        debug.outln("Exiting...");
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
