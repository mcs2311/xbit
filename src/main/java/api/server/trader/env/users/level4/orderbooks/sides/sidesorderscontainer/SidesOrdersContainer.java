//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.sidesorderscontainers;
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
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;

//-------------------------------------------------------------------------------------
public class SidesOrdersContainer extends AbstractCluster<Configuration, OrderX> implements FlowableOnSubscribe<UserOrdersEvent> {
	private CurrencyPair currencyPair;
	private String exchange;
	private Sides sides;

	private Orderbooks orderbooks;
	private OrdersLoader ordersLoader;
	private Scales scales;

	private long highestId;
	private long highestTradeId;
	private long latestTradeTime;

	private int lastNumberOfOpenDeals;
    //---cache:


    //---rx:
	private Flowable<UserOrdersEvent> flowable;
	private FlowableEmitter<UserOrdersEvent> emitter;

//-------------------------------------------------------------------------------------
    public SidesOrdersContainer(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair, String _exchange, Sides _types) {
//    	super(_debug, _resolver);
		super(_debug, _resolver);
    	currencyPair = _currencyPair;
    	exchange = _exchange;
    	sides = _types;

		highestId = -1;
    	highestTradeId = 0;
    	latestTradeTime = 0;

    	lastNumberOfOpenDeals = 0;

		flowable = Flowable.create(this, BackpressureStrategy.BUFFER).share();

		orderbooks = sides.getOrderbooks();

    	ordersLoader = new OrdersLoader(_debug, _resolver, this);

    	UserExchange _userExchange = resolver.getUserExchanges()
    			.getUserExchange(exchange);
    	
    	scales = _userExchange.getScales(currencyPair);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserOrdersEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserOrdersEvent> getOrdersEvent() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNextOrderEvent(UserOrderEvent _userOrderEvent) {
    	int _type = _userOrderEvent.getType();
    	int _numberOfOpenDeals = calculateNumberOfOpenDeals();
		debug.outln(Debug.WARNING, "SidesOrdersContainer.onNextOrderEvent: ["+_userOrderEvent+"]");
    	switch(_type){
    		case UserOrderEvent.ORDER_UPDATE: {
	    		if(emitter != null){
//    				if(lastNumberOfOpenDeals != _numberOfOpenDeals){
						debug.outln(Debug.WARNING, "SidesOrdersContainer._numberOfOpenDeals["+_numberOfOpenDeals+"]");
		    			emitter.onNext(new UserOrdersEvent(UserOrdersEvent.ORDERS_UPDATE, 
		    												_numberOfOpenDeals, 
		    												_userOrderEvent.isALockSwitch()));
//    				}
	    			lastNumberOfOpenDeals = _numberOfOpenDeals;
	    		}
    			break;
    		}
    		default: {
				debug.outln(Debug.ERROR, "Unknwon order type in SidesOrdersContainer: " + _type);
				break;
    		}    		
    	}
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
    public String getExchange() {
    	return exchange;
    }

//-------------------------------------------------------------------------------------
    public long getHighestTradeId() {
    	return highestTradeId;
    }

//-------------------------------------------------------------------------------------
    public long getLastestTradeTime() {
    	return latestTradeTime;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized OrderX getOrderById(long _id) {
    	Key _key = new Key(_id);
		return getEntity(_key);
    }

//-------------------------------------------------------------------------------------
	public synchronized long addOrder(OrderX _order){
		long _id = _order.getId();
		if(_id == -1){
			highestId++;
			_id = highestId;
			_order.setId(_id);
		} else if(_id > highestId){
			highestId = _id;
		}

		_order.setScales(scales);
		_order.getOrderEvent()
				.subscribeOn(Schedulers.computation())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_userOrderEvent -> onNextOrderEvent(_userOrderEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

    	Key _key = new Key(_id);
		addEntity(_key, _order);
		onNextOrderEvent(new UserOrderEvent(UserOrderEvent.ORDER_UPDATE));
/*		if(emitter != null){
			emitter.onNext(new UserOrdersEvent(UserOrdersEvent.ORDERS_DETECTED_NEW));
		}*/

		highestTradeId = Math.max(highestTradeId, _order.getLastTradeId());
		latestTradeTime = Math.max(latestTradeTime, _order.getTime());

		orderbooks.announce(_order);
		return _id;
	}

//-------------------------------------------------------------------------------------
    public synchronized void removeOrderById(long _id) {
    	Key _key = new Key(_id);
		removeEntity(_key);
    }

//-------------------------------------------------------------------------------------
	public synchronized List<OrderX> getAllOrders(){
		List<OrderX> _orders = getEntities();
		return _orders;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public int calculateNumberOfOpenDeals(){
		List<OrderX> _orders = getEntities();
		int _numberOfOpenDeals = 0;
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			if(_order.isOpenDeal()){
				_numberOfOpenDeals++;				
			}
		}
		return _numberOfOpenDeals;
	}

//-------------------------------------------------------------------------------------
	private void postLoadOrders(){
		List<OrderX> _orders = getEntities();
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			postLoadOrder(_order);
		}
//		onNextOrderEvent(null);
	}

//-------------------------------------------------------------------------------------
    private void postLoadOrder(OrderX _order){
    	if(_order.getState() != OrderX.STATE_CONSUMED){
    		BigDecimal _amountConsumed = calculateConsumed(_order);
    		_order.setAmountConsumed(_amountConsumed);
    		BigDecimal _amountLocked = calculateLocked(_order);
    		_order.setAmountLocked(_amountLocked);
//			debug.outln(Debug.INFO, "order "+_order.getId() + ", consumed=" + _order.getAmountConsumed() + " , locked="+_order.getAmountLocked());
		}
    }

//-------------------------------------------------------------------------------------
    private BigDecimal calculateConsumed(OrderX _order){
    	return sumForwardPeersWithState(_order, OrderX.STATE_EXECUTED);
    }
    
//-------------------------------------------------------------------------------------
    private BigDecimal calculateLocked(OrderX _order){
    	return sumForwardPeersWithState(_order, OrderX.STATE_WAITING_FOR_EXECUTION);
    }
    
//-------------------------------------------------------------------------------------
    private BigDecimal sumForwardPeersWithState(OrderX _order, int _state){
/*    	synchronized(_order){
    		return _order.getAlreadySpent();
    	}*/
		List<Long> _forwardPeers = _order.getForwardPeers();
//		BigDecimal _amountConsumed = BigDecimal.ZERO;
		BigDecimal _sum = BigDecimal.ZERO;
		for (int i = 0; i < _forwardPeers.size(); i++) {
			long _id = _forwardPeers.get(i);
			OrderX _forwardOrder = getOrderById(_id);
			if(_forwardOrder.getState() == _state){
//				_order.addToAlreadySpent(_forwardOrder.getAmount());
				_sum = _sum.add(_forwardOrder.getAmount());
//				debug.outln(Debug.INFO, "SchedulerQueue.calculateConsumed.2 id= "+_order.getId()+", spent="+_order.getAlreadySpent()+", _forwardPeers="+_forwardPeers);
			}
		}
//		return _order.getAmount().subtract(_amountSpent);
		return _sum;//_order.getAlreadySpent();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private void preSaveOrders(){
		List<OrderX> _orders = getEntities();
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			preSaveOrder(_order);
		}
	}

//-------------------------------------------------------------------------------------
    private void preSaveOrder(OrderX _order){
//		if(_order.getState() == OrderX.STATE_WAITING_FOR_EXECUTION){
		if(_order.isTemporary()){
			long _id = _order.getId();
			removeOrderById(_id);
			List<Long> _backwardPeers = _order.getBackwardPeers();
			removeIdFromForwardPeers(_id, _backwardPeers);
		} else if(_order.isLocked()){
			_order.setState(OrderX.STATE_EXECUTED);
		}

		if((_order.getType() == AbstractOrder.TYPE_SELL) && 
			(_order.getPosition() == AbstractOrder.POSITION_LONG) && 
			((_order.getState() == OrderX.STATE_EXECUTED) || (_order.getState() == OrderX.STATE_MANUAL_TRADE))){
				_order.setState(OrderX.STATE_CONSUMED);
		}

	}

//-------------------------------------------------------------------------------------
    public void removeIdFromForwardPeers(long _id, List<Long> _peers){
		debug.outln(Debug.INFO, "SchedulerInputQueue.removeIdFromForwardPeers:....id="+_id+", fpeers="+_peers);
		for (int i = 0; i < _peers.size(); i++) {
			long _peerId = _peers.get(i);
			OrderX _order = getOrderById(_peerId);
			if(_order == null){
				debug.outln(Debug.INFO, "SchedulerInputQueue.removeIdFromForwardPeers: null order retreived with _peerId= " + _peerId);
			} else {
				_order.removeForwardPeer(_id);
				_order.setState(OrderX.STATE_EXECUTED);
			}
		}		
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    	ordersLoader.load();
    	postLoadOrders();
    }

//-------------------------------------------------------------------------------------
    public void save() {
//    	debug.outln(Debug.IMPORTANT3, "Sides.save...");
    	super.save();
    	preSaveOrders();
    	ordersLoader.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Sides:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
