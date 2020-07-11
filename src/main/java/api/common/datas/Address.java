//-------------------------------------------------------------------------------------
package codex.xbit.api.common.datas;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;
import codex.common.utils.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class Address extends AbstractData {
    private String address;
    private String owner;
    private String owner_type;

//-------------------------------------------------------------------------------------
    public Address() {
    }

//-------------------------------------------------------------------------------------
    public String getAddress() {
        return address;
    }

//-------------------------------------------------------------------------------------
    public String getOwner() {
        return owner;
    }

//-------------------------------------------------------------------------------------
    public String getOwnerType() {
        return owner_type;
    }

//-------------------------------------------------------------------------------------
    public boolean isExchange() {
        return owner_type.equals("exchange");
    }

//-------------------------------------------------------------------------------------
    public boolean isNotExchange() {
        return owner_type.equals("unknown") || owner_type.equals("other");
    }

//-------------------------------------------------------------------------------------
    public float matchAnyTriggerWords(Map<String, Float> _wordsMap) {
		Iterator<String> _keySetIterator = _wordsMap.keySet().iterator();
    	while (_keySetIterator.hasNext()){
    		String _word = _keySetIterator.next();
    		float _weight = _wordsMap.get(_word);
    		if((owner != null) && owner.contains(_word)){
    			return _weight;
    		} else if((owner_type != null) && owner_type.contains(_word)){
    			return _weight;
    		}
    	}
    	return (float)0.0;
//        return _words.contains(owner) || _words.contains(owner_type);
    }

//-------------------------------------------------------------------------------------
    public String toString() {
        return owner + "/" + owner_type;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
