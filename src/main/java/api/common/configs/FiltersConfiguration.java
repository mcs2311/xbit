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
public class FiltersConfiguration extends AbstractConfiguration implements Serializable {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, Map<String, Float>> repositories;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, Map<String, Float>> signals;

//-------------------------------------------------------------------------------------
    public FiltersConfiguration() {
    }

//-------------------------------------------------------------------------------------
    public Map<String, Map<String, Float>> getRespositories(){
    	return repositories;
    }

//-------------------------------------------------------------------------------------
    public Map<String, Map<String, Float>> getSignals(){
    	return signals;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------