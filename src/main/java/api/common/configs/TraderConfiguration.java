//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;

//import org.ini4j.*;
import org.jline.utils.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

import codex.common.utils.*;

import codex.xbit.api.common.loaders.serializers.*;
import codex.xbit.api.common.loaders.deserializers.*;


//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
@JsonSerialize(using = TraderSerializer.class)
@JsonDeserialize(using = TraderDeserializer.class)
public class TraderConfiguration extends AbstractConfiguration {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private int mode;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private int state;

    private int maxBarCount;

    public static final int MODE_NONE = 0;
    public static final int MODE_DEMO = 1;
    public static final int MODE_LIVE = 2;

    public static final int STATE_INIT = 100;
    public static final int STATE_NORMAL = 101;
    public static final int STATE_PAUSED = 102;
    public static final int STATE_PANIC = 103;
    public static final int STATE_BEAR = 104;
    public static final int STATE_BULL = 105;

    public static final String DEFAULT_MODE = "DEMO";
    public static final String DEFAULT_STATE = "NORMAL";

    public static final int MAX_BAR_COUNT = 10000;
/*
//-------------------------------------------------------------------------------------
    public TraderConfiguration(Debug _debug) {
    	this(_debug, MODE_NONE, STATE_INIT, 0);
    }
*/
//-------------------------------------------------------------------------------------
    public TraderConfiguration(Debug _debug) {
    	super(_debug);
    }

//-------------------------------------------------------------------------------------
    public TraderConfiguration(int _mode, int _state, int _maxBarCount) {
    	enabled = true;
        mode = _mode;
        state = _state;
        maxBarCount = _maxBarCount;
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
            case MODE_LIVE	: return "LIVE";
            case MODE_DEMO	: return "DEMO";
            default 		: return " -- ";
        }
    }

//-------------------------------------------------------------------------------------
    public static int getModeAsInt(String _mode){
        switch(_mode){
            case " -- "		: return MODE_NONE;
            case "LIVE"		: return MODE_LIVE;
            case "DEMO"		: return MODE_DEMO;
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
        debug.out(Debug.IMPORTANT1, "Switching trader to the [");
        debug.out(_prefixColorLevel, getModeAsString(), false);
        debug.outln(Debug.IMPORTANT1, "] mode " + _text + " ...", false);
//        debug.setPrefix0(_prefixColorLevel, getModeAsString());
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setMode(String _mode){
        setMode(getModeAsInt(_mode));
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public int getState(){
        return state;
    }

//-------------------------------------------------------------------------------------
    public boolean isNotBearish(){
        return (state != STATE_INIT) && 
        	(state != STATE_PAUSED) && 
        	(state != STATE_PANIC) && 
        	(state != STATE_BEAR);
    }

//-------------------------------------------------------------------------------------
    public boolean isNotBullish(){
        return (state != STATE_INIT) && 
        	(state != STATE_PAUSED) && 
//        	(state != STATE_PANIC) && 
        	(state != STATE_BULL);
    }

//-------------------------------------------------------------------------------------
    public boolean isBearish(){
        return (state == STATE_BEAR) ||
        	(state == STATE_PANIC);
    }

//-------------------------------------------------------------------------------------
    public boolean isBullish(){
        return (state == STATE_BULL);
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public String getStateAsString(){
        return getStateAsString(state);
    }

//-------------------------------------------------------------------------------------
    public static String getStateAsString(int _state){
        switch(_state){
            case STATE_INIT 		: return " -- ";
            case STATE_NORMAL 		: return "NORMAL";
            case STATE_PAUSED 		: return "PAUSED";
            case STATE_PANIC 		: return "PANIC";
            case STATE_BEAR 		: return "BEAR";
            case STATE_BULL 		: return "BULL";
            default 				: return " -- ";
        }
    }

//-------------------------------------------------------------------------------------
    public static int getStateAsInt(String _state){
        switch(_state.toUpperCase()){
            case " -- " 		: return STATE_INIT;
            case "NORMAL" 		: return STATE_NORMAL;
            case "PAUSED" 		: return STATE_PAUSED;
            case "PANIC" 		: return STATE_PANIC;
            case "BEAR" 		: return STATE_BEAR;
            case "BULL" 		: return STATE_BULL;
            default 			: return -1;
        }
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setState(int _state){
    	setState(_state, "");
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setState(int _state, String _text){
        state = _state;
        int _prefixColorLevel = getStateColor(_state);
        debug.out(Debug.IMPORTANT1, "Switching trader to the [");
        debug.out(_prefixColorLevel, getStateAsString(), false);
        debug.outln(Debug.IMPORTANT1, "] state "+_text+" ...", false);
//        debug.setPrefix1(_prefixColorLevel, getStateAsString());
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setState(String _state){
        setState(getStateAsInt(_state));
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
            case MODE_NONE: return Debug.ERROR;
            case MODE_DEMO: return Debug.INFO;
            case MODE_LIVE: return Debug.ERROR;
            default: return Debug.ERROR;
        }
    }

//-------------------------------------------------------------------------------------
    public static int getModeColorAsAttributedStyle(int _mode){
        switch(_mode){
            case MODE_NONE: return AttributedStyle.RED;
            case MODE_DEMO: return AttributedStyle.GREEN;
            case MODE_LIVE: return AttributedStyle.RED;
            default: return AttributedStyle.RED;
        }
    }

//-------------------------------------------------------------------------------------
    public int getStateColor(){
    	return getStateColor(state);
    }

//-------------------------------------------------------------------------------------
    public static int getStateColor(int _state){
        switch(_state){
            case STATE_INIT: return Debug.ERROR;
            case STATE_NORMAL: return Debug.INFO;
            case STATE_PAUSED: return Debug.IMPORTANT2;
            case STATE_PANIC: return Debug.ERROR;
            case STATE_BEAR: return Debug.WARNING;
            case STATE_BULL: return Debug.IMPORTANT4;
            default: return Debug.ERROR;
        }
    }

//-------------------------------------------------------------------------------------
    public static int getStateColorAsAttributedStyle(int _state){
        switch(_state){
            case STATE_INIT: return AttributedStyle.RED;
            case STATE_NORMAL: return AttributedStyle.GREEN;
            case STATE_PAUSED: return AttributedStyle.BLUE;
            case STATE_PANIC: return AttributedStyle.RED;
            case STATE_BEAR: return AttributedStyle.YELLOW;//MAGENTA;
            case STATE_BULL: return AttributedStyle.CYAN;
            default: return AttributedStyle.RED;
        }
    }


//-------------------------------------------------------------------------------------
    public int getMaxBarCount() {
        return maxBarCount;
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void load(Preferences _p) {
        String _mode = _p.get("mode", DEFAULT_MODE);
        String _state = _p.get("state", DEFAULT_STATE);
    	maxBarCount = _p.getInt("maxBarCount", MAX_BAR_COUNT);
        setMode(_mode);
        setState(_state);
    }

//-------------------------------------------------------------------------------------
    public void save(Ini.Section _section) {
        _section.put("mode", getModeAsString());
        _section.put("state", getStateAsString());
    }
*/

//-------------------------------------------------------------------------------------
    public void dumpInfo(){
//        debug.outln("Server: \talias: "+alias+"\thost: "+host+"\tport: "+port+"\tmaxNumberOfThreads: "+maxNumberOfThreads);
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
