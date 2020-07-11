//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level4.marketmodels;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;
//import java.lang.reflect.*;

import org.knowm.xchange.dto.Order;
import org.knowm.xchange.currency.CurrencyPair;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
//import codex.xbit.api.server.trader.common.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level0.influxexchanges.*;

//-------------------------------------------------------------------------------------
public class MarketModels extends AbstractCluster<Configuration, MarketModel> {

    //---cache:
//    private List<InfluxExchange> exchanges;

//-------------------------------------------------------------------------------------
    public MarketModels(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized MarketModel getMarketModel(CurrencyPair _currencyPair) {
    	Key _key = new Key(_currencyPair);
    	MarketModel _marketModel = getEntity(_key);
    	if(_marketModel == null){
	    	_marketModel = new MarketModel(debug, resolver, _currencyPair);
//	    	_marketModel.load();
			addEntity(_key, _marketModel);
    	}
    	return _marketModel;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
