//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level2.shadoworders;
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
import codex.xbit.api.server.trader.common.loaders.orders.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public class ShadowOrders extends AbstractCluster<Configuration, ShadowOrder> {
	private TacticConfiguration tacticConfiguration;
	private CurrencyPair currencyPair;
	private int type;


//-------------------------------------------------------------------------------------
//    public ShadowOrders(Debug _debug, Resolver _resolver, TacticConfiguration _tacticConfiguration, CurrencyPair _currencyPair, int _type) {
    public ShadowOrders(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
//    	tacticConfiguration = _tacticConfiguration;
//    	currencyPair = _currencyPair;
//    	type = _type;
    }
/*
//-------------------------------------------------------------------------------------
	public TacticConfiguration getTacticConfiguration(){
		return tacticConfiguration;
	}

//-------------------------------------------------------------------------------------
	public CurrencyPair getCurrencyPair(){
		return currencyPair;
	}

//-------------------------------------------------------------------------------------
	public int getType(){
		return type;
	}

//-------------------------------------------------------------------------------------
	public synchronized ShadowOrder getNextOrder(){
		if(type == ShadowOrder.TYPE_BUY){
			return getLowestPriceOrder();
		} else {
			return getHighestPriceOrder();
		}
	}

//-------------------------------------------------------------------------------------
	public synchronized ShadowOrder getHighestPriceOrder(){
//		debug.outln(Debug.INFO, "Orders.getHighestPriceOrder: ...type["+type+"]");
		BigDecimal _highestPrice = BigDecimal.ZERO;
		ShadowOrder _highestPriceOrder = null;
		for (int i = 0; i < shadowOrders.size(); i++) {
			ShadowOrder _order = shadowOrders.get(i);
			BigDecimal _currentPrice = _order.getPrice();
			if(_currentPrice.compareTo(_highestPrice) > 0){
				if(_order.getState() == ShadowOrder.STATE_WAITING_TO_BE_CONSUMED){
					if(!_order.isSpent()){
						_highestPriceOrder = _order;
						_highestPrice = _currentPrice;
					}
				}
			}
		}
		return _highestPriceOrder;
	}
	
//-------------------------------------------------------------------------------------
	public synchronized ShadowOrder getLowestPriceOrder(){
//		debug.outln(Debug.INFO, "Orders.getLowestPriceOrder: ...");
		BigDecimal _lowestPrice = BigDecimal.valueOf(Integer.MAX_VALUE);
		ShadowOrder _lowestPriceOrder = null;
		for (int i = 0; i < shadowOrders.size(); i++) {
			ShadowOrder _order = shadowOrders.get(i);
			BigDecimal _currentPrice = _order.getPrice();
//			debug.outln(Debug.INFO, "Orders.getLowestPriceOrder: ..._currentPrice="+_currentPrice+",_lowestPrice="+_lowestPrice+",_order.state="+_order.getStateAsString());
			if(_currentPrice.compareTo(_lowestPrice) < 0){
				if(_order.getState() == ShadowOrder.STATE_WAITING_TO_BE_CONSUMED){
					if(!_order.isSpent()){
						_lowestPriceOrder = _order;
						_lowestPrice = _currentPrice;
					}
				}
			}
		};
		return _lowestPriceOrder;
	}

//-------------------------------------------------------------------------------------
	public synchronized void pack(){
//		debug.outln(Debug.INFO, "Orders.getLowestPriceOrder: ...");
		for (int i = 0; i < shadowOrders.size(); i++) {
			ShadowOrder _order = shadowOrders.get(i);
			if(_order.getState() == ShadowOrder.STATE_NOT_EXECUTED){
				shadowOrders.remove(_order);
			}
		}
	}

//-------------------------------------------------------------------------------------
    public synchronized ShadowOrder getOrderById(int _id) {
    	for (int i = 0; i < shadowOrders.size(); i++) {
    		ShadowOrder _order = shadowOrders.get(i);
			if(_order.getId() == _id){
				return _order;
			}    		
    	}
		return null;
    }

//-------------------------------------------------------------------------------------
	public synchronized void setState(List<Integer> _orderIds, int _state){
		debug.outln(Debug.IMPORTANT3, "Orders setState..."+_orderIds+", state="+_state);
		_orderIds.forEach(_orderId -> {
			shadowOrders.get(_orderId).setState(_state);
		});
	}

//-------------------------------------------------------------------------------------
	public synchronized int size(){
		return shadowOrders.size();
	}
*/
//-------------------------------------------------------------------------------------
	public synchronized void addOrder(ShadowOrder _shadowOrder){
    	Key _key = new Key(_shadowOrder.getExchange(), _shadowOrder.getCurrencyPair(), _shadowOrder.getId());
    	addEntity(_key, _shadowOrder);
	}

//-------------------------------------------------------------------------------------
	public synchronized ShadowOrder getOrderByOrderId(long _orderId){
		List<ShadowOrder> _shadowOrders = getEntities();
    	debug.outln(Debug.IMPORTANT3, "ShadowOrder.getOrderById.0:" + _orderId);
		for (int i = 0; i < _shadowOrders.size(); i++) {
			ShadowOrder _shadowOrder = _shadowOrders.get(i);
			if(_shadowOrder.matchOrderId(_orderId)){
    			debug.outln(Debug.IMPORTANT3, "ShadowOrder.getOrderById.1:" + _shadowOrder);
				return _shadowOrder;
			}
		}
    	debug.outln(Debug.IMPORTANT3, "ShadowOrder.getOrderById.2:");
		return null;
	}

//-------------------------------------------------------------------------------------
    public synchronized boolean matchOrders(UserTradeEvent _userTradeEvent){
//    	queue.put(_userTradeEvent);
    	ShadowOrder _shadowOrder = getOrderByOrderId(_userTradeEvent.getTrade().getOrderId());
    	debug.outln(Debug.IMPORTANT3, "ShadowOrders.updateOrder.0:" + _shadowOrder);
    	if(_shadowOrder != null){
    		_shadowOrder.updateWithTrade(_userTradeEvent.getTrade());
    		return true;
    	}
    	return false;
    }


/*
//-------------------------------------------------------------------------------------
	private synchronized int getLastId(){
		int _size = shadowOrders.size();
		if(_size == 0){
			return -1;
		} else {
			return shadowOrders.get(_size - 1).getId();
		}
	}

//-------------------------------------------------------------------------------------
	public List<String> getInfo(){
        List<String> _list = new ArrayList<String>();
        for (int i = 0; i < shadowOrders.size(); i++) {
            ShadowOrder _order = shadowOrders.get(i);
            _list.addAll(_order.getInfo());
        }
        return _list;
    }
*/
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
