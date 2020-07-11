//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components;
//-------------------------------------------------------------------------------------
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.Collectors; 
import java.util.stream.StreamSupport; 

import io.reactivex.*;
import io.reactivex.disposables.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.common.aspects.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("rawtypes")
public abstract class AbstractMeta<T1 extends Configuration, T2 extends AbstractCluster> extends AbstractItem {

    protected ConfigurationLoader<T1> configurationLoader;
    protected Map<Key, T2> entities;

    //---cache:
    private Iterator<Key> keys;
//    private Iterator<T2> values;
    private Collection<T2> values;
//    Collection<V>
    
//-------------------------------------------------------------------------------------
    public AbstractMeta(Debug _debug, Resolver _resolver){
    	this(_debug, _resolver, "");
    }

//-------------------------------------------------------------------------------------
    public AbstractMeta(Debug _debug, Resolver _resolver, String _clusterName){
    	super(_debug, _resolver, _clusterName);
//    	configurationLoader = _configurationLoader;
//    	resolver = _resolver;
        entities = new HashMap<Key, T2>();
        keys = entities.keySet().iterator();
        values = entities.values();//.iterator();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<T2> getEntities() {
		// Convert iterator to iterable 
//        Iterable<T2> _iterable = () -> values; 
  
        // Create a List from the Iterable 
        List<T2> _list = StreamSupport 
                           .stream(values.spliterator(), false) 
                           .collect(Collectors.toList()); 
  
        // Return the List 
        return _list; 
	}

//-------------------------------------------------------------------------------------
    public boolean isEnabled(){
    	return true;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	protected T2 getEntity(Key _key){
		return entities.get(_key);		
	}
	
//-------------------------------------------------------------------------------------
	protected void addEntity(Key _key, T2 _entity){
		entities.put(_key, _entity);		
	}
	
//-------------------------------------------------------------------------------------
	protected void removeEntity(Key _key){
		entities.remove(_key);		
	}
	
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
/*    	final AtomicInteger _indexHolder = new AtomicInteger();
//    	loadCustomEntities();
    	if(configurationLoader != null){
    		displayInfo(Debug.IMPORTANT3, "Loading " + clusterName+ "... ", true, false);
	    	List<T1> _configurations = configurationLoader.load();
	    	if(_configurations != null){
		    	_configurations.forEach(_configuration -> {
		    		final int _counter = _indexHolder.getAndIncrement();
		    		if(_counter != 0){
            			displayInfo(Debug.IMPORTANT3, ", ", false, false);
		    		}
//            		debug.out(Debug.IMPORTANT3, "[ ", false);
		            if(_configuration.isEnabled()){
		                displayInfo(Debug.IMPORTANT1, _configuration.getDescriptor(), false, false);
		            } else {
		                displayInfo(Debug.ERROR, _configuration.getDescriptor(), false, false);       
		            }
//		           	debug.out(Debug.IMPORTANT3, " ]", false);
		            loadEntity(_configuration);
		    	});
	    	}
			displayInfo(Debug.IMPORTANT3, "", false, true);
	    }*/
	    Iterator<T2> _iterator = values.iterator();
        while(_iterator.hasNext()){
            T2 _entity = _iterator.next();
            if(_entity.isEnabled()){
            	_entity.load();
            }
        }
    }

//-------------------------------------------------------------------------------------
    protected void displayInfo(int _color, String _text, boolean _displayPrefix, boolean _newLine) {
    	if(name != null){
    		if(_newLine){
				debug.outln(_color, _text, _displayPrefix);
    		} else {
				debug.out(_color, _text, _displayPrefix);
    		}
    	}
    }


//-------------------------------------------------------------------------------------
    public void save() {
	    Iterator<T2> _iterator = values.iterator();
        while(_iterator.hasNext()){
            T2 _entity = _iterator.next();
            if(_entity.isEnabled()){
            	_entity.save();
            }
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
