//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;

import org.knowm.xchange.currency.*;
import com.fasterxml.jackson.annotation.*;

import codex.common.utils.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.common.loaders.*;



//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class AbstractConfiguration implements Configuration, Serializable {
    @JsonIgnore
    protected transient Debug debug;
    @JsonIgnore
    protected int id;
    @JsonIgnore
    protected String name;

	protected boolean enabled;

    @JsonIgnore
	protected transient ConfigurationLoader configurationLoader;
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public AbstractConfiguration(){
    	this(null, "");
    }

//-------------------------------------------------------------------------------------
    public AbstractConfiguration(String _name){
    	this(null, _name);
    }

//-------------------------------------------------------------------------------------
    public AbstractConfiguration(Debug _debug){
    	this(_debug, "");
    }

//-------------------------------------------------------------------------------------
    public AbstractConfiguration(Debug _debug, String _name){
    	debug = _debug;
    	name = _name;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public void initiate(Debug _debug, String _name){
		initiate(_debug, _name, null);
	}

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public void initiate(Debug _debug, String _name, ConfigurationLoader _configurationLoader){
    	debug = _debug;
		setName(_name);
		int _id = -1;
		try{
			_id = Integer.parseInt(_name);
		}catch(Exception _e){}
		setId(_id);
		configurationLoader = _configurationLoader;
//		debug.outln("Configuration..._name="+_name+", _id="+_id);
	}


//-------------------------------------------------------------------------------------
    public void setDebug(Debug _debug) {
        debug = _debug;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    @JsonIgnore
	public int getId(){
		return id;
	}

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public void setId(int _id){
		id = _id;
	}

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public String getName(){
		return name;
	}

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public void setName(String _name){
		name = _name;
	}

//-------------------------------------------------------------------------------------
	public boolean isEnabled(){
		return enabled;
	}

//-------------------------------------------------------------------------------------
	public void setEnabled(boolean _enabled){
		enabled = _enabled;
	}

//-------------------------------------------------------------------------------------
	public String getDescriptor(){
		return getName();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void load(){
		//configurationLoader.load();???
	}

//-------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
	public void save(){
		configurationLoader.save(this);
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
