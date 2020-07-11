//-------------------------------------------------------------------------------------
package codex.xbit.api.common.loaders.serializers;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


//-------------------------------------------------------------------------------------
public class PilotSerializer extends StdSerializer<PilotConfiguration> {

//-------------------------------------------------------------------------------------
	public PilotSerializer(){
		this(null);
	}

//-------------------------------------------------------------------------------------
	public PilotSerializer(Class<PilotConfiguration> _c){
		super(_c);
	}

//-------------------------------------------------------------------------------------
    @Override
    public void serialize(
      PilotConfiguration _value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
  
        jgen.writeStartObject();
        jgen.writeStringField("mode", _value.getModeAsString());
        jgen.writeStringField("type", _value.getType());
        jgen.writeObjectField("probabilities", _value.getProbabilities());
        jgen.writeEndObject();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
