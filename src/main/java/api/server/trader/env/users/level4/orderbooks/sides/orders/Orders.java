//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level2.shadoworders.*;
//import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.sidesorderscontainers.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.ordercrawler.*;

//-------------------------------------------------------------------------------------
public class Orders extends AbstractCluster<Configuration, OrderX> implements FlowableOnSubscribe<UserOrdersEvent> {
	private CurrencyPair currencyPair;
	private String exchange;
	private int type;
	private SidesOrdersContainer sidesOrdersContainer;

	private OrderCrawler orderCrawler;

	//---cache:


    //---rx:
	private Flowable<UserOrdersEvent> flowable = Flowable.create(this, BackpressureStrategy.BUFFER);
	private FlowableEmitter<UserOrdersEvent> emitter;


//-------------------------------------------------------------------------------------
    public Orders(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair, String _exchange, int _type, SidesOrdersContainer _typesOrdersContainer) {
    	super(_debug, _resolver);
    	currencyPair = _currencyPair;
    	exchange = _exchange;
    	type = _type;
//    	sides = _types;
    	sidesOrdersContainer = _typesOrdersContainer;

    	orderCrawler = new OrderCrawler(_debug, _resolver, this);

//    	orderbooks = _types.getOrderbooks();

//    	orders = new ArrayList<OrderX>();

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);
/*    	flowable = Flowable.create(this, BackpressureStrategy.DROP)
    			.share()
    			.onBackpressureBuffer(16, () -> { },
              		BackpressureOverflowStrategy.DROP_OLDEST);*/

        if(emitter != null){
        	emitter.onNext(new UserOrdersEvent(UserOrdersEvent.ORDERS_UPDATE));
        }
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(ShadowOrder _shadowOrder){
	}

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
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public CurrencyPair getCurrencyPair(){
		return currencyPair;
	}

//-------------------------------------------------------------------------------------
	public String getExchange(){
		return exchange;
	}

//-------------------------------------------------------------------------------------
	public int getType(){
		return type;
	}

//-------------------------------------------------------------------------------------
	public synchronized OrderX getNextOrder(){
		if(type == OrderX.TYPE_BUY){
			return orderCrawler.getLowestPriceOrder();
		} else {
			return orderCrawler.getHighestPriceOrder();
		}
	}
	
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public synchronized void pack(){
//		debug.outln(Debug.INFO, "Orders.getLowestPriceOrder: ...");
		List<OrderX> _orders = getEntities();
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			if(_order.getState() == OrderX.STATE_WAITING_FOR_EXECUTION){
				removeOrderById(_order.getId());
			}
		}
	}

//-------------------------------------------------------------------------------------
    public synchronized OrderX getOrderById(long _id) {
    	Key _key = new Key(_id);
		return getEntity(_key);
    }

//-------------------------------------------------------------------------------------
    public synchronized void removeOrderById(long _id) {
    	Key _key = new Key(_id);
		removeEntity(_key);
		if(sidesOrdersContainer != null){
			sidesOrdersContainer.removeOrderById(_id);
		}
    }

//-------------------------------------------------------------------------------------
	public synchronized void setState(List<Long> _orderIds, int _state){
//		debug.outln(Debug.IMPORTANT3, "Orders setState..."+_orderIds+", state="+_state);
		List<OrderX> _orders = getEntities();
		_orderIds.forEach(_orderId -> {
			getOrderById(_orderId).setState(_state);
		});
	}

//-------------------------------------------------------------------------------------
	public synchronized int size(){
		List<OrderX> _orders = getEntities();
		return _orders.size();
	}

//-------------------------------------------------------------------------------------
	public synchronized long addOrder(OrderX _order){
		debug.outln(Debug.WARNING, "["+this+"] adding order:["+_order+"]");
		long _id = sidesOrdersContainer.addOrder(_order);
    	Key _key = new Key(_id);
		addEntity(_key, _order);
		return _id;
	}


//-------------------------------------------------------------------------------------
    public synchronized void addTrade(AbstractTrade _trade) {
    	if(matchTradeWithOrders(_trade)){

    	} else {// new unknown trade!
			OrderX _order = new OrderX(debug, resolver, _trade);
			_order.setId(-1);
	    	_order.setState(OrderX.STATE_MANUAL_TRADE);
			debug.outln(Debug.WARNING, "Unknown trade DETECTED!:["+_order+"]");
			addOrder(_order);
    	}
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean matchTradeWithOrders(AbstractTrade _trade) {
		List<OrderX> _orders = getEntities();
//		long _tradeId = _trade.getId();
//		long _orderId = _trade.getOrderId();
//		sidesOrdersContainer.checkForLatestTradeId(_tradeId);
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			if(_order.matchTradeIdWithOrderId(_trade)){
				return true;
			}
		}	
		return false;
	}

//-------------------------------------------------------------------------------------
	public synchronized OrderX getOrder(int _index){
//		return getOrderById(_index);
		List<OrderX> _orders = getEntities();
		return _orders.get(_index);
	}

//-------------------------------------------------------------------------------------
	public synchronized List<OrderX> getOrders(){
//		return getOrderById(_index);
		List<OrderX> _orders = getEntities();
		return _orders;//.get(_orders.size() -1 -_index);
	}

//-------------------------------------------------------------------------------------
	public synchronized BigDecimal getTotalActualConsumed(List<Long> _peers){
		BigDecimal _totalActualSpent = BigDecimal.ZERO;
    	for (int i = 0; i < _peers.size(); i++) {
    		long _peerId = _peers.get(i);
    		OrderX _order = getOrderById(_peerId);
    		BigDecimal _spent = _order.getAmountConsumed();
    		_totalActualSpent = _totalActualSpent.add(_spent);
    	}	
    	return _totalActualSpent;
    }

//-------------------------------------------------------------------------------------
	public List<String> getInfo(){
        List<String> _list = new ArrayList<String>();
		List<OrderX> _orders = getEntities();
        for (int i = 0; i < _orders.size(); i++) {
            OrderX _order = _orders.get(i);
            _list.addAll(_order.getInfo());
        }
        return _list;
    }

//-------------------------------------------------------------------------------------
	private void loadAllOrdersFromSidesOrdersContainer(){
		List<OrderX> _orders = sidesOrdersContainer.getAllOrders();
		int _counter = 0;
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			if(_order.getType() == type){
		    	Key _key = new Key(_order.getId());
				addEntity(_key, _order);
				_counter++;
			}
		}
		debug.outln(Debug.INFO, "Totaly loaded orders="+_counter + " out of "+_orders.size() + " available...");
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    	loadAllOrdersFromSidesOrdersContainer();
    	sidesOrdersContainer
    	.getOrdersEvent()
		.subscribeOn(Schedulers.computation())
		.observeOn(Schedulers.computation(), false)
		.subscribe(_userOrdersEvent -> {
						orderCrawler.refresh();
					},
					_throwable -> onError(_throwable),
					() -> onCompleted());
		orderCrawler.refresh();
	}

//-------------------------------------------------------------------------------------
    public void save() {
//    	debug.outln(Debug.IMPORTANT3, "Orders.save...0");
//    	pack();
    	super.save();
//    	debug.outln(Debug.IMPORTANT3, "Orders.save...1");
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Orders:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
