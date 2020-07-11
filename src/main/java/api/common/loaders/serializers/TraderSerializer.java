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
public class TraderSerializer extends StdSerializer<TraderConfiguration> {

//-------------------------------------------------------------------------------------
	public TraderSerializer(){
		this(null);
	}

//-------------------------------------------------------------------------------------
	public TraderSerializer(Class<TraderConfiguration> _c){
		super(_c);
	}

//-------------------------------------------------------------------------------------
    @Override
    public void serialize(
      TraderConfiguration _value, JsonGenerator jgen, SerializerProvider provider) 
      throws IOException, JsonProcessingException {
  
        jgen.writeStartObject();
        jgen.writeStringField("mode", _value.getModeAsString());
        jgen.writeStringField("state", _value.getStateAsString());
        jgen.writeNumberField("maxBarCount", _value.getMaxBarCount());
        jgen.writeEndObject();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
