//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;

//import org.ini4j.*;
import com.fasterxml.jackson.annotation.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class WhaleAlertConfiguration extends AbstractConfiguration implements Serializable {
    private String server;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, String> keys;
    private double rateLimit;
    private int timeWindow;
    private long triggerSize;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, Map<String, Float>> triggerWords;

    private long triggerWordsSize;

//-------------------------------------------------------------------------------------
    public WhaleAlertConfiguration() {
    }

//-------------------------------------------------------------------------------------
    public String getServer(){
    	return server;
    }

//-------------------------------------------------------------------------------------
    public String getKey(String _alias){
    	return keys.get(_alias);
    }

//-------------------------------------------------------------------------------------
    public double getRateLimit(){
    	return rateLimit;
    }

//-------------------------------------------------------------------------------------
    public int getTimeWindow(){
    	return timeWindow;
    }

//-------------------------------------------------------------------------------------
    public long getTriggerSize(){
    	return triggerSize;
    }
/*
//-------------------------------------------------------------------------------------
    public Map<String, List<String>> getTriggerWords(){
    	return triggerWords;
    }
*/
//-------------------------------------------------------------------------------------
    public Map<String, Float> getTriggerWords(String _trigger){
    	return triggerWords.get(_trigger);
    }

//-------------------------------------------------------------------------------------
    public long getTriggerWordsSize(){
    	return triggerWordsSize;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void load(Preferences _p) {
        server = trim(_p.get("server", ""));
        key = trim(_p.get("key", ""));
    	rateLimit = _p.getDouble("rateLimit", 0.0);
      	timeWindow = _p.getInt("timeWindow", 0);
      	triggerSize = _p.getLong("triggerSize", 0L);
    }

//-------------------------------------------------------------------------------------
    public void save(Ini.Section _section) {
    }

*/
//-------------------------------------------------------------------------------------
    public void dumpInfo(){
//        debug.outln("Server: \talias: "+alias+"\thost: "+host+"\tport: "+port+"\tmaxNumberOfThreads: "+maxNumberOfThreads);
    }

//-------------------------------------------------------------------------------------
    private String trim(String _s){
    	return _s.replaceAll("\"", "");
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
