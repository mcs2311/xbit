//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.rawtrades;
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
public class RawTrades extends AbstractMeta<Configuration, RawTradesByExchangeAndCurrencyPair> {

    //---rx:

//-------------------------------------------------------------------------------------
    public RawTrades(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized RawTradesByExchangeAndCurrencyPair getRawTradesByExchangeAndCurrencyPair(String _exchange, CurrencyPair _currencyPair) {
    	Key _key = new Key(_exchange, _currencyPair);
		RawTradesByExchangeAndCurrencyPair _rawTradesByExchangeAndCurrencyPair = getEntity(_key);
		if(_rawTradesByExchangeAndCurrencyPair == null){
			_rawTradesByExchangeAndCurrencyPair = new RawTradesByExchangeAndCurrencyPair(debug, resolver, _exchange, _currencyPair);
			addEntity(_key, _rawTradesByExchangeAndCurrencyPair);
		}
		return _rawTradesByExchangeAndCurrencyPair;
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
		return "RawTrades:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
