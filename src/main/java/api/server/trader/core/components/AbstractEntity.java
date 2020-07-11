//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components;
//-------------------------------------------------------------------------------------
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;

//-------------------------------------------------------------------------------------
public abstract class AbstractEntity<T extends Configuration> extends AbstractItem {
    protected T configuration;

//-------------------------------------------------------------------------------------
    public AbstractEntity(Debug _debug, Resolver _resolver){
    	this(_debug, _resolver, null, null);
    }

//-------------------------------------------------------------------------------------
    public AbstractEntity(Debug _debug, Resolver _resolver, String _name){
    	this(_debug, _resolver, _name, null);
    }

//-------------------------------------------------------------------------------------
    public AbstractEntity(Debug _debug, Resolver _resolver, T _configuration){
    	this(_debug, _resolver, null, _configuration);
    }

//-------------------------------------------------------------------------------------
    public AbstractEntity(Debug _debug, Resolver _resolver, String _name, T _configuration){
    	super(_debug, _resolver, _name);
//    	resolver = _resolver;
        configuration = _configuration;
        if(_configuration != null){
	        int _id = configuration.getId();
	        String _name1 = configuration.getName();
	        setId(_id);
//	        if(_name != null){
	        if(_name == null){
	    		setName(_name1);
	        }
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public T getConfiguration() {
    	return configuration;
    }

//-------------------------------------------------------------------------------------
    public boolean isEnabled(){
    	if(configuration == null){
    		return true;
    	}
    	return configuration.isEnabled();
	}

//-------------------------------------------------------------------------------------
    public void setEnabled(boolean _enabled){
    	if(configuration != null){
    		configuration.setEnabled(_enabled);
    	}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
//		debug.outln("AbstractEntity save...0");
    	super.save();
    	if(configuration != null){
    		configuration.save();
    	}
//		debug.outln("AbstractEntity save...1");
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
