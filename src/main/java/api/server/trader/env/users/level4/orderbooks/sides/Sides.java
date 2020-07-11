//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
//import java.util.*;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import codex.common.utils.*;

import org.knowm.xchange.currency.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.common.loaders.orders.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level1.rawtrades.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.sidesorderscontainers.*;


//-------------------------------------------------------------------------------------
public class Sides extends AbstractItem {
	private CurrencyPair currencyPair;
	private String exchange;
	private Orderbooks orderbooks;
	private SidesOrdersContainer sidesOrdersContainer;

    private Side buySide;
    private Side sellSide;

    //---cache:
//    private RawTradesByExchangeAndCurrencyPair rawTradesByExchangeAndCurrencyPair;


//-------------------------------------------------------------------------------------
    public Sides(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair, String _exchange, Orderbooks _orderbooks) {
    	super(_debug, _resolver);
    	currencyPair = _currencyPair;
    	exchange = _exchange;
		orderbooks = _orderbooks;

    	sidesOrdersContainer = new SidesOrdersContainer(_debug, _resolver, _currencyPair, _exchange, this);

		buySide = new Side(_debug, _resolver, _currencyPair, _exchange, OrderX.TYPE_BUY, this);
		sellSide = new Side(_debug, _resolver, _currencyPair, _exchange, OrderX.TYPE_SELL, this);

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
    public Side getSide(int _type) {
    	if(_type == OrderX.TYPE_BUY){
    		return buySide;
    	} else {
    		return sellSide;
    	}
    }

//-------------------------------------------------------------------------------------
    public Side getReverseSide(int _type) {
    	if(_type != OrderX.TYPE_BUY){
    		return buySide;
    	} else {
    		return sellSide;
    	}
	}

//-------------------------------------------------------------------------------------
    public Orderbooks getOrderbooks() {
    	return orderbooks;
    }

//-------------------------------------------------------------------------------------
    public SidesOrdersContainer getSidesOrdersContainer() {
    	return sidesOrdersContainer;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private void onNext(UserTradeEvent _userTradeEvent) {
    	int _type = _userTradeEvent.getType();
    	switch(_type){
    		case UserTradeEvent.TRADE_DETECTED_NEW : {
    			AbstractTrade _trade = _userTradeEvent.getTrade();
    			debug.outln(Debug.ERROR, "Sides. TRADE_DETECTED_NEW:"+_trade);
//    			if(_trade.getId() > sidesOrdersContainer.getLastTradeId()){
	    			getSide(_trade.getType()).addTrade(_trade);
//		rawTradesByExchangeAndCurrencyPair    			
//    			.setLastTradeId(sidesOrdersContainer.getLastTradeId());
//    			}
    			break;
    		}
    		default :{
    			debug.outln(Debug.ERROR, "Unknown operation in Sides:"+_type);
    			break;
    		}
    	}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public List<String> getInfo(int _netCommandParameter){
        List<String> _list = new ArrayList<String>();
		_list.addAll(buySide.getInfo(_netCommandParameter));
		_list.addAll(sellSide.getInfo(_netCommandParameter));
        return _list;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();


    	sidesOrdersContainer.load();

    		resolver.getMixers()
    			.getMixer(exchange, currencyPair)
    			.getRawTrade(sidesOrdersContainer.getHighestTradeId(),
    				sidesOrdersContainer.getLastestTradeTime())
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_userTradeEvent -> onNext(_userTradeEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

        buySide.load();
        sellSide.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
//    	debug.outln(Debug.IMPORTANT3, "Sides.save...");
    	super.save();
//    	buySide.getScheduler().save();
//    	sellSide.getScheduler().save();
        buySide.save();
        sellSide.save();
    	sidesOrdersContainer.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Sides:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
