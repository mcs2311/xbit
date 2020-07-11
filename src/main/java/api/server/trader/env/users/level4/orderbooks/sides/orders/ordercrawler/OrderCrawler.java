//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.ordercrawler;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;

//-------------------------------------------------------------------------------------
public class OrderCrawler extends AbstractItem {
	private Orders orders;

	private OrderX highestPriceOrder;
	private OrderX lowestPriceOrder;
	//---cache:

    //---rx:


//-------------------------------------------------------------------------------------
    public OrderCrawler(Debug _debug, Resolver _resolver, Orders _orders) {
    	super(_debug, _resolver);
    	orders = _orders;
    	highestPriceOrder = null;
    	lowestPriceOrder = null;
//    	refresh();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public synchronized OrderX getHighestPriceOrder(){
		debug.outln(Debug.INFO, "OrderCrawler.getHighestPriceOrder=["+highestPriceOrder+"]");
		return highestPriceOrder;
	}

//-------------------------------------------------------------------------------------
	public synchronized OrderX getLowestPriceOrder(){
		debug.outln(Debug.INFO, "OrderCrawler.getLowestPriceOrder=["+lowestPriceOrder+"]");
		return lowestPriceOrder;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public synchronized void refresh(){
		List<OrderX> _orders = orders.getEntities();
		debug.outln(Debug.INFO, "OrderCrawler.refresh=["+_orders.size()+"]");
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			if(isHighestPriceOrder(_order)){
				highestPriceOrder = _order;
			} 
			if(isLowestPriceOrder(_order)){
				lowestPriceOrder = _order;
			}
		}
	}

//-------------------------------------------------------------------------------------
/*	public synchronized OrderX getHighestPriceOrder(){
//		debug.outln(Debug.INFO, "Orders.getHighestPriceOrder: ...type["+type+"]");
//		BigDecimal _highestPrice = BigDecimal.ZERO;
		OrderX _highestPriceOrder = null;
		List<OrderX> _orders = getEntities();
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			BigDecimal _currentPrice = _order.getPrice();
//			debug.outln(Debug.INFO, "Orders.getHighestPriceOrder: ..._currentPrice="+_currentPrice+",_highestPrice="+_highestPrice+",_order.state="+_order.getStateAsString());
			if(checkForHighestPriceOrder(_order, _highestPriceOrder)){
				_highestPriceOrder = _order;
				_highestPrice = _currentPrice;				
			}*/
/*			
			if(_currentPrice.compareTo(_highestPrice) > 0){
				if(_order.isExecuted()){
					if(!_order.isConsumed() && !_order.isLocked()){
						_highestPriceOrder = _order;
						_highestPrice = _currentPrice;
					}
				}
			}*/
/*		}
		debug.outln(Debug.INFO, "Orders.getHighestPriceOrder["+type+"]:"+_highestPriceOrder);
		return _highestPriceOrder;
	}
	
//-------------------------------------------------------------------------------------
	public synchronized OrderX getLowestPriceOrder(){
//		debug.outln(Debug.INFO, "Orders.getLowestPriceOrder: ...");
		BigDecimal _lowestPrice = BigDecimal.valueOf(Integer.MAX_VALUE);
		OrderX _lowestPriceOrder = null;
		List<OrderX> _orders = getEntities();
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			BigDecimal _currentPrice = _order.getPrice();
//			debug.outln(Debug.INFO, "Orders.getLowestPriceOrder: ..._currentPrice="+_currentPrice+",_lowestPrice="+_lowestPrice+",_order.state="+_order.getStateAsString());
			if(_currentPrice.compareTo(_lowestPrice) < 0){
				if(_order.isExecuted()){
					if(!_order.isConsumed() && !_order.isLocked()){
						_lowestPriceOrder = _order;
						_lowestPrice = _currentPrice;
					}
				}
			}
		};
		debug.outln(Debug.INFO, "Orders.getLowestPriceOrder["+type+"]:"+_lowestPriceOrder);
		return _lowestPriceOrder;
	}

*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private boolean isHighestPriceOrder(OrderX _order){
//		debug.outln(Debug.INFO, "Orders.getHighestPriceOrder: ...type["+type+"]");
		if(!_order.isGoodForTrading()){
			return false;
		}
		if(highestPriceOrder == null){
			return true;
		}
		BigDecimal _highestPrice = highestPriceOrder.getPrice();
		BigDecimal _currentPrice = _order.getPrice();

		int _highestPosition = highestPriceOrder.getPosition();
		int _currentPosition = _order.getPosition();
		if((_highestPosition == AbstractOrder.POSITION_LONG) && 
			(_currentPosition == AbstractOrder.POSITION_SHORT)){
			return true;
		}
		return (_currentPrice.compareTo(_highestPrice) > 0);
//		debug.outln(Debug.INFO, "Orders.getHighestPriceOrder["+type+"]:"+_highestPriceOrder);
	}

//-------------------------------------------------------------------------------------
	private boolean isLowestPriceOrder(OrderX _order){
//		debug.outln(Debug.INFO, "Orders.getHighestPriceOrder: ...type["+type+"]");
		if(!_order.isGoodForTrading()){
			return false;
		}
		if(lowestPriceOrder == null){
			return true;
		}
		BigDecimal _lowestPrice = lowestPriceOrder.getPrice();
		BigDecimal _currentPrice = _order.getPrice();
/*
		int _highestPosition = highestPriceOrder.getPosition();
		int _currentPosition = _order.getPosition();
		if((_highestPosition == AbstractOrder.POSITION_LONG) && 
			(_currentPosition == AbstractOrder.POSITION_SHORT)){
			return true;
		}*/
		return (_currentPrice.compareTo(_lowestPrice) < 0);
//		debug.outln(Debug.INFO, "Orders.getHighestPriceOrder["+type+"]:"+_highestPriceOrder);
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
		return "OrderCrawler:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
