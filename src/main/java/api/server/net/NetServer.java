//-------------------------------------------------------------------------------------
package codex.xbit.api.server.net;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;
import java.time.*;

import org.ini4j.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;
import codex.xbit.api.server.trader.*;
//import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;


//-------------------------------------------------------------------------------------
public class NetServer extends Thread {
    private Debug debug;
    private ServerConfiguration serverConfiguration;
    private Trader trader;

    private boolean serverIsRunning;

//-------------------------------------------------------------------------------------
    public NetServer(Debug _debug, ServerConfiguration _serverConfiguration, Trader _trader) {
        debug = _debug;
        serverConfiguration = _serverConfiguration;
        trader = _trader;

        serverIsRunning = true;

        String _proxyHost = serverConfiguration.getProxyHost();
        String _proxyPort = serverConfiguration.getProxyPort();
        if(!_proxyHost.equals("")){
	        debug.outln(Debug.IMPORTANT3, "Setting proxy "+_proxyHost+":"+_proxyPort+" ...");
	       	System.setProperty("http.proxyHost", _proxyHost);
			System.setProperty("http.proxyPort", _proxyPort);
        }
        listen();
    }


//-------------------------------------------------------------------------------------
    public void listen() {
        ServerSocket _listener = null;
        int _port = serverConfiguration.getPort();
        debug.outln(Debug.IMPORTANT3, "Starting server at port ["+_port+"] ...");
        try {
            _listener = new ServerSocket(_port);
//            debug.outln("listen...0");
            while (serverIsRunning) {
//                debug.outln("listen...1");
                Socket _socket = _listener.accept();
//                debug.outln("listen...2");
                if(serverIsRunning){
                    NetCommandResponder _netCommandResponder = new NetCommandResponder(debug, _socket, trader);                    
                }
            }
        } catch(IOException _e){
            debug.outln(Debug.WARNING, "IOException: "+_e.getMessage());
//                closeAllConnections();
//                System.exit(1);
        }
    }

//-------------------------------------------------------------------------------------
    public void save() {
        try{
            new Socket("localhost", serverConfiguration.getPort());
        }catch(IOException _e){
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
