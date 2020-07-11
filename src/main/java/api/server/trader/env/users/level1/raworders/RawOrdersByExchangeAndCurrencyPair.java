//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.raworders;
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
import org.knowm.xchange.service.trade.params.*;
import org.knowm.xchange.service.trade.params.orders.*;
import org.knowm.xchange.exceptions.*;
import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.common.loaders.orders.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;

//-------------------------------------------------------------------------------------
public class RawOrdersByExchangeAndCurrencyPair extends AbstractCluster<Configuration, RawOrder> implements FlowableOnSubscribe<UserOrdersEvent>, Runnable {
	private String exchange;
	private CurrencyPair currencyPair;

	//---cache:
	private TradeService tradeService;
	private DefaultOpenOrdersParamCurrencyPair defaultOpenOrdersParamCurrencyPair;
	private Thread thisThread;
	
    //---rx:
	private Flowable<UserOrdersEvent> flowable;
	private FlowableEmitter<UserOrdersEvent> emitter;


//-------------------------------------------------------------------------------------
    public RawOrdersByExchangeAndCurrencyPair(Debug _debug, Resolver _resolver, String _exchange, CurrencyPair _currencyPair) {
    	super(_debug, _resolver);
    	exchange = _exchange;
    	currencyPair = _currencyPair;

		UserExchange _userExchange = resolver.getUserExchanges().getUserExchange(exchange);

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);

    	ExchangeConfiguration _exchangeConfiguration = _userExchange.getExchangeConfiguration();
		CurrencyPair _currencyPairCode = _exchangeConfiguration.getCurrencyPairCode(_currencyPair);

		if(!_exchangeConfiguration.supportForStreamingOrderChanges()){
	        tradeService = _userExchange.getTradeService();
	        defaultOpenOrdersParamCurrencyPair = new DefaultOpenOrdersParamCurrencyPair(_currencyPairCode);
	        try{
		    	thisThread = new Thread(this);
		    	thisThread.start();
		   	}catch(Throwable _t){
    			onError(_t);
    		}
		} else {
			io.reactivex.Observable<Order> _orderChanges = _resolver.getUserExchanges().getUserExchange(_exchange)
	    			.getOrderChanges(_currencyPairCode);

	    	if(_orderChanges != null){
	    			_orderChanges.subscribeOn(Schedulers.io())
	    			.observeOn(Schedulers.computation(), false)
	    			.subscribe(_order -> onNext(_order),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());
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
    public void run() {
        while(isEnabled()){
            try{
//                Thread.sleep(1800000);
                Thread.sleep(6000);
            }catch(InterruptedException _e){
            	break;
            }
//            checkForNewOrders();
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private void checkForNewOrders() {
		debug.outln("Check for new orders...0");
		OpenOrders _openOrders = null;
		try{
			_openOrders = tradeService.getOpenOrders(defaultOpenOrdersParamCurrencyPair);			
		} catch(IOException _e){
			debug.outln(Debug.ERROR, "Cannot retreive orders...");
			return;
		}
		checkForNewOrders(_openOrders.getAllOpenOrders());
    }

//-------------------------------------------------------------------------------------
    private void checkForNewOrders(List<Order> _orders) {
		debug.outln("Check for new orders...1"+_orders);
		for (int i = 0; i < _orders.size(); i++) {
			Order _order = _orders.get(i);
			checkIfOrderAlreadyExists(_order);
		}
    }

//-------------------------------------------------------------------------------------
    private void checkIfOrderAlreadyExists(Order _order) {
		debug.outln("Check for new Order...3"+_order);
		String _orderIdString = _order.getId();
		long _orderId = StringUtils.getLongNumber(_orderIdString);
		Key _key = new Key(_orderId);
		RawOrder _rawOrder = getEntity(_key);
		if(_rawOrder == null){
			if(emitter != null){
					int _type = (_order.getType() == Order.OrderType.ASK) ? AbstractOrder.TYPE_BUY : AbstractOrder.TYPE_SELL;
					
					_rawOrder = new RawOrder(debug, 
								resolver,
								_orderId, 
								exchange, 
								currencyPair, 
								_order.getTimestamp().getTime(), 
								_order.getAveragePrice(), 
								_order.getOriginalAmount(), 
								_type,
								AbstractOrder.POSITION_LONG);

					debug.outln("New trade detected..." + _rawOrder);

					UserOrdersEvent _userOrdersEvent = 
					new UserOrdersEvent(UserOrdersEvent.ORDERS_UPDATE, _rawOrder);
					if(emitter != null){
						emitter.onNext(_userOrdersEvent);
					}
			}
		}
    }

//-------------------------------------------------------------------------------------
    public synchronized void addRawOrder(RawOrder _rawOrder) {
	    long _orderId = _rawOrder.getId();
    	Key _key = new Key(_orderId);
		addEntity(_key, _rawOrder);
	}

//-------------------------------------------------------------------------------------
    public synchronized boolean checkIfOrderIdExists(long _orderId) {
    	List<RawOrder> _rawOrders = getEntities();
    	for (int i = 0; i < _rawOrders.size() ; i++) {
    		RawOrder _rawOrder = _rawOrders.get(i);
    		if(_rawOrder.containsOrderId(_orderId)){
    			return true;
    		}
    	}
    	return false;
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void onNext(Order _order){
		checkIfOrderAlreadyExists(_order);        
	}

//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserOrdersEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserOrdersEvent> getRawOrder() {
		return flowable;
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
		setEnabled(false);
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}    	
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "RawOrdersByExchangeAndCurrencyPair:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
