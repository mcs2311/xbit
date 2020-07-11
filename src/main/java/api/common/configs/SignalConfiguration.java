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
public class SignalConfiguration extends AbstractConfiguration implements Serializable {
    private float confidence;

//-------------------------------------------------------------------------------------
    public SignalConfiguration(String _name, float _confidence) {
    	super(_name);
    	confidence = _confidence;
    }

//-------------------------------------------------------------------------------------
    public float getConfidence(){
    	return confidence;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
