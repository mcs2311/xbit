//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level6.pairs;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level5.brokers.*;


//-------------------------------------------------------------------------------------
public class Pair extends AbstractEntity<Configuration> {
	private TacticConfiguration tacticConfiguration;
    private CurrencyPair currencyPair;

    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public Pair(Debug _debug, Resolver _resolver, TacticConfiguration _tacticConfiguration, CurrencyPair _currencyPair) {
        super(_debug, _resolver, null, null);
	    debug = new Debug(_debug, _currencyPair.toString(), Debug.IMPORTANT1);
        tacticConfiguration = _tacticConfiguration;
		currencyPair = _currencyPair;		
        load();
    }

//-------------------------------------------------------------------------------------
    public CurrencyPair getCurrencyPair() {
    	return currencyPair;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//		debug.outln(Debug.INFO, "Pair["+currencyPair+"].load...");
    	super.load();
        Brokers _brokers = resolver.getBrokers();
        _brokers.addBrokers(debug, tacticConfiguration, currencyPair);
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Pair:" + 
		currencyPair;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
