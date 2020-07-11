//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.raworders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.common.loaders.orders.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;

//-------------------------------------------------------------------------------------
public class RawOrders extends AbstractMeta<Configuration, RawOrdersByExchangeAndCurrencyPair> {

    //---rx:

//-------------------------------------------------------------------------------------
    public RawOrders(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized RawOrdersByExchangeAndCurrencyPair getRawOrdersByExchangeAndCurrencyPair(String _exchange, CurrencyPair _currencyPair) {
    	Key _key = new Key(_exchange, _currencyPair);
		RawOrdersByExchangeAndCurrencyPair _rawOrdersByExchangeAndCurrencyPair = getEntity(_key);
		if(_rawOrdersByExchangeAndCurrencyPair == null){
			_rawOrdersByExchangeAndCurrencyPair = new RawOrdersByExchangeAndCurrencyPair(debug, resolver, _exchange, _currencyPair);
			addEntity(_key, _rawOrdersByExchangeAndCurrencyPair);
		}
		return _rawOrdersByExchangeAndCurrencyPair;
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
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "RawOrders:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
