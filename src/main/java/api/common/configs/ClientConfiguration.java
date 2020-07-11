//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;

import org.ini4j.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class ClientConfiguration implements Serializable {
    private transient Debug debug;
    private String zoneId;
    private String lastUser;
    private String lastServer;

    //---
    private int state;
    private List<ServerConfiguration> serverConfigurations;

    private ServerConfiguration currentServerConfiguration;

    //---
    public static final int STATE_RUNNING = 0;
    public static final int STATE_STOPPED = 1;

//-------------------------------------------------------------------------------------
    public ClientConfiguration(Debug _debug) {
        debug = _debug;
        serverConfigurations = new ArrayList<ServerConfiguration>();
    }

//-------------------------------------------------------------------------------------
    public String getZoneId() {
    	return zoneId;
    }

//-------------------------------------------------------------------------------------
    public String getLastUser() {
    	return lastUser;
    }

//-------------------------------------------------------------------------------------
    public String getLastServer() {
    	return lastServer;
    }

//-------------------------------------------------------------------------------------
    public String getLastUserAndServer(){
        return  lastUser + "@" + lastServer;
    }
    
//-------------------------------------------------------------------------------------
    public List<ServerConfiguration> getServerConfigurations(){
        return serverConfigurations;
    }
    
//-------------------------------------------------------------------------------------
    public ServerConfiguration getCurrentServerConfiguration(){
        return  currentServerConfiguration;
    }
    
//-------------------------------------------------------------------------------------
    public void setCurrentServerConfiguration(ServerConfiguration _serverConfiguration){
        currentServerConfiguration = _serverConfiguration;
    }

//-------------------------------------------------------------------------------------
    public void addServerConfiguration(ServerConfiguration _serverConfiguration) {
    	serverConfigurations.add(_serverConfiguration);
    }


//-------------------------------------------------------------------------------------
    public String getNextServer(String _alias) {
    	for (int i = 0; i < serverConfigurations.size(); i++) {
    		ServerConfiguration _serverConfiguration = serverConfigurations.get(i);
    		if(_serverConfiguration.getAlias().equals(_alias)){
    			return getNextServer(i);
    		}
    	}
    	return null;
//    	ServerConfiguration _serverConfiguration = new ServerConfiguration(debug, _user, _server);
//		debug.outln(Debug.INFO, "Adding ["+_serverConfiguration.getAlias()+"] -> ["+_serverConfiguration.getHost()+":"+_serverConfiguration.getPort()+"]...");
    }

//-------------------------------------------------------------------------------------
    private String getNextServer(int _lastIndex) {
    	if(_lastIndex < serverConfigurations.size() - 1){
    		_lastIndex++;
    	} else {
    		_lastIndex = 0;
    	}
    	ServerConfiguration _serverConfiguration = serverConfigurations.get(_lastIndex);
    	return _serverConfiguration.getAlias();
    }
    
//-------------------------------------------------------------------------------------
    public void setLastUserAndServer(String _user, String _server){
        lastUser = _user;
        lastServer = _server;
		for (int i = 0; i < serverConfigurations.size(); i++) {
    		ServerConfiguration _serverConfiguration = serverConfigurations.get(i);
//    		_serverConfiguration.dumpInfo();
    		if(_serverConfiguration.getAlias().equals(_server)){
    			setCurrentServerConfiguration(_serverConfiguration);
    			return;
    		}
    	}
        debug.outln(Debug.ERROR, "Cannot find any server configuration matching alias: " + _server);
//        refresh();
    }

//-------------------------------------------------------------------------------------
    public int getIndexOfServer(String _server) {
		for (int i = 0; i < serverConfigurations.size(); i++) {
    		ServerConfiguration _serverConfiguration = serverConfigurations.get(i);
    		if(_serverConfiguration.getAlias().equals(_server)){
    			return i;
    		}
    	}
    	return -1;
    }
    
//-------------------------------------------------------------------------------------
    public long getStartTime() {
        return currentServerConfiguration.getStartTime();
    }

//-------------------------------------------------------------------------------------
    public void setStartTime(long _startTime) {
        currentServerConfiguration.setStartTime(_startTime);
    }

//-------------------------------------------------------------------------------------
    public synchronized int getState() {
    	return state;
    }

//-------------------------------------------------------------------------------------
    public synchronized void setState(int _state) {
    	state = _state;
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isRunning() {
    	return (state == STATE_RUNNING);
    }

//-------------------------------------------------------------------------------------
    public void link() {
    	setLastUserAndServer(lastUser, lastServer);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load(Preferences _p) {
        setState(STATE_RUNNING);
        zoneId = _p.get("zoneId", "");
        lastUser = _p.get("lastUser", "");
        lastServer = _p.get("lastServer", "");
    }

//-------------------------------------------------------------------------------------
    public void save(Ini.Section _section) {
//        _section.put("version", version++);
        _section.put("lastUser", lastUser);
        _section.put("lastServer", lastServer);
        setState(STATE_STOPPED);
    }


//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
