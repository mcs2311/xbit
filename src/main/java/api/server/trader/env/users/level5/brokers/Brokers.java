//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level5.brokers;
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

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;

//-------------------------------------------------------------------------------------
public class Brokers extends AbstractCluster<Configuration, Broker> {
//    private List<Broker> brokers;
    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public Brokers(Debug _debug, Resolver _resolver) {
        super(_debug, _resolver, "brokers");
    }

//-------------------------------------------------------------------------------------
    public synchronized void addBrokers(Debug _debug, TacticConfiguration _tacticConfiguration, CurrencyPair _currencyPair) {
        int _numberOfBrokers = _tacticConfiguration.getNumberOfBrokers();
	    List<String> _exchanges = _tacticConfiguration.getExchanges();
//		_debug.outln(Debug.INFO, "_numberOfBrokers=["+_numberOfBrokers+"]...");
//		_debug.outln(Debug.INFO, "_exchanges=["+_exchanges.size()+"]...");
        for (int i = 0; i < _numberOfBrokers; i++) {
        	for (int j = 0; j < _exchanges.size(); j++) {
//				_debug.outln(Debug.INFO, ".......!!!!!!-----------------i="+i+",j="+j);
        		String _exchange = _exchanges.get(j);
        		String _name0 = _exchange + "-BUY-B" + i;
	    		Debug _debug0 = new Debug(_debug, _name0, Debug.IMPORTANT3);
        		Broker _brokerBuy = new Broker(_debug0, resolver, _tacticConfiguration, _currencyPair, _exchange, OrderX.TYPE_BUY);
        		_brokerBuy.setName(_name0);
//        		brokers.add(_brokerBuy);
				Key _key0 = new Key(_tacticConfiguration, _currencyPair, _exchange, OrderX.TYPE_BUY);        	
	        	addEntity(_key0, _brokerBuy);

        		String _name1 = _exchange + "-SELL-B" + i;
	    		Debug _debug1 = new Debug(_debug, _name1, Debug.IMPORTANT3);
        		Broker _brokerSell = new Broker(_debug1, resolver, _tacticConfiguration, _currencyPair, _exchange, OrderX.TYPE_SELL);
        		_brokerSell.setName(_name1);
//        		brokers.add(_brokerSell);
				Key _key1 = new Key(_tacticConfiguration, _currencyPair, _exchange, OrderX.TYPE_SELL);        	
	        	addEntity(_key1, _brokerSell);
	        }
        }
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
		return "Brokers:" ;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
