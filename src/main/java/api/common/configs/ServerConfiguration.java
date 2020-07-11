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
public class ServerConfiguration implements Serializable {
    private transient Debug debug;
    
    private String alias;
    private String host;
    private int port;
    private int maxNumberOfThreads;
    private String zoneId;
    private String proxyHost;
    private String proxyPort;
    private String pathToSingle;
    private String pathToUsers;


    private long startTime;


    public static final String DEFAULT_ALIAS = "local";
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 40000;
    public static final int DEFAULT_MAX_NUMBER_OF_THREADS = 4;

    public static final String DEFAULT_PATH_TO_SINGLE = "/.xbit/data/single";
    public static final String DEFAULT_PATH_TO_USERS = "/.xbit/data/users";

//-------------------------------------------------------------------------------------
    public ServerConfiguration(Debug _debug) {
        debug = _debug;
    }

//-------------------------------------------------------------------------------------
    public ServerConfiguration(Debug _debug, String _alias) {
        debug = _debug;
        alias = _alias;
    }

//-------------------------------------------------------------------------------------
    public void setDebug(Debug _debug) {
        debug = _debug;
    }

//-------------------------------------------------------------------------------------
    public String getAlias() {
        return alias;
    }

//-------------------------------------------------------------------------------------
    public String getHost() {
        return host;
    }

//-------------------------------------------------------------------------------------
    public int getPort() {
        return port;
    }

//-------------------------------------------------------------------------------------
    public void setPort(int _port) {
        port = _port;
    }

//-------------------------------------------------------------------------------------
    public int getMaxNumberOfThreads() {
        return maxNumberOfThreads;
    }

//-------------------------------------------------------------------------------------
    public String getZoneId() {
        return zoneId;
    }

//-------------------------------------------------------------------------------------
    public void setMaxNumberOfThreads(int _maxNumberOfThreads) {
        maxNumberOfThreads = _maxNumberOfThreads;
    }

//-------------------------------------------------------------------------------------
    public String getProxyHost() {
        return proxyHost;
    }

//-------------------------------------------------------------------------------------
    public String getProxyPort() {
        return proxyPort;
    }

//-------------------------------------------------------------------------------------
    public String getPathToSingle() {
        return pathToSingle;
    }

//-------------------------------------------------------------------------------------
    public String getPathToUsers() {
        return pathToUsers;
    }

//-------------------------------------------------------------------------------------
    public String getPathToExchanges() {
        return pathToSingle + "/exchanges";
    }

//-------------------------------------------------------------------------------------
    public String getPathToRepositories() {
        return pathToSingle + "/repositories";
    }

//-------------------------------------------------------------------------------------
    public String getPathToSettings() {
        return pathToSingle + "/settings";
    }

//-------------------------------------------------------------------------------------
    public long getStartTime() {
        return startTime;
    }

//-------------------------------------------------------------------------------------
    public void setStartTime(long _startTime) {
        startTime = _startTime;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load(Preferences _p) {
        alias = _p.get("alias", DEFAULT_ALIAS);
        host = _p.get("host", DEFAULT_HOST);
        port = _p.getInt("port", DEFAULT_PORT);
        maxNumberOfThreads = _p.getInt("maxNumberOfThreads", DEFAULT_MAX_NUMBER_OF_THREADS);
        zoneId = trim(_p.get("zoneId", ""));
        proxyHost = trim(_p.get("proxyHost", ""));
        proxyPort = trim(_p.get("proxyPort", ""));

        pathToSingle = trim(_p.get("pathToSingle", DEFAULT_PATH_TO_SINGLE));
        pathToUsers = trim(_p.get("pathToUsers", DEFAULT_PATH_TO_USERS));
    }

//-------------------------------------------------------------------------------------
    public void save(Ini.Section _section) {
        _section.put("alias", alias);
        if(!host.equals(DEFAULT_HOST)){
            _section.put("host", host);
        }
        if(port != DEFAULT_PORT){
            _section.put("port", port);
        }
        if(maxNumberOfThreads != DEFAULT_MAX_NUMBER_OF_THREADS){
            _section.put("maxNumberOfThreads", maxNumberOfThreads);
        }
    }

//-------------------------------------------------------------------------------------
    public String toString() {
        return "Server: \talias: "+alias+"\thost: "+host+"\tport: "+port+"\tmaxNumberOfThreads: "+maxNumberOfThreads;
    }

//-------------------------------------------------------------------------------------
    public void dumpInfo(){
        debug.outln(toString());
    }

//-------------------------------------------------------------------------------------
    private String trim(String _s){
    	return _s.replaceAll("\"", "");
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
