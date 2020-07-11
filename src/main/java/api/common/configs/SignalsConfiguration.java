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
public class SignalsConfiguration extends AbstractConfiguration implements Serializable {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, Float> confidence;

//-------------------------------------------------------------------------------------
    public SignalsConfiguration() {
    }

//-------------------------------------------------------------------------------------
    public float getConfidence(String _name){
    	return confidence.get(_name);
    }

//-------------------------------------------------------------------------------------
    public SignalConfiguration get(String _name){
    	try{
    		return new SignalConfiguration(_name, getConfidence(_name));
    	} catch(NullPointerException _e){
    		System.out.println("Signals settings is null[" + _name + "]...\nExiting...");
    		System.exit(0);
    	}
    	return null;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
