//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components.orders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public abstract class LinkableOrder extends AbstractOrder {
	protected List<Long> tradeIds;
	protected List<Long> orderIds;

	private LinkableOrder parentOrder;
	//---cache:

//-------------------------------------------------------------------------------------
    public LinkableOrder(Debug _debug, Resolver _resolver, AbstractOrder _order) {
		super(_debug, _resolver, _order);
        tradeIds = new ArrayList<Long>();
        orderIds = new ArrayList<Long>();
    }

//-------------------------------------------------------------------------------------
    public LinkableOrder(Debug _debug, Resolver _resolver, long _id, String _exchange, CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, int _position) {
		super(_debug, _resolver, _id, _exchange, _currencyPair, _time, _price, _amount, _type, _position);
        tradeIds = new ArrayList<Long>();
        orderIds = new ArrayList<Long>();
    }

//-------------------------------------------------------------------------------------
    public synchronized List<Long> getTradeIds() {
        return tradeIds;
    }

//-------------------------------------------------------------------------------------
    public synchronized void setTradeIds(List<Long> _tradeIds) {
        tradeIds = _tradeIds;
    }

//-------------------------------------------------------------------------------------
    public void setTradeId(int _index, long _tradeId) {
    	while(tradeIds.size() <= (_index + 1)) {
    		tradeIds.add(0L);
    	}
    	tradeIds.set(_index, _tradeId);
    }

//-------------------------------------------------------------------------------------
    public String getTradeIdsAsString() {
        return StringUtils.convertLongListToString(tradeIds);
    }

//-------------------------------------------------------------------------------------
    public void addTradeId(long _tradeId) {
    	if(!containsTradeId(_tradeId)){
        	tradeIds.add(_tradeId);
    	}
    }

//-------------------------------------------------------------------------------------
    public void addTradeIds(List<Long> _tradeIds) {
    	_tradeIds.forEach(_tradeId -> addTradeId(_tradeId));
    }

//-------------------------------------------------------------------------------------
    public boolean containsTradeId(long _tradeId) {
        for(int i = 0; i < tradeIds.size(); i++) {
        	long _tradeId0 = tradeIds.get(i);
        	if(_tradeId0 == _tradeId){
        		return true;
        	}
        }
        return false;
    }

//-------------------------------------------------------------------------------------
    public synchronized long getLastTradeId() {
    	long _lastTradeId = 0;
        for(int i = 0; i < tradeIds.size(); i++) {
        	long _tradeId = tradeIds.get(i);
        	if(_tradeId > _lastTradeId){
        		_lastTradeId = _tradeId;
        	}
        }
        return _lastTradeId;
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean matchTradeIdWithOrderId(AbstractTrade _trade) {
		long _orderId = _trade.getOrderId();
        for(int i = 0; i < orderIds.size(); i++) {
        	long _orderId0 = orderIds.get(i);
        	long _tradeId = _trade.getId();
        	if(_orderId0 == _orderId){
//        		setTradeId(i, _tradeId);
        		if(unmatchTradeIdWithTradeId(_tradeId)){
        			addTrade(_trade);
        		}
        		return true;
        	}
        }
        return false;
	}

//-------------------------------------------------------------------------------------
    public boolean unmatchTradeIdWithTradeId(long _tradeId) {
        for(int i = 0; i < tradeIds.size(); i++) {
        	long _tradeId0 = tradeIds.get(i);
        	if(_tradeId0 == _tradeId){
        		return false;
        	}
        }
        return true;
	}

//-------------------------------------------------------------------------------------
    public synchronized void addTrade(AbstractTrade _trade) {
		long _tradeId = _trade.getId();
		BigDecimal _tradePrice = _trade.getPrice();
		BigDecimal _tradeAmount = _trade.getAmount();

		if(getTradeIds().size() == 0){
			price = _tradePrice;
			amount = _tradeAmount;
		} else {
			BigDecimal _newAmount = amount.add(_tradeAmount);
			
			BigDecimal _existingTerm = amount.multiply(price);
			BigDecimal _newTerm = _tradeAmount.multiply(_tradePrice);

			price = (_existingTerm.add(_newTerm)).divide(_newAmount, RoundingMode.HALF_UP);
			amount = _newAmount;
		}

		addTradeId(_tradeId);
//		debug.outln("LinkableOrder.0: currentTime="+getTime() + " _tradeTime="+_trade.getTime());
		setTime(Math.max(getTime(), _trade.getTime()));
//		debug.outln("LinkableOrder.1: currentTime="+getTime() + " _tradeTime="+_trade.getTime());
	}

//-------------------------------------------------------------------------------------
    public synchronized void updateWithTrade(AbstractTrade _trade) {
    	addTradeId(_trade.getId());
		setTime(Math.max(getTime(), _trade.getTime()));
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<Long> getOrderIds() {
        return orderIds;
    }

//-------------------------------------------------------------------------------------
    public void setOrderId(long _orderId) {
    	orderIds.clear();
        addOrderId(_orderId);
    }

//-------------------------------------------------------------------------------------
    public void setOrderIds(List<Long> _orderIds) {
        orderIds = _orderIds;
    }

//-------------------------------------------------------------------------------------
    public String getOrderIdsAsString() {
        return StringUtils.convertLongListToString(orderIds);
    }

//-------------------------------------------------------------------------------------
    public void addOrderId(long _orderId) {
        orderIds.add(_orderId);
    }

//-------------------------------------------------------------------------------------
    public boolean containsOrderId(long _orderId) {
        for(int i = 0; i < orderIds.size(); i++) {
        	long _orderId0 = orderIds.get(i);
        	if(_orderId0 == _orderId){
        		return true;
        	}
        }
        return false;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public LinkableOrder getParentOrder(){
		return parentOrder;
	}
	
//-------------------------------------------------------------------------------------
	public void setParentOrder(LinkableOrder _parentOrder){
		parentOrder = _parentOrder;
	}
	
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return super.toString() + ",tradeIds="+tradeIds+",orderIds="+orderIds;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
