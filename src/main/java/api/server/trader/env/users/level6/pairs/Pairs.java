//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level6.pairs;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.Order;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;




//-------------------------------------------------------------------------------------
public class Pairs extends AbstractCluster<Configuration, Pair> {
//    private List<Pair> pairs;
    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public Pairs(Debug _debug, Resolver _resolver) {
        super(_debug, _resolver, "pairs");
//        pairs = new ArrayList<Pair>();
    }

//-------------------------------------------------------------------------------------
    public synchronized void addPairs(Debug _debug, TacticConfiguration _tacticConfiguration) {
    	List<CurrencyPair> _currencyPairs = _tacticConfiguration.getPairs();

        for (int i = 0; i < _currencyPairs.size(); i++) {
        	CurrencyPair _currencyPair = _currencyPairs.get(i);
        	Pair _pair = new Pair(_debug, resolver, _tacticConfiguration, _currencyPair);
//        	pairs.add(_pair);
			Key _key = new Key(_tacticConfiguration, _currencyPair);        	
        	addEntity(_key, _pair);
        }
    }

//-------------------------------------------------------------------------------------
    public synchronized List<CurrencyPair> getPairs() {
    	List<Pair> _pairs = getEntities();
    	List<CurrencyPair> _currencyPairs = new ArrayList<CurrencyPair>();
        for (int i = 0; i < _pairs.size(); i++) {
        	CurrencyPair _currencyPair = _pairs.get(i).getCurrencyPair();
        	_currencyPairs.add(_currencyPair);
        }
        return _currencyPairs;
    }

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter){
		return null;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Pairs:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
