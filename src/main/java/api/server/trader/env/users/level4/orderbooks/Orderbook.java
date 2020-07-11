//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
//import java.util.*;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import codex.common.utils.*;

import org.knowm.xchange.currency.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.*;


//-------------------------------------------------------------------------------------
public class Orderbook extends AbstractEntity<Configuration> {
	private CurrencyPair currencyPair;
	private String exchange;

    private Sides sides;
    //---cache:



//-------------------------------------------------------------------------------------
    public Orderbook(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair, String _exchange, Orderbooks _orderbooks) {
    	super(_debug, _resolver);
		debug.outln(Debug.INFO, "Load orderbook for "+_currencyPair+"/"+_exchange+"...");
    	currencyPair = _currencyPair;
    	exchange = _exchange;

    	String _prefix = exchange + "-" + currencyPair.toString();
    	debug = new Debug(_debug, _prefix, Debug.IMPORTANT3);

    	sides = new Sides(debug, _resolver, _currencyPair, _exchange, _orderbooks);
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public CurrencyPair getCurrencyPair() {
    	return currencyPair;
    }


//-------------------------------------------------------------------------------------
    public Sides getSides() {
    	return sides;
    }

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter, int _index){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_ORDERBOOKS: {
				return sides.getInfo(_netCommandParameter);				
			}
			case NetCommand.COMMAND_SHOW_CURRENCYPAIRS: {
				return (Object)currencyPair;
			}
			default: {
				return (Object)(new ArrayList<String>(Arrays.asList("Command not found:" + _netCommandParameter)));
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
        sides.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
//    	debug.outln(Debug.IMPORTANT3, "Orderbook.save...");
        sides.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Orderbook:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
