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
public class TraderDeserializer extends StdDeserializer<TraderConfiguration> {

//-------------------------------------------------------------------------------------
	public TraderDeserializer(){
		this(null);
	}

//-------------------------------------------------------------------------------------
	public TraderDeserializer(Class<PilotConfiguration> _c){
		super(_c);
	}

//-------------------------------------------------------------------------------------
    @Override
    public TraderConfiguration deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
  
        JsonNode node = jp.getCodec().readTree(jp);

        TextNode _nodeMode = (TextNode) node.get("mode");
        String _mode = "demo";
        if(_nodeMode != null){
        	_mode = (String) _nodeMode.asText();
        }

        TextNode _nodeState = (TextNode) node.get("state");
        String _state = "normal";
        if(_nodeState != null){
        	_state = (String) _nodeState.asText();
        }

        IntNode _nodeMaxBarCount = (IntNode) node.get("maxBarCount");
        int _maxBarCount = 100;
        if(_nodeMaxBarCount != null){
        	_maxBarCount = (Integer) _nodeMaxBarCount.asInt();
        }
        
 		int _modeInt = TraderConfiguration.getModeAsInt(_mode);
 		int _stateInt = TraderConfiguration.getStateAsInt(_state);
        return new TraderConfiguration(_modeInt, _stateInt, _maxBarCount);
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
