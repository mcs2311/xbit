//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level3.agents;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

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


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;
import codex.xbit.api.server.trader.env.users.level2.shadowbalances.*;
import codex.xbit.api.server.trader.env.users.level2.shadoworders.*;
//import codex.xbit.api.server.trader.env.users.level3.agents.helpers.pricemanager.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;


//-------------------------------------------------------------------------------------
public class Agent extends AbstractEntity<Configuration> implements Runnable {
//	private LinkedTransferQueue<Event> queue;
	private Event event;

	private OrderX orderIn;
	private ShadowOrder orderOut;

    private int state;
    private MarketModel marketModel;
	private ShadowBalance shadowBalanceBase, shadowBalanceCounter;

//	private int notEnoughFundsCounter;

    //---cache:
	private TraderConfiguration traderConfiguration;

//    private Object oLock = new Object();
    private Thread thisThread;

	//---statics:
	public static final int STATE_INIT 							= 0;
	public static final int STATE_NOT_ENOUGH_FUNDS 				= 1;
	public static final int STATE_ORDERING 						= 3;
	public static final int STATE_STOPPED		 				= 4;
//	public static final int STATE_EXECUTED 						= 3;

//-------------------------------------------------------------------------------------
    public Agent(Debug _debug, Resolver _resolver, OrderX _orderIn) {
    	super(_debug, _resolver, null, null);
    	orderIn = _orderIn;
    	orderOut = new ShadowOrder(_debug, _resolver, _orderIn);
    	resolver.getShadowOrders()
    		.addOrder(orderOut);
//		queue = new LinkedTransferQueue<Event>();
		traderConfiguration = _resolver.getTraderConfiguration();

    	debug.outln(Debug.IMPORTANT3, "Agent created for order..." + _orderIn);
//    	notEnoughFundsCounter = 0;
    	setState(STATE_INIT);
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private synchronized boolean isActive(){
		return ((state != STATE_INIT) &&
			(state != STATE_STOPPED));
	}

//-------------------------------------------------------------------------------------
	private synchronized boolean isNotStopped(){
		return (state != STATE_STOPPED);
	}

//-------------------------------------------------------------------------------------
	public synchronized boolean isOrdering(){
		return (state == STATE_ORDERING);
	}

//-------------------------------------------------------------------------------------
	private synchronized int getState(){
		return state;
	}

//-------------------------------------------------------------------------------------
	private synchronized void setState(int _state){
		state = _state;
	}

//-------------------------------------------------------------------------------------
	private synchronized String getStateAsString(){
		return convertStateToString(state);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNextMarketModelEvent(MarketModelEvent _marketModelEvent){
//		debug.outln(Debug.INFO, "Broker _eventId="+_event.getId());
//    	queue.put(_marketModelEvent);
    	setEvent(_marketModelEvent);
    }
/*
//-------------------------------------------------------------------------------------
    public void onNextOrderIn(UserOrderEvent _userOrderEvent){
//		debug.outln(Debug.INFO, "Broker _eventId="+_event.getId());
    	queue.put(_userOrderEvent);
//    	notifiAll();????
    }

//-------------------------------------------------------------------------------------
    public void onNextOrderOut(UserOrderEvent _userOrderEvent){
//		debug.outln(Debug.INFO, "Broker _eventId="+_event.getId());
    	queue.put(_userOrderEvent);
//    	notifiAll();????
    }

//-------------------------------------------------------------------------------------
    public void onBalanceEvent(UserBalanceEvent _userBalanceEvent){
//		debug.outln(Debug.INFO, "Broker _eventId="+_event.getId());
    	queue.put(_userBalanceEvent);
//    	notifiAll();????
    }
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
    	try{
	        while(isNotStopped()){
	        	process();
	        }
    	}catch(Throwable _t){
    		onError(_t);
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private void process(){
//		debug.outln(Debug.INFO, "Agent.process...state=");
		Event _event = getEvent();
		processEvent(_event);
//		setEvent(event);
//		debug.outln(Debug.INFO, "Agent.process...exit");
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private synchronized void setEvent(Event _event){
    	event = _event;
    	notify();
    }

//-------------------------------------------------------------------------------------
    private synchronized Event getEvent(){
		while(event == null){
			try{
				wait();
			} catch(InterruptedException _e){

			}
		}
		Event _event = event;
		event = null;
    	return _event;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private synchronized void processEvent(Event _event){
		debug.outln(Debug.IMPORTANT4, "Agent.processEvent state=" + getStateAsString() + ", _event="+_event);
		MarketModelEvent _marketModelEvent = (MarketModelEvent)_event;
    	switch(state){
    		case STATE_INIT: {
    			break;
    		}
    		case STATE_NOT_ENOUGH_FUNDS: {
    			if(lockIfAvailable()){
					setState(STATE_ORDERING);
    			} else {
					setState(STATE_STOPPED);
    				orderIn.unlockParentOrder();
    			}
    			break;
    		}
    		case STATE_ORDERING: {
/*    			if(!checkForFunds()){
    				break;
    			}*/
    			if(_marketModelEvent == null){
    				break;
    			}
//				notEnoughFundsCounter = 0;
				Decision _decision = orderIn.recalculate(_marketModelEvent);
				Response _response = processDecision(_decision);
				switch(_response.getCode()){
					case Response.RESPONSE_CODE_ABANDONED:
					case Response.RESPONSE_CODE_ERROR:{
						setState(STATE_STOPPED);
						unlockAfterOrderComplete();
	    				orderIn.unlockParentOrder();
						break;
					}
					case Response.RESPONSE_CODE_PLACED:
					case Response.RESPONSE_CODE_CANCELLED: {
						break;
					}
					case Response.RESPONSE_CODE_EXECUTED:{
						setState(STATE_STOPPED);
						debug.outln(Debug.IMPORTANT4, "Agent.processEvent executed!!!: _price=" + orderOut.getPrice());
						orderIn.setPrice(orderOut.getPrice());
						unlockAfterOrderComplete();
	//					orderIn.setTradeIds(orderOut.getTradeIds());
	//					orderIn.setOrderIds(orderOut.getOrderIds());
			//    				orderIn.setAmount(orderOut.getAmount());
						orderIn.setTime(orderOut.getTime());
						orderIn.setExecuted();
//						orderIn.setState(OrderX.STATE_EXECUTED);
	//		debug.outln(Debug.INFO, "SchedulerInputQueue.processCompletedOrder: ...1.._completedOrder=" + _completedOrder);


						StateOrder _parentOrder = (StateOrder)orderIn.getParentOrder();
						if(_parentOrder != null){
							_parentOrder.addToConsumed(orderIn.getAmount());
//							_parentOrder.removeFromLocked(orderIn.getAmount());
						}
	    				orderIn.unlockParentOrder();
						save();
						break;
					}
					default:{
						setState(STATE_STOPPED);
						debug.outln(Debug.ERROR, "Agent. Unknown response: " + _response.getCode());
						break;
					}
				}
				break;
    		}
    		case STATE_STOPPED: {
				orderIn.setState(OrderX.STATE_CANCELLED);
				StateOrder _parentOrder = (StateOrder)orderIn.getParentOrder();
				if(_parentOrder != null){
					_parentOrder.removeFromLocked(orderIn.getAmount());
				}
				save();
    			break;
    		}
    		default: {
    			debug.outln(Debug.ERROR, "Unknown kind in Agent.processEvent_MARKETMODEL:"+state);
    			break;
    		}
    	}
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private synchronized boolean lockIfAvailable() {
		BigDecimal _orderAmount = orderIn.getAmount();
		if(orderIn.getType() == AbstractOrder.TYPE_SELL){
//			BigDecimal _baseAmount = shadowBalanceBase.getAvailable();
			boolean _success = shadowBalanceBase.lock(_orderAmount);
			if(_success){
				orderIn.setAmountLockedInShadowBalance(_orderAmount);
			}
			return _success;
//					debug.outln(Debug.IMPORTANT4, "Agent.processEvent_BALANCE.0 _amount="+orderIn.getAmount()+", _baseAmount=" + _baseAmount);
		} else {
			_orderAmount = marketModel.getQuote(_orderAmount);
//			BigDecimal _counterAmount = shadowBalanceCounter.getAvailable();
//					debug.outln(Debug.IMPORTANT4, "Agent.processEvent_BALANCE.1 _amount="+orderIn.getAmount()+", _orderAmount=" + _orderAmount+" , _counterAmount="+_counterAmount);
			boolean _success = shadowBalanceCounter.lock(_orderAmount);
			if(_success){
				orderIn.setAmountLockedInShadowBalance(_orderAmount);
			}
			return _success;			
		}
	}

//-------------------------------------------------------------------------------------
	private synchronized boolean unlockAfterOrderComplete() {
		debug.outln(Debug.IMPORTANT4, "Agent.unlockAfterOrderComplete.0:" + orderIn);
		BigDecimal _orderAmount = orderIn.getAmountLockedInShadowBalance();
		if(orderIn.getType() == AbstractOrder.TYPE_SELL){
//			BigDecimal _orderAmount = orderIn.getAmount();
//			BigDecimal _baseAmount = shadowBalanceBase.getAvailable();
			debug.outln(Debug.IMPORTANT4, "Agent.unlockAfterOrderComplete.1:" + _orderAmount);
			return shadowBalanceBase.unlock(_orderAmount);
//					debug.outln(Debug.IMPORTANT4, "Agent.processEvent_BALANCE.0 _amount="+orderIn.getAmount()+", _baseAmount=" + _baseAmount);
		} else {
//			BigDecimal _orderAmount = orderIn.getAmount().multiply(orderIn.getPrice());
//			BigDecimal _counterAmount = shadowBalanceCounter.getAvailable();
//					debug.outln(Debug.IMPORTANT4, "Agent.processEvent_BALANCE.1 _amount="+orderIn.getAmount()+", _orderAmount=" + _orderAmount+" , _counterAmount="+_counterAmount);
			debug.outln(Debug.IMPORTANT4, "Agent.unlockAfterOrderComplete.2:" + _orderAmount);
			return shadowBalanceCounter.unlock(_orderAmount);
		}
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private Response processDecision(Decision _decision) {
		debug.outln(Debug.ERROR, "Jumper processDecision _decision="+_decision);
    	if(_decision == null){
    		return Response.RESPONSE_ERROR;
    	}
    	int _decisionCode = _decision.getDecision();
    	switch(_decisionCode){
    		case Decision.DECISION_CHANGE_ORDER: {
    			return processDecision_CHANGE_ORDER(_decision);
    		}
    		case Decision.DECISION_DONT_TOUCH_ORDER: {
    			return processDecision_DONT_TOUCH_ORDER(_decision);
    		}
    		case Decision.DECISION_ABANDON_ORDER: {
    			return processDecision_ABANDON_ORDER(_decision);
    		}
    		default: {
				debug.outln(Debug.ERROR, "Unknown decision=" + _decisionCode);
    			return Response.RESPONSE_ERROR;
    		}
    	}
    }

//-------------------------------------------------------------------------------------
	private Response processDecision_CHANGE_ORDER(Decision _decision) {
		BigDecimal _price = _decision.getPrice();
		debug.outln(Debug.INFO, "Agent-to-shadowOrder.... processDecision  _price="+_price);
//    		agent.execute();
		int _traderState = traderConfiguration.getState();
		switch(_traderState){
			case TraderConfiguration.STATE_INIT  : {
				return Response.RESPONSE_ERROR;
			}
			case TraderConfiguration.STATE_NORMAL: {
				return orderOut.command(Command.COMMAND_CODE_CHANGE, _price);
			}
			case TraderConfiguration.STATE_PAUSED: {
				return Response.RESPONSE_PLACED;
			}
			case TraderConfiguration.STATE_PANIC : {
				if(orderIn.getType() == AbstractOrder.TYPE_SELL){
					return orderOut.command(Command.COMMAND_CODE_CHANGE, _price);
				} else if(orderIn.getType() == AbstractOrder.TYPE_BUY){
					return orderOut.command(Command.COMMAND_CODE_CANCEL, null);
				}
			}
			case TraderConfiguration.STATE_BEAR  : {
				return orderOut.command(Command.COMMAND_CODE_CHANGE, _price);
			}
			case TraderConfiguration.STATE_BULL  : {
				return orderOut.command(Command.COMMAND_CODE_CHANGE, _price);
			}
			default : {
				debug.outln(Debug.INFO, "Unknown trader state in processDecision:" + _traderState);
				return Response.RESPONSE_ERROR;
			}
		}
	}

//-------------------------------------------------------------------------------------
	private Response processDecision_DONT_TOUCH_ORDER(Decision _decision) {
		return Response.RESPONSE_PLACED;
	}

//-------------------------------------------------------------------------------------
	private Response processDecision_ABANDON_ORDER(Decision _decision) {
		return orderOut.command(Command.COMMAND_CODE_STOP, null);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized void load() {
    	super.load();
    	String _exchange = orderIn.getExchange();
    	CurrencyPair _currencyPair = orderIn.getCurrencyPair();
    	UserExchange _userExchange = resolver.getUserExchanges()
    			.getUserExchange(orderIn.getExchange());

    	shadowBalanceBase = resolver.getShadowBalances()
    			.getShadowBalance(_exchange, _currencyPair.base);
/*
		shadowBalanceBase.getShadowBalance()
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.computation(), false)
    			.subscribe(_userBalanceEvent -> onBalanceEvent(_userBalanceEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
*/
    	shadowBalanceCounter = resolver.getShadowBalances()
    			.getShadowBalance(_exchange, _currencyPair.counter);

/*
		shadowBalanceCounter.getShadowBalance()
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.computation(), false)
    			.subscribe(_userBalanceEvent -> onBalanceEvent(_userBalanceEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
*/
//    	scales = _userExchange.getScales(orderIn.getCurrencyPair());

    	MarketModels _marketModels = resolver.getMarketModels();
    	marketModel = _marketModels.getMarketModel(_currencyPair);
				marketModel.getMarketModelEvent()
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.newThread(), false)
    			.subscribe(_marketModelEvent -> onNextMarketModelEvent(_marketModelEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

/*
        if(orderIn.getType() == OrderX.TYPE_BUY){
			priceManager = new BidPriceManager(debug, resolver, marketModel, scales, orderIn.getTacticConfiguration(), orderIn, (float)0.10);
        } else {
			priceManager = new AskPriceManager(debug, resolver, marketModel, scales, orderIn.getTacticConfiguration(), orderIn, (float)0.10);        	
        }
        
        priceManager.setOrder(orderIn);
*/        
/*
    	orderIn.getOrderEvent()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_userOrderEvent -> onNextOrderIn(_userOrderEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

    	orderOut.getOrderEvent()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_userOrderEvent -> onNextOrderOut(_userOrderEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
*/
		setState(STATE_NOT_ENOUGH_FUNDS);
		thisThread = new Thread(this);
		thisThread.start();
		processEvent(null);
	}

//-------------------------------------------------------------------------------------
    public void save() {
//		debug.outln(Debug.IMPORTANT3, "Agent save...");
    	orderIn.unlockParentOrder();

    	super.save();
    	if(isActive()){
    		if(orderOut != null){
				orderOut.save();
    		}
			orderOut = null;
		}
		setState(STATE_STOPPED);
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}
    }


//-------------------------------------------------------------------------------------
	private String convertStateToString(int _state){
		switch(_state){
//			case STATE_INIT 			: return "INIT";
			case STATE_NOT_ENOUGH_FUNDS : return "NOT_ENOUGH_FUNDS";
			case STATE_ORDERING 		: return "ORDERING";
//			case STATE_EXECUTED 		: return "EXECUTED";
			case STATE_STOPPED		 	: return "STOPPED";
			default 					: return "UNKNOWN"; 
		}
	}

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Agent:" + orderIn + ", " + orderOut;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------