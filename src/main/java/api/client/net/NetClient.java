//-------------------------------------------------------------------------------------
package codex.xbit.api.client.net;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.prefs.*;
import org.ini4j.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;



//-------------------------------------------------------------------------------------
public class NetClient {
    private Debug debug;
    private ClientConfiguration clientConfiguration;
    private ServerConfiguration serverConfiguration;

    private NetCommandRequester netCommandRequester;

//-------------------------------------------------------------------------------------
    public NetClient(Debug _debug, ClientConfiguration _clientConfiguration) {
        debug = _debug;
        clientConfiguration = _clientConfiguration;
        refresh();
    }

//-------------------------------------------------------------------------------------
    public NetClient(Debug _debug, ServerConfiguration _serverConfiguration) {
        debug = _debug;
        serverConfiguration = _serverConfiguration;
		netCommandRequester = new NetCommandRequester(debug, _serverConfiguration, this);
    }

//-------------------------------------------------------------------------------------
    public void refresh() {
        if(netCommandRequester != null){
            netCommandRequester.close();
        }
        ServerConfiguration _serverConfiguration = clientConfiguration.getCurrentServerConfiguration();
		netCommandRequester = new NetCommandRequester(debug, _serverConfiguration, this);
/*
        for (int i = 0; i < serverConfigurations.size(); i++) {
            ServerConfiguration _serverConfiguration = serverConfigurations.get(i);
            if(lastUsedServer.equals(_serverConfiguration.getAlias())){
                currentServerConfiguration = _serverConfiguration;
                netCommandRequester = new NetCommandRequester(debug, _serverConfiguration, this);
                break;
            }
        }*/
    }
/*
//-------------------------------------------------------------------------------------
    public String getZoneId(){
        return zoneId;
    }

//-------------------------------------------------------------------------------------
    public String getUser(){
        return getLastUsedUser();
    }

//-------------------------------------------------------------------------------------
    public String getLastUsedUser(){
        return lastUsedUser;
    }

//-------------------------------------------------------------------------------------
    public String getLastUsedServer(){
        return lastUsedServer;
    }

//-------------------------------------------------------------------------------------
    public String getLastUsedUserAndServer(){
        return  lastUsedUser + "@" + lastUsedServer;
    }
*/
/*
//-------------------------------------------------------------------------------------
    public void setLastUser(String _user, String _server){
        lastUsedUser = _user;
        lastUsedServer = _server;
        refresh();
    }
*/
//-------------------------------------------------------------------------------------
    public NetCommandRequester getNetCommandRequester() {
    	if(clientConfiguration != null){
    		refresh();    		
    	}
        return netCommandRequester;
    }
/*
//-------------------------------------------------------------------------------------
    public String getServerName() {
        return currentServerConfiguration.getHost();
    }

//-------------------------------------------------------------------------------------
    public void setServerPort(int _port) {
        currentServerConfiguration.setPort(_port);
        saveIni();
        refresh();
    }

//-------------------------------------------------------------------------------------
    public int getServerPort() {
        return currentServerConfiguration.getPort();
    }
*/


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void save() {
        
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
