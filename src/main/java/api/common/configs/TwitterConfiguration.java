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
public class TwitterConfiguration extends AbstractConfiguration implements Serializable {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, String> keys;

//-------------------------------------------------------------------------------------
    public TwitterConfiguration() {
    }

//-------------------------------------------------------------------------------------
    public String getApiKey(){
    	return keys.get("API_KEY");
    }

//-------------------------------------------------------------------------------------
    public String getApiKeySecret(){
    	return keys.get("API_KEY_SECRET");
    }

//-------------------------------------------------------------------------------------
    public String getAccessToken(){
    	return keys.get("ACCESS_TOKEN");
    }

//-------------------------------------------------------------------------------------
    public String getAccessTokenSecret(){
    	return keys.get("ACCESS_TOKEN_SECRET");
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
