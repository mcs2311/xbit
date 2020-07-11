//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.raworders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

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
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.users.level1.rawbalances.*;
import codex.xbit.api.server.trader.env.users.level1.rawtrades.*;
import codex.xbit.api.server.trader.env.users.level1.raworders.orderexecutor.*;

//-------------------------------------------------------------------------------------
public class RawOrder extends LinkableOrder implements FlowableOnSubscribe<AbstractOrder> {
	private int state;

	private OrderExecutor orderExecutor;

	private RawBalancesByExchange rawBalancesByExchange;
	private RawTradesByExchangeAndCurrencyPair rawTradesByExchangeAndCurrencyPair;

	//---cache:
//	private Response lastResponse;

    //---rx:
	private Flowable<AbstractOrder> flowable;
	private FlowableEmitter<AbstractOrder> emitter;

	//---statics:

	public static final int STATE_NONE 								= 0;
	public static final int STATE_INIT 								= 1;
	public static final int STATE_PLACED		 					= 2;
	public static final int STATE_EXECUTED		 					= 3;
	public static final int STATE_STOPPED		 					= 4;


//-------------------------------------------------------------------------------------
    public RawOrder(Debug _debug, Resolver _resolver, String _exchange, CurrencyPair _currencyPair, Order _order) {
    	this(_debug, 
    		_resolver, 
    		StringUtils.getLongNumber(_order.getId()),
    		_exchange,
    		_currencyPair,
    		_order.getTimestamp().getTime(),
    		_order.getAveragePrice(),
    		_order.getRemainingAmount(),
    		(_order.getType() == Order.OrderType.ASK) ? AbstractOrder.TYPE_BUY : AbstractOrder.TYPE_SELL,
    		AbstractOrder.POSITION_LONG
    		);
    }

//-------------------------------------------------------------------------------------
    public RawOrder(Debug _debug, Resolver _resolver, long _id, String _exchange, CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, int _position) {
    	super(_debug, _resolver, _id, _exchange, _currencyPair, _time, _price, _amount, _type, _position);
//        setState(STATE_NOT_EXECUTED);
//        debug.outln(Debug.IMPORTANT3, "OrderX state="+_state+", int="+state);
//    	lastResponse = Response.RESPONSE_ERROR;
    	setState(STATE_NONE);
    	load();
    }

//-------------------------------------------------------------------------------------
    public RawOrder(Debug _debug, Resolver _resolver, AbstractOrder _order) {
    	super(_debug, _resolver, _order);
//    	lastResponse = Response.RESPONSE_ERROR;
    	setState(STATE_NONE);
    	load();
    }

//-------------------------------------------------------------------------------------
    public RawOrder clone() {
		return new RawOrder(debug, 
			resolver, 
			id,
			exchange,
			currencyPair,
			time,
			price,
			amount,
			type,
			position);
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
    public synchronized Response command(int _command, BigDecimal _price, BigDecimal _amount) {
    	int _state = getState();
        debug.outln(Debug.IMPORTANT3, "RawOrder _command= "+_command + ", _state="+_state);
    	switch(_state){
    		case STATE_NONE: {
    			return Response.RESPONSE_ERROR;
    		}
    		case STATE_INIT: {
    			return ___state_INIT(_command, _price, _amount);
    		}
    		case STATE_PLACED: {
    			return ___state_PLACED(_command, _price, _amount);
    		}
    		case STATE_EXECUTED: {
    			return ___state_EXECUTED(_command, _price, _amount);
    		}
    		case STATE_STOPPED: {
    			return ___state_STOPPED(_command, _price, _amount);
    		}
    		default: {
    			debug.outln(Debug.ERROR, "Unknown state in RawOrder: " + state);
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
    public Response ___state_INIT(int _command, BigDecimal _price, BigDecimal _amount) {
		switch(_command){
			case Command.COMMAND_CODE_CANCEL: {
				return Response.RESPONSE_CANCELLED;
			}
			case Command.COMMAND_CODE_CHANGE: {
				return place(_price, _amount);
			}
			case Command.COMMAND_CODE_RESET: {
				return Response.RESPONSE_CANCELLED;
			}
			case Command.COMMAND_CODE_STOP: {
				setState(STATE_STOPPED);
				return Response.RESPONSE_ABANDONED;
			}
			default: {
				debug.outln(Debug.ERROR, "Unknown command in RawOrder: " + _command);
				return Response.RESPONSE_ERROR;
			}
		}
    }

//-------------------------------------------------------------------------------------
    public Response ___state_PLACED(int _command, BigDecimal _price, BigDecimal _amount) {
		switch(_command){
			case Command.COMMAND_CODE_CANCEL: {
				return cancel();
			}
			case Command.COMMAND_CODE_CHANGE: {
				Response _response = cancel();//.getCode();
				switch(_response.getCode()){
					case Response.RESPONSE_CODE_CANCELLED: {
						return place(_price, _amount);
					}
					case Response.RESPONSE_CODE_EXECUTED:{
						setState(STATE_INIT);
						return _response;
					}
					case Response.RESPONSE_CODE_ERROR: {
						setState(STATE_STOPPED);
						return _response;
					}
					default : {
						debug.outln(Debug.ERROR, "Unknown command in RawOrder.___state_PLACED: " + _command + ", _response="+_response);
						return _response;
					}
				}
			}
			case Command.COMMAND_CODE_RESET: {
				if(cancel().getCode() == Response.RESPONSE_CODE_CANCELLED){
					setState(STATE_INIT);
					return Response.RESPONSE_CANCELLED;
				}
//				setState(STATE_INIT);
				return Response.RESPONSE_ERROR;
			}
			case Command.COMMAND_CODE_STOP: {
				if(cancel().getCode() == Response.RESPONSE_CODE_CANCELLED){
					setState(STATE_STOPPED);
					return Response.RESPONSE_CANCELLED;
				}
				setState(STATE_STOPPED);
				return Response.RESPONSE_ERROR;
			}
			default: {
				debug.outln(Debug.ERROR, "Unknown command in RawOrder: " + _command);
				return Response.RESPONSE_ERROR;
			}
		}
	}

//-------------------------------------------------------------------------------------
    public Response ___state_EXECUTED(int _command, BigDecimal _price, BigDecimal _amount) {
		switch(_command){
			case Command.COMMAND_CODE_CANCEL: {
				return Response.RESPONSE_ERROR;
			}
			case Command.COMMAND_CODE_CHANGE: {
				return Response.RESPONSE_ERROR;
			}
			case Command.COMMAND_CODE_RESET: {
				setState(STATE_INIT);
				return Response.RESPONSE_CANCELLED;
			}
			case Command.COMMAND_CODE_STOP: {
				setState(STATE_STOPPED);
				return Response.RESPONSE_CANCELLED;
			}
			default: {
				debug.outln(Debug.ERROR, "Unknown command in RawOrder: " + _command);
				return Response.RESPONSE_ERROR;
			}
		}
	}


//-------------------------------------------------------------------------------------
    public Response ___state_STOPPED(int _command, BigDecimal _price, BigDecimal _amount) {
		switch(_command){
			case Command.COMMAND_CODE_CANCEL: {
				return Response.RESPONSE_CANCELLED;
			}
			case Command.COMMAND_CODE_CHANGE: {
				return Response.RESPONSE_ERROR;
			}
			case Command.COMMAND_CODE_RESET: {
				setState(STATE_INIT);
				return Response.RESPONSE_CANCELLED;
			}
			case Command.COMMAND_CODE_STOP: {
				setState(STATE_STOPPED);
				return Response.RESPONSE_CANCELLED;
			}
			default: {
				debug.outln(Debug.ERROR, "Unknown command in RawOrder: " + _command);
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
    public Response place(BigDecimal _price, BigDecimal _amount) {
		Response _response = orderExecutor.placeOrder(_price, _amount);
		switch(_response.getCode()){
			case Response.RESPONSE_CODE_PLACED: {
				setState(STATE_PLACED);
				return _response;
			}
			case Response.RESPONSE_CODE_ERROR: {
				if(_response.getId() == Response.ERROR_CODE_INSUFFICIENT_FUNDS){
					return _response;
				}
				_response = loadStatus();
				switch(_response.getCode()){
					case Response.RESPONSE_CODE_PLACED: {
						setState(STATE_PLACED);
						return Response.RESPONSE_PLACED;
					}
					case Response.RESPONSE_CODE_EXECUTED: {
						setState(STATE_EXECUTED);
						return Response.RESPONSE_EXECUTED;
					}
					case Response.RESPONSE_CODE_CANCELLED: {
						setState(STATE_INIT);
						return Response.RESPONSE_CANCELLED;
					}
					case Response.RESPONSE_CODE_ERROR: {
						setState(STATE_STOPPED);
						return Response.RESPONSE_ERROR;
					}
					default: {
						debug.outln(Debug.ERROR, "[Error 100]: Unknown state in RawOrder.place");
						return Response.RESPONSE_ERROR;
					}
				}
			}
			default: {
				debug.outln(Debug.ERROR, "[Error 101]: Unknown state in RawOrder.place");
				return Response.RESPONSE_ERROR;
			}
		}
	}

//-------------------------------------------------------------------------------------
    public Response cancel() {
    	Response _response = orderExecutor.cancelOrder();
		switch(_response.getCode()){
			case Response.RESPONSE_CODE_CANCELLED: {
				setState(STATE_INIT);
				return _response;
			}
			case Response.RESPONSE_CODE_ERROR: {
				_response = loadStatus();
				switch(_response.getCode()){
					case Response.RESPONSE_CODE_PLACED: {
						return cancel();
					}
					case Response.RESPONSE_CODE_EXECUTED: {
						return Response.RESPONSE_EXECUTED;
					}
					case Response.RESPONSE_CODE_CANCELLED: {
						setState(STATE_INIT);
						return _response;
					}
					case Response.RESPONSE_CODE_ERROR: {
						return Response.RESPONSE_ERROR;
					}
					default: {
						debug.outln(Debug.ERROR, "[Error 103]: Unknown state in RawOrder.cancel");
						return Response.RESPONSE_ERROR;
					}
				}
			}
			default: {
				debug.outln(Debug.ERROR, "[Error 104]: Unknown state in RawOrder.cancel");
				return Response.RESPONSE_ERROR;
			}
		}
    }

//-------------------------------------------------------------------------------------
    private Response loadStatus() {
    	LimitOrder _order = null;
    	int _counter = 0;
    	while(_order == null){
	    	_order = orderExecutor.getStatus();
	    	if(_counter > 100){
	    		return Response.RESPONSE_ERROR;
	    	}
	    	_counter++;
    	}
    	return adaptStatus(_order);
//		return Response.RESPONSE_ERROR;
	}

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
	public void subscribe(FlowableEmitter<AbstractOrder> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<AbstractOrder> getRawOrder() {
		return flowable;
    }



//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public RawOrder getOrderWithAmount(BigDecimal _amount) {
    	RawOrder _rawOrder = new RawOrder(debug, 
    			resolver, 
    			id,
    			exchange, 
    			System.currentTimeMillis(), 
    			decision, 
    			type);
    	return _order;
    }
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private Response adaptStatus(LimitOrder _order) {
		debug.outln("RawOrder.loadStatus.status=" + _order.getStatus());
		Order.OrderStatus _status = _order.getStatus();
		switch(_status) {
			case PENDING_NEW: {
				return Response.RESPONSE_PLACED;
			}
			case NEW: {
				return Response.RESPONSE_PLACED;
			}
			case CANCELED: 
			case PARTIALLY_FILLED:
			case FILLED: {
				BigDecimal _price = _order.getAveragePrice();
				if(_price == null){
					_price = _order.getLimitPrice();
				}
				if(_price != null){
					setPrice(_price);
				}

				BigDecimal _amount = _order.getCumulativeAmount();
				setAmount(_amount);
				
				String _idAsAString = _order.getId();
				long _id = StringUtils.getLongNumber(_idAsAString);
				setOrderId(_id);

				time = _order.getTimestamp().getTime();

				debug.outln("RawOrder.loadStatus[" + _idAsAString + "]: _price="+getPrice()+", _amount="+getAmount() + ", _status=" + _status);
//		    	Event _event = new Event(Event.ORDER_EXECUTED);
//		    	pushEvent(_event);
				rawBalancesByExchange.triggerBalanceCheck();
				rawTradesByExchangeAndCurrencyPair.triggerTradesCheck();
				if((_status == Order.OrderStatus.CANCELED) && (_amount.compareTo(BigDecimal.ZERO) == 0)){
					return Response.RESPONSE_CANCELLED;
				} else {
					return new Response(Response.RESPONSE_CODE_EXECUTED, _id);					
				}
			}
			case PENDING_CANCEL: {
				return Response.RESPONSE_ERROR;
			}
			case PENDING_REPLACE: {
				return Response.RESPONSE_ERROR;
			}
			case REPLACED: {
				return Response.RESPONSE_ERROR;
			}
			case STOPPED: 
			case REJECTED:
			case EXPIRED: {
				return Response.RESPONSE_CANCELLED;				
			}
			case UNKNOWN: {
				return Response.RESPONSE_ERROR;
			}
			default: {
    			debug.outln(Debug.ERROR, "Unknown order status for order on exchange: ");
				return Response.RESPONSE_PLACED;
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
        debug.outln(Debug.IMPORTANT3, "RawOrder created... "+this);
    	orderExecutor = new OrderExecutor(debug, resolver, this);

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);
		rawBalancesByExchange = resolver.getRawBalances().
				getRawBalancesByExchange(exchange);

		rawTradesByExchangeAndCurrencyPair = resolver.getRawTrades().
				getRawTradesByExchangeAndCurrencyPair(exchange, currencyPair);
/*    	_resolver.getUserExchanges().getExchange(_exchange.getShortName())
    			.getOrderChanges(_currencyPair)
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation())
    			.subscribe(_order -> onNext(_order),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());*/
    	setState(STATE_INIT);
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    	if(orderExecutor != null){
    		if(getState() == STATE_PLACED){
    			cancel();
    		}
    	}
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "RawOrder: "+super.toString();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
