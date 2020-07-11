//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.raworders.orderexecutor;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.Collection;
import java.util.Date;
//import java.lang.reflect.*;

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
import org.knowm.xchange.exceptions.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;
import codex.xbit.api.server.trader.env.users.level1.raworders.*;


//-------------------------------------------------------------------------------------
public class OrderExecutor extends AbstractItem {
	private RawOrder rawOrder;

	//limit:
    private String limitOrderId;
    private LimitOrder limitOrder;
	private LimitOrder lastExecutedLimitOrder;
	private long lastRealId;

    //---cache:
    private UserExchange exchange;
	private CurrencyPair currencyPairCoded;
	private TraderConfiguration traderConfiguration;
    private OrderQueryParamCurrencyPair orderQuery;
	private TradeService tradeService;

	//---statics:

//-------------------------------------------------------------------------------------
    public OrderExecutor(Debug _debug, Resolver _resolver, RawOrder _rawOrder) {
    	super(_debug, _resolver);
    	rawOrder = _rawOrder;
		traderConfiguration = _resolver.getTraderConfiguration();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private void init(){
		debug.outln(Debug.INFO, "OrderExecutor: >>>" + rawOrder);		
		exchange = resolver.getUserExchanges().getUserExchange(rawOrder.getExchange());

        tradeService = exchange.getTradeService();
        if (tradeService == null) {
            debug.outln(Debug.ERROR, "TradeService is null for exchange " + exchange.getExchangeConfiguration().getShortName());
            return;
        }

		CurrencyPair _currencyPairCoded = exchange.getExchangeConfiguration().getCurrencyPairCode(rawOrder.getCurrencyPair());
    	debug.outln(Debug.INFO, "executor started for "+_currencyPairCoded + ", type="+rawOrder.getType()+", amount="+rawOrder.getAmount()+", price="+rawOrder.getPrice());
        currencyPairCoded = _currencyPairCoded;
		orderQuery = new DefaultQueryOrderParamCurrencyPair​(_currencyPairCoded, null);
		limitOrderId = null;
		limitOrder = null;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized Response placeOrder(BigDecimal _price, BigDecimal _amount) {
//        debug.out(Debug.IMPORTANT1, "Place rawOrder : ");
    	if(exchange == null){
    		init();
    	}
    	
    	long _timestamp = System.currentTimeMillis();
//  		String _id = String.valueOf(_timestamp);


  		Order.OrderType _type = (rawOrder.getType() == RawOrder.TYPE_BUY) ? Order.OrderType.BID : Order.OrderType.ASK;

		limitOrder = new LimitOrder(_type,
			_amount,
			currencyPairCoded,
			rawOrder.getIdAsString(),
			rawOrder.getTimestamp(),
			_price);
		limitOrder.setPostOnly(true);

    	if(traderConfiguration.getMode() != TraderConfiguration.MODE_LIVE){
    		delayDemo();
    		return Response.RESPONSE_PLACED;
    	}
//        	debug.outln(Debug.IMPORTANT1, "Place limitOrder 0: " + limitOrder.getLimitPrice() + " .... amount= " + rawOrder.getAmount());
        try{
            limitOrderId = tradeService.placeLimitOrder(limitOrder);
        	debug.outln(Debug.IMPORTANT1, "Place limitOrder: price=" + limitOrder.getLimitPrice() + ", amount= " + _amount + " .... limitOrderId= " + limitOrderId + ": RESPONSE_CODE_PLACED");
        	String[] _ids = limitOrderId.split("\\.");
        	limitOrderId = _ids[0];
            lastExecutedLimitOrder = limitOrder;
            lastRealId = StringUtils.getLongNumber(_ids[1]);
    		return new Response(Response.RESPONSE_CODE_PLACED, lastRealId);
        }catch(IOException _e1){
            debug.outln(Debug.ERROR, "Cannot place rawOrder: IOException:"+_e1.getMessage());
        } catch(ExchangeException _e2){
            debug.outln(Debug.ERROR, "Cannot place rawOrder: ExchangeException:"+_e2.getMessage());
        }catch(Exception _e3){
            debug.outln(Debug.ERROR, "Cannot place rawOrder: Exception:"+_e3.getMessage() + ",_price=" + _price + ",_amount=" + _amount);
            if(_e3.getMessage().trim().equals("Insufficient funds")){
        		debug.outln(Debug.IMPORTANT1, "Place limitOrder: price=" + limitOrder.getLimitPrice() + ", amount= " + _amount + " .... limitOrderId= " + limitOrderId + ": RESPONSE_ERROR/ERROR_CODE_INSUFFICIENT_FUNDS");
            	return new Response(Response.RESPONSE_CODE_ERROR, Response.ERROR_CODE_INSUFFICIENT_FUNDS);
            }
        }
        debug.outln(Debug.IMPORTANT1, "Place limitOrder: price=" + limitOrder.getLimitPrice() + ", amount= " + _amount + " .... limitOrderId= " + limitOrderId + ": RESPONSE_ERROR");
    	return Response.RESPONSE_ERROR;
	}
/*
//-------------------------------------------------------------------------------------
    public synchronized boolean changeOrder(BigDecimal _amount, BigDecimal _price) {
//        debug.out(Debug.IMPORTANT1, "Change rawOrder :" + orderId + " with price =" +rawOrder.getLimitPrice());
    	if(traderConfiguration.getMode() != TraderConfiguration.MODE_LIVE){
    		delayDemo();
    		return true;
    	}
    	try{
//    		return (cancelOrder() && placeOrder());
	    	if(cancelOrder()){
	    		rawOrder.setAmount(_amount);
	    		rawOrder.setPrice(_price);
	    		return placeOrder();
	    	}


//            lastExecutedLimitOrder = limitOrder;
//            debug.outln(Debug.IMPORTANT1, "Change rawOrder :" + orderId + " with price =" +rawOrder.getLimitPrice() + " .... orderId= " + orderId);
//			return true;
    	}catch(Exception _e){
    		debug.outln(Debug.ERROR, "Order could not be changed... checking status...");
//    		_e.printStackTrace();
    	}
//        debug.outln(Debug.IMPORTANT1, "", false);
    	return false;
    }
*/

//-------------------------------------------------------------------------------------
    public synchronized Response cancelOrder() {


    	if(traderConfiguration.getMode() != TraderConfiguration.MODE_LIVE){
//    		delayDemo();
    		return Response.RESPONSE_CANCELLED;
    	}
    	if((tradeService == null) || (limitOrderId == null) || (limitOrder == null)){
    		return Response.RESPONSE_ERROR;
    	}
//        debug.outln(Debug.IMPORTANT1, "Cancel limitOrder : " + limitOrder.getLimitPrice() + " .... limitOrderId= " + limitOrderId);
    	try{
    		boolean _success = tradeService.cancelOrder​(limitOrderId);
    		if(_success){
        		debug.outln(Debug.IMPORTANT1, "Cancel rawOrder : " + limitOrderId + "." + lastRealId + ": RESPONSE_CANCELLED");
    			return Response.RESPONSE_CANCELLED;
    		}
    	}catch(Exception _e){
//    		debug.outln(Debug.ERROR, "Order could not be canceled... checking status..."+_e.getMessage());
//    		_e.printStackTrace();
    	}
//    	pushEvent(Event.ORDER_FAILED_EVENT);
        debug.outln(Debug.IMPORTANT1, "Cancel rawOrder : " + limitOrderId + "." + lastRealId + ": RESPONSE_ERROR");
    	return Response.RESPONSE_ERROR;
	}

//-------------------------------------------------------------------------------------
    public synchronized LimitOrder getStatus() {
    	if(traderConfiguration.getMode() != TraderConfiguration.MODE_LIVE){
    		delayDemo();
    		return null;
    	}
    	if(tradeService == null){
    		debug.outln(Debug.ERROR, "OrderExecutor.getStatus tradeService is null");
    		return null;
    	}
    	if(limitOrderId == null){
    		debug.outln(Debug.ERROR, "OrderExecutor.getStatus limitOrderId is null");
    		return null;
    	}

    	Collection<Order> _collection = null;
//    	orderQuery.setOrderId(orderId);
		orderQuery = new DefaultQueryOrderParamCurrencyPair​(currencyPairCoded, limitOrderId);
    	try{
    		_collection = tradeService.getOrder​(orderQuery);
    	}catch(IOException _e1){
    		debug.outln(Debug.ERROR, "Order refresh error: IOException on rawOrder id:" + lastRealId + " on exchange: " + exchange.getShortName());
//    		_e1.printStackTrace();
    		return null;
    	} catch(org.knowm.xchange.hitbtc.v2.dto.HitbtcException _e2) {
    		debug.outln(Debug.ERROR, "OrderHelper.getStatus Order not found!!!");
//    		System.exit(0);
    		return null;
    	}
//		debug.outln(Debug.ERROR, "getStatus._collection="+_collection);
    	if(_collection.size() < 1){
//    		debug.outln(Debug.ERROR, "Order getstatus rawOrder id:" + lastRealId + "  _collection: " + _collection);
    		return null;
    	}
    	LimitOrder _order = (LimitOrder)_collection.toArray()[0];
//		Order.OrderStatus _newStatus = _rawOrder.getStatus();
		return _order;

//		debug.outln(Debug.ERROR, "Check orderId="+limitOrderId+"  :  OrderStatus="+_newStatus);
//		return _newStatus;
	}
/*
//-------------------------------------------------------------------------------------
    public Decision getFinalDecision() {
    	if(lastExecutedLimitOrder == null){
    		return null;
    	}
		return new Decision(Decision.DECISION_NONE,
			lastExecutedLimitOrder.getOriginalAmount(),
			lastExecutedLimitOrder.getLimitPrice());
//			finalOrder.getCumulativeAmount(),
//			finalOrder.getAveragePrice());
	}
*/
//-------------------------------------------------------------------------------------
    private void delayDemo() {
		try{
			Thread.sleep(50);
		}catch(InterruptedException _e){}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }

//-------------------------------------------------------------------------------------
    public void save() {
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Executor:" + rawOrder;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------