//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level2.shadoworders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.disposables.*;
import io.reactivex.schedulers.*;

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
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.users.level1.raworders.*;
import codex.xbit.api.server.trader.env.users.level1.rawtrades.*;

//-------------------------------------------------------------------------------------
public class ShadowOrder extends LinkableOrder implements FlowableOnSubscribe<UserOrderEvent> {
	private int state;

	//---shadow:
	private BigDecimal alreadySpent;
	private LinkableOrder orderIn;
	private RawOrder orderOut;

    //---rx:
	private Flowable<UserOrderEvent> flowable;
	private FlowableEmitter<UserOrderEvent> emitter;

	//---cache:
	private long lastOrderId;
//	private RawTradesByExchangeAndCurrencyPair rawTradesByExchangeAndCurrencyPair;

	public static final int STATE_NONE 								= 0;
	public static final int STATE_INIT 								= 1;
	public static final int STATE_WAITING 							= 2;
	public static final int STATE_PARTIALLY_EXECUTED		 		= 3;
	public static final int STATE_EXECUTED		 					= 4;
	public static final int STATE_STOPPED		 					= 5;

//-------------------------------------------------------------------------------------
    public ShadowOrder(Debug _debug, Resolver _resolver, LinkableOrder _orderIn) {
    	super(_debug, _resolver, _orderIn);
    	orderIn = _orderIn;
		setState(STATE_NONE);
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER)
    				.share();
        debug.outln(Debug.IMPORTANT3, "ShadowOrder created... "+this);

    	orderOut = new RawOrder(_debug, _resolver, this);
    	resolver.getRawOrders()
    		.getRawOrdersByExchangeAndCurrencyPair(_orderIn.getExchange(), _orderIn.getCurrencyPair())
    		.addRawOrder(orderOut);
    	
/*    	rawTradesByExchangeAndCurrencyPair = 
    	resolver.getRawTrades()
    			.getRawTradesByExchangeAndCurrencyPair(_orderIn.getExchange(), _orderIn.getCurrencyPair());
*/
    	lastOrderId = -1;
    	load();
    }

//-------------------------------------------------------------------------------------
/*    public ShadowOrder(Debug _debug, Resolver _resolver, long _id, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type) {
		super(_debug, 
			_resolver, 
			_id, 
			_exchange, 
			_currencyPair,
			_time, 
			_price, 
			_amount, 
			_type);
    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER)
    				.share();
		setState(STATE_INIT);
    }

//-------------------------------------------------------------------------------------
    public ShadowOrder clone() {
		return new ShadowOrder(debug, 
			resolver, 
			id,
			exchange,
			currencyPair,
			time,
			price,
			amount,
			type);
    }*/

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void onNext(Order _order){
    	updateChanges(_order);
    	emitter.onNext(this);
    }
*/
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserOrderEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserOrderEvent> getOrderEvent() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public int getState() {
        return state;
    }

//-------------------------------------------------------------------------------------
    public void setState(int _state) {
        state = _state;
//        debug.outln(Debug.IMPORTANT3, "OrderX setstate="+_state);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized Response command(int _command, BigDecimal _price) {
//		if(orderExecutor.cancelOrder()){
//			return orderExecutor.placeOrder(_amount, _price);
//		}
        debug.outln(Debug.IMPORTANT3, "ShadowOrder _command= "+_command + ", _price=" + _price);
        if(orderOut == null){
        	return Response.RESPONSE_ERROR;
        }
    	Response _response = orderOut.command(_command, _price, amount);
    	switch(_response.getCode()){
    		case Response.RESPONSE_CODE_ERROR: {
//        		debug.outln(Debug.ERROR, "Unknown ERROR in ShadowOrder");
    			return _response;
    		}
    		case Response.RESPONSE_CODE_PLACED: 
    		case Response.RESPONSE_CODE_CANCELLED:
    		case Response.RESPONSE_CODE_ABANDONED: {
    			lastOrderId = _response.getId();
    			return _response;
    		}
    		case Response.RESPONSE_CODE_EXECUTED: {
    			BigDecimal _averagePrice = orderOut.getPrice();
    			price = _averagePrice;

    			BigDecimal _amountSpent = orderOut.getAmount();
    			amount = amount.subtract(_amountSpent);
    			
    			time = orderOut.getTime();
    			long _lastOrderId = orderOut.getOrderIds().get(0);
    			orderIn.addOrderId(_lastOrderId);
//    			List<Long> _tradeIds = rawTradesByExchangeAndCurrencyPair.getTradeIdsByOrderId(_lastOrderId);
//    			addTradeIds(_tradeIds);
    			if(amount.compareTo(BigDecimal.ZERO) <= 0){
    				orderOut.command(Command.COMMAND_CODE_STOP, null, null);
        			return Response.RESPONSE_EXECUTED;
    			} else {
    				orderOut.command(Command.COMMAND_CODE_RESET, null, null);
        			return Response.RESPONSE_PLACED;
    			}
//    			break;
    		}
    		default 							: {
        		debug.outln(Debug.ERROR, "Unknown return code in ShadowOrder:"+_response.getCode());
        		return Response.RESPONSE_ERROR;
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
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public synchronized boolean matchOrderId(long _orderId){
		return (lastOrderId == _orderId) || orderIn.containsOrderId(_orderId);
	}

//-------------------------------------------------------------------------------------
    public synchronized long getLastOrderId() {
    	return lastOrderId;
    }

//-------------------------------------------------------------------------------------
    public synchronized void updateWithTrade(AbstractTrade _trade){
    	debug.outln(Debug.IMPORTANT3, "ShadowOrder.updateWithTrade:" + _trade);
    	orderIn.updateWithTrade(_trade);
	}

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal getAlreadySpent() {
        return alreadySpent;
    }

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal addToAlreadySpent(BigDecimal _spent) {
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.0: ...alreadySpent="+alreadySpent+", _spent="+_spent);
        alreadySpent = alreadySpent.add(_spent);
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.1: ...alreadySpent="+alreadySpent);
        return alreadySpent;
    }


//-------------------------------------------------------------------------------------
    public synchronized BigDecimal getActualSpent() {
//		debug.outln(Debug.INFO, "OrderX.getActualSpent.0: ...amount="+amount+", state="+state);
/*		if(state != STATE_NOT_EXECUTED){
			return amount;
		} else {*/
			return BigDecimal.ZERO;
//		}
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isSpent() {
//		debug.outln(Debug.INFO, "OrderX.isSpent.0: ...alreadySpent="+alreadySpent+", amount="+amount);
        return alreadySpent.equals(amount);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
		setState(STATE_INIT);
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
		if(orderOut != null){
			orderOut.save();
		}
		orderOut = null;
	}

//-------------------------------------------------------------------------------------
    public String toString() {
		return "ShadowOrder: "+super.toString()+", "+state;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
/*

    	int _state = getState();
    	switch(){
			case STATE_NONE :{
				return false;
			}
			case STATE_INIT :{

			}
			case STATE_WAITING :{

			}
			case STATE_PARTIALLY_EXECUTED :{

			}
			case STATE_EXECUTED	:{

			}
			case STATE_STOPPED :{
				return false;
			}
			default :{
        		debug.outln(Debug.ERROR, "Unknown state in ShadowOrder: "+_state);
				return false;
			}
    	}

*/