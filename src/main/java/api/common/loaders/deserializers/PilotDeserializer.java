//-------------------------------------------------------------------------------------
package codex.xbit.api.common.loaders.deserializers;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.deser.std.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


//-------------------------------------------------------------------------------------
public class PilotDeserializer extends StdDeserializer<PilotConfiguration> {

//-------------------------------------------------------------------------------------
	public PilotDeserializer(){
		this(null);
	}

//-------------------------------------------------------------------------------------
	public PilotDeserializer(Class<PilotConfiguration> _c){
		super(_c);
	}

//-------------------------------------------------------------------------------------
    @Override
    public PilotConfiguration deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
  
        JsonNode node = jp.getCodec().readTree(jp);

        TextNode _nodeMode = (TextNode) node.get("mode");
        String _mode = "auto";
        if(_nodeMode != null){
        	_mode = (String) _nodeMode.asText();
        }
		
		TextNode _nodeType = (TextNode) node.get("type");
        String _type = "";
        if(_nodeType != null){
        	_type = (String) _nodeType.asText();
        }

        @SuppressWarnings("unchecked")
    	Map<String, Map<String, Double>> _probabilities = node.get("probabilities").traverse(jp.getCodec()).readValueAs(HashMap.class);

        
 		int _modeInt = PilotConfiguration.getModeAsInt(_mode);
        return new PilotConfiguration(_modeInt, _type, _probabilities);
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
