//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


//import codex.xbit.api.server.trader.common.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public class Key {
   public Object[] keys;

//-------------------------------------------------------------------------------------
   public Key(Object _key1) {
		keys = new Object[1];
		keys[0] = _key1;
   }

//-------------------------------------------------------------------------------------
   public Key(Object _key1, Object _key2) {
		keys = new Object[2];
		keys[0] = _key1;
		keys[1] = _key2;
   }

//-------------------------------------------------------------------------------------
   public Key(Object _key1, Object _key2, Object _key3) {
		keys = new Object[3];
		keys[0] = _key1;
		keys[1] = _key2;
		keys[2] = _key3;
   }

//-------------------------------------------------------------------------------------
   public Key(Object _key1, Object _key2, Object _key3, Object _key4) {
		keys = new Object[4];
		keys[0] = _key1;
		keys[1] = _key2;
		keys[2] = _key3;
		keys[3] = _key4;
   }

//-------------------------------------------------------------------------------------
   @Override   
   public boolean equals(Object _obj) {
//		System.out.println("Key search-------");
		if (!(_obj instanceof Key)){
			return false;
		}
    	Key _ref = (Key) _obj;
    	for (int i = 0; i < _ref.keys.length; i++) {
//			System.out.println("_ref.keys[" + i + "]=[" + _ref.keys[i] + "], _keys[" + i + "]=[" + keys[i] + "]");
    		if(_ref.keys[i] != null){
	    		if(!_ref.keys[i].equals(keys[i])){
	    			return false;
	    		}
    		}
    	}
    	return true;
   }

//-------------------------------------------------------------------------------------
    @Override
    public int hashCode() {
    	int _hashcode = 0;
    	for (int i = 0; i < keys.length; i++) {
//			System.out.println("Key["+i+"]:= "+keys[i]);
    		if(keys[i] != null){
    			_hashcode = _hashcode ^ keys[i].hashCode();
    		}
    	}
    	return _hashcode;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
