//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;

import org.jline.utils.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

import codex.common.utils.*;
import codex.xbit.api.common.loaders.serializers.*;
import codex.xbit.api.common.loaders.deserializers.*;


//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
@JsonSerialize(using = PilotSerializer.class)
@JsonDeserialize(using = PilotDeserializer.class)
public class PilotConfiguration extends AbstractConfiguration {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private int mode;

//    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private String type;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, Map<String, Double>> probabilities;

    @JsonIgnore
    private String descriptor;

    public static final int MODE_NONE 		= 0;
    public static final int MODE_AUTO 		= 1;
    public static final int MODE_MANUAL 	= 2;

//-------------------------------------------------------------------------------------
    public PilotConfiguration(Debug _debug) {
    	super(_debug);
    }

//-------------------------------------------------------------------------------------
    public PilotConfiguration(int _mode, String _type, Map<String, Map<String, Double>> _probabilities) {
//        debug = _debug;
    	enabled = true;
        mode = _mode;
        type = _type;
        probabilities = _probabilities;
        setDescriptor();
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public int getMode(){
        return mode;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public String getModeAsString(){
        return getModeAsString(mode);
    }

//-------------------------------------------------------------------------------------
    public static String getModeAsString(int _mode){
        switch(_mode){
            case MODE_NONE	: return " -- ";
            case MODE_AUTO	: return "AUTO";
            case MODE_MANUAL: return "MANUAL";
            default: return "ERROR";
        }
    }

//-------------------------------------------------------------------------------------
    public static int getModeAsInt(String _mode){
        switch(_mode.toUpperCase()){
            case "AUTO"		: return MODE_AUTO;
            case "MANUAL"	: return MODE_MANUAL;
            default 		: return -1;
        }
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setMode(int _mode){
    	setMode(_mode, "");
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setMode(int _mode, String _text){
        mode = _mode;
        int _prefixColorLevel = getModeColor(_mode);
        debug.out(Debug.IMPORTANT1, "Switching pilot to [");
        debug.out(_prefixColorLevel, getModeAsString(), false);
        debug.outln(Debug.IMPORTANT1, "] mode "+_text+" ...", false);
//        debug.setPrefix0(_prefixColorLevel, getModeAsString());
    	setDescriptor();
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setMode(String _mode){
        setMode(getModeAsInt(_mode));
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public String getType(){
        return type;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setType(String _type){
        type = _type;
        int _prefixColorLevel = getModeColor(mode);
        debug.out(Debug.IMPORTANT1, "Switching pilot to [");
        debug.out(_prefixColorLevel, _type, false);
        debug.outln(Debug.IMPORTANT1, "] type ...", false);    	
    	setDescriptor();
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public Map<String, Map<String, Double>> getProbabilities(){
    	return probabilities;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public int getModeColor(){
    	return getModeColor(mode);
    }
//-------------------------------------------------------------------------------------
    public static int getModeColor(int _mode){
        switch(_mode){
            case MODE_AUTO		: return Debug.IMPORTANT4;
            case MODE_MANUAL	: return Debug.IMPORTANT1;
            default 			: return Debug.ERROR;
        }
    }

//-------------------------------------------------------------------------------------
    public int getModeColorAsAttributedStyle(){
    	return getModeColorAsAttributedStyle(mode);
    }

//-------------------------------------------------------------------------------------
    public static int getModeColorAsAttributedStyle(int _mode){
        switch(_mode){
            case MODE_AUTO		: return AttributedStyle.CYAN;
            case MODE_MANUAL	: return AttributedStyle.YELLOW;
            default 			: return AttributedStyle.RED;
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String getDescriptor(){
    	return descriptor;
    }

//-------------------------------------------------------------------------------------
    private void setDescriptor(){
//    	String _descriptor;
        switch(mode){
            case MODE_NONE	: {
            	descriptor = "-"; 
            	break;
            }
            case MODE_AUTO	: {
//            	descriptor = "A" + "-" + type.substring(0, 3);
            	switch(type){
            		case "safe" : {
            			descriptor = "SAF";
            			break;
            		}
            		case "balanced" : {
            			descriptor = "BAL";
            			break;
            		}
            		case "risky" : {
            			descriptor = "RSK";
            			break;
            		}
            		default : {
            			descriptor = "-";
            			break;
            		}
            	}
            	break;
            }
            case MODE_MANUAL: {
            	descriptor = "M";
            	break;
            }
            default: {
            	descriptor = "ERROR";
            }
        }
        descriptor = descriptor.toUpperCase();
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
