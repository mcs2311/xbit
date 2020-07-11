//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import info.bitrich.xchangestream.core.*;
import org.knowm.xchange.*;
import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.marketdata.*;
import org.knowm.xchange.service.trade.*;
import org.knowm.xchange.service.trade.params.orders.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.ta4j.core.*;
import org.ta4j.core.num.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;

//-------------------------------------------------------------------------------------
public class Orderbooks extends AbstractCluster<Configuration, Orderbook> implements FlowableOnSubscribe<UserOrderbookEvent> {
//	private TacticConfiguration tacticConfiguration;
    //---cache:

    //---rx:
	private Flowable<UserOrderbookEvent> flowable;
	private FlowableEmitter<UserOrderbookEvent> emitter;

//-------------------------------------------------------------------------------------
    public Orderbooks(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
//        configurationLoader = new ConfigurationLoader<OrderbookConfiguration>(_debug, _path, ".orderbook", "currencyPair", OrderbookConfiguration.class);
//        tacticConfiguration = _tacticConfiguration;
//        orderbooks = new ArrayList<Orderbook>();
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER)
    				.share();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserOrderbookEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserOrderbookEvent> getOrderbookEvent() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
	public synchronized void announce(OrderX _order) {
		UserOrderbookEvent _userOrderbookEvent = new UserOrderbookEvent(UserOrderbookEvent.ORDER_ADDED, _order);
		if(emitter != null){
			emitter.onNext(_userOrderbookEvent);
		}		
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized Orderbook getOrderbook(CurrencyPair _currencyPair, String _exchange) {
//		debug.outln(Debug.INFO, "getOrderbook: "+_currencyPair+"," +_exchange+"...");
    	Key _key = new Key(_currencyPair, _exchange);
		Orderbook _orderbook = getEntity(_key);
		if(_orderbook == null){
//			debug.outln(Debug.INFO, "getOrderbook = null... ");
			_orderbook = new Orderbook(debug, resolver, _currencyPair, _exchange, this);
			addEntity(_key, _orderbook);
		}
    	return _orderbook;
    }

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter){
        List<Object> _list = new ArrayList<Object>();
        List<Orderbook> _orderbooks = getEntities();
        for (int i = 0; i < _orderbooks.size(); i++) {
            Orderbook _orderbook = _orderbooks.get(i);
            Object _message = _orderbook.getInfo(_netCommandParameter, i);
            if(_message != null){
            	_list.add(_message);
            }
        }
        return (Object)_list;
    }

//-------------------------------------------------------------------------------------
    public void changeOrderbook(String[] _args) {
    	try{
        	debug.outln(Debug.IMPORTANT3, "Change orderbook #"+_args[1]+" " + _args[2] + " = " + _args[3]);
    	}catch(Exception _e){
        	debug.outln(Debug.ERROR, "Error: " + _e.getMessage());	
    	}

	}


//-------------------------------------------------------------------------------------
    public void changeProfit(String[] _args) {
/*    	String _profit = _args[1];
    	BigDecimal _targetProfit = new BigDecimal(_profit);
        debug.out(Debug.IMPORTANT3, "Setting target profit = "+_targetProfit+" for orderbooks...");
		for (Map.Entry<Key, Orderbook> _entry : entities.entrySet()) {
//		    System.out.println(entry.getKey() + "/" + entry.getValue());
		    Key _key = _entry.getKey();
		    Orderbook _orderbook = _entry.getValue();
		    OrderbookConfiguration _orderbookConfiguration = _orderbook.getConfiguration();
    		if(_orderbookConfiguration.isEnabled() && _orderbookConfiguration.isNotClosed()){
        		debug.out(Debug.IMPORTANT3, "["+_orderbookConfiguration.getId()+"]", false);		
    			_orderbookConfiguration.setTargetProfit(_targetProfit);
    		}
		}    	
        debug.outln(Debug.IMPORTANT3, "", false);	*/	
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//    	exchanges = resolver.getUserExchanges();
//    	super.load();
/*    	List<CurrencyPair> _currencyPairs = tacticConfiguration.getPairs();
    	_currencyPairs.forEach(_currencyPair -> {
    		Orderbook _orderbook = new Orderbook(debug, resolver, tacticConfiguration, _currencyPair);
    		orderbooks.add(_orderbook);
    		_orderbook.load();
    	});*/
	}

//-------------------------------------------------------------------------------------
    public void save() {
//    	debug.outln(Debug.IMPORTANT3, "Orderbooks.save...");
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Orderbooks:";
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
