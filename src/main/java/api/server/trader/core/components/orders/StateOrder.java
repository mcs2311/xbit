//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components.orders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.disposables.*;
import io.reactivex.schedulers.*;

import org.knowm.xchange.currency.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level3.agents.*;

//-------------------------------------------------------------------------------------
public class StateOrder extends LinkableOrder implements FlowableOnSubscribe<UserOrderEvent> {
	protected int state;
	protected List<Long> backwardPeers;
	protected List<Long> forwardPeers;

	//---shadow:
	protected BigDecimal amountConsumed, amountLocked;
	protected BigDecimal amountLockedInShadowBalance;	

	//---statics:

	public static final int STATE_NONE  				= 0;
	public static final int STATE_INIT  				= 1;
	public static final int STATE_WAITING_FOR_EXECUTION = 2;
	public static final int STATE_EXECUTED  			= 3;
	public static final int STATE_CANCELLED 			= 4;
	public static final int STATE_LOCKED				= 5;
	public static final int STATE_CONSUMED 				= 6;
	public static final int STATE_MANUAL_TRADE 			= 7;
	public static final int STATE_RESERVED 				= 100;

//	public static final int STATE_UNUSABLE = 5;
    //---rx:
	private Flowable<UserOrderEvent> flowable;
	private FlowableEmitter<UserOrderEvent> emitter;

//-------------------------------------------------------------------------------------
    public StateOrder(Debug _debug, Resolver _resolver, AbstractOrder _order) {
		this(_debug, 
			_resolver, 
			_order.getId(),
			_order.getExchange(), 
			_order.getCurrencyPair(),
			_order.getTime(), 
			_order.getPrice(), 
			_order.getAmount(), 
			_order.getType(),
			_order.getPosition());
    }

//-------------------------------------------------------------------------------------
    public StateOrder(Debug _debug, Resolver _resolver, AbstractTrade _trade) {
		this(_debug, 
			_resolver, 
			_trade.getId(),
			_trade.getExchange(), 
			_trade.getCurrencyPair(),
			_trade.getTime(), 
			_trade.getPrice(), 
			_trade.getAmount(), 
			_trade.getType(),
			AbstractOrder.POSITION_LONG);
		addOrderId(_trade.getOrderId());
    }

//-------------------------------------------------------------------------------------
    public StateOrder(Debug _debug, Resolver _resolver, long _id, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, int _position) {
    	this(_debug, _resolver,
    		_id, 
			_exchange, 
			_currencyPair,
			_time, 
			_price, 
			_amount, 
			_type,
			_position, 
			STATE_INIT,
			null,
			null,
			null,
			null);
	}

//-------------------------------------------------------------------------------------
    public StateOrder(Debug _debug, Resolver _resolver, String _id, String _time, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, String _price, String _amount, String _type, String _position, String _state, String _backwardPeers, String  _forwardPeers, String _tradeIds, String  _orderIds) {
//		List<Integer> _backwardPeersList = StringUtils.convertStringToIntegerList(_backwardPeers);
//		List<Integer> _forwardPeersList = StringUtils.convertStringToIntegerList(_forwardPeers);
		this(_debug, 
			_resolver, 
			_id, 
			_time, 
			_exchange,
			_currencyPair, 
			_price, 
			_amount, 
			convertTypeFromString(_type), 
			convertPositionFromString(_position), 
			_state,
			StringUtils.convertStringToLongList(_backwardPeers),  
			StringUtils.convertStringToLongList(_forwardPeers),
			StringUtils.convertStringToLongList(_tradeIds),  
			StringUtils.convertStringToLongList(_orderIds));
    }

//-------------------------------------------------------------------------------------
    public StateOrder(Debug _debug, Resolver _resolver, String _id, String _time, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, String _price, String _amount, int _type, int _position, String _state, List<Long> _backwardPeers, List<Long>  _forwardPeers, List<Long> _tradeIds, List<Long>  _orderIds) {
		this(_debug, 
			_resolver, 
			StringUtils.getLongNumber(_id), 
			_exchange, 
			_currencyPair,
			Long.valueOf(_time), 
			new BigDecimal(_price), 
			new BigDecimal(_amount), 
			_type,
			_position,
			convertStateFromString(_state),
			_backwardPeers,  
			_forwardPeers,
			_tradeIds,
			_orderIds);
    }

//-------------------------------------------------------------------------------------
    public StateOrder(Debug _debug, Resolver _resolver, long _id, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, int _position, int _state, List<Long> _backwardPeers, List<Long>  _forwardPeers, List<Long> _tradeIds, List<Long> _orderIds) {
    	super(_debug, _resolver,
    		_id, 
			_exchange, 
			_currencyPair,
			_time, 
			_price, 
			_amount, 
			_type,
			_position);

        setState(_state);
        backwardPeers = (_backwardPeers == null) ? (new ArrayList<Long>()) : _backwardPeers;
        forwardPeers = (_forwardPeers == null) ? (new ArrayList<Long>()) : _forwardPeers;

        tradeIds = (_tradeIds == null) ? (new ArrayList<Long>()) : _tradeIds;
        orderIds = (_orderIds == null) ? (new ArrayList<Long>()) : _orderIds;
//        debug.outln(Debug.IMPORTANT3, "OrderX state="+_state+", int="+state);

//        agent = new Agent(debug, resolver, currencyPair, _type, tacticConfiguration);

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);
//    				.share();

        amountConsumed = BigDecimal.ZERO;
        amountLocked = BigDecimal.ZERO;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
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
    public int getState() {
        return state;
    }

//-------------------------------------------------------------------------------------
    public void setState(String _state) {
        setState(convertStateFromString(_state));
    }

//-------------------------------------------------------------------------------------
    public void setState(int _state) {
    	int _previousState = state;
        state = _state;
//        debug.outln(Debug.IMPORTANT3, "OrderX setstate="+_state);
        if(emitter != null){
        	debug.outln(Debug.IMPORTANT3, "OrderX.StateOrder setstate="+_state);
        	emitter.onNext(new UserOrderEvent(UserOrderEvent.ORDER_UPDATE, _previousState, _state));        		
        }
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isExecuted() {
		return (state == STATE_EXECUTED) ||
				(state == STATE_MANUAL_TRADE);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isTemporary() {
		return (state == STATE_WAITING_FOR_EXECUTION) ||
				(state == STATE_CANCELLED);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isOpenDeal() {
		return ((getType() == TYPE_BUY) &&
				(state != STATE_CANCELLED) && 
				(state != STATE_CONSUMED));
	}

//-------------------------------------------------------------------------------------
    public String getStateAsString() {
        return convertStateFromInt(state);
    }

//-------------------------------------------------------------------------------------
	public synchronized boolean attemptToCancel(){
		if((state != STATE_CANCELLED) || (state != STATE_CONSUMED)){
			state = STATE_CANCELLED;
			return true;
		} else {
			return false;
		}
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized BigDecimal getAmountConsumed() {
        return amountConsumed;
    }

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal setAmountConsumed(BigDecimal _consumed) {
    	amountConsumed = _consumed;
    	checkState();
//		if(amountConsumed.compareTo(amount) >= 0){
//			setState(STATE_CONSUMED);
//		}
		return amountConsumed;
    }

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal addToConsumed(BigDecimal _consumed) {
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.0: ...alreadySpent="+alreadySpent+", _spent="+_spent);
        return setAmountConsumed(amountConsumed.add(_consumed));
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.1: ...alreadySpent="+alreadySpent);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isConsumed() {
//		debug.outln(Debug.INFO, "OrderX.isSpent.0: ...alreadySpent="+alreadySpent+", amount="+amount);
    	return (state == STATE_CONSUMED);// || amountConsumed > 
//        return ;
    }

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal getAmountLocked() {
        return amountLocked;
    }

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal setAmountLocked(BigDecimal _locked) {
    	int _evaluateLocked = _locked.compareTo(amount.subtract(amountConsumed));
		if(_evaluateLocked <= 0){
    		amountLocked = _locked;
    		checkState();
			return amountLocked;
		} else if(_evaluateLocked > 0){
			return null; //ERROR!
		}
		return null;
    }

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal addToLocked(BigDecimal _locked) {
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.0: ...alreadySpent="+alreadySpent+", _spent="+_spent);
        return setAmountLocked(amountLocked.add(_locked));
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.1: ...alreadySpent="+alreadySpent);
    }

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal removeFromLocked(BigDecimal _locked) {
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.0: ...alreadySpent="+alreadySpent+", _spent="+_spent);
        return setAmountLocked(amountLocked.subtract(_locked));
//		debug.outln(Debug.INFO, "OrderX.addToAlreadySpent.1: ...alreadySpent="+alreadySpent);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isLocked() {
//		debug.outln(Debug.INFO, "OrderX.isSpent.0: ...alreadySpent="+alreadySpent+", amount="+amount);
        return (state == STATE_LOCKED);//amountLocked.equals(amount);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean isGoodForTrading() {
//		return (isExecuted() && !isConsumed() && !isLocked());
		return isExecuted();// && !isConsumed() && !isLocked());
	}

//-------------------------------------------------------------------------------------
    public synchronized BigDecimal getAvailableToConsume() {
    	BigDecimal _availableToConsume = amount.subtract(amountConsumed).subtract(amountLocked);
		debug.outln(Debug.INFO, "getAvailableToConsume order#"+getId()+":amount=" + amount + ", state=" + state + ", amountConsumed="+amountConsumed+", amountLocked="+amountLocked + ", _availableToConsume=" + _availableToConsume);
    	return _availableToConsume;
  }

//-------------------------------------------------------------------------------------
    public synchronized void checkState() {
    	if(amountConsumed.compareTo(amount) >= 0){
    		setState(STATE_CONSUMED);
    	} else if(amountLocked.compareTo(amount) >= 0){
    		setState(STATE_LOCKED);
    	} else if(isConsumed() || isLocked()){
    		setState(STATE_EXECUTED);
    	}
    }

//-------------------------------------------------------------------------------------
	public void unlockParentOrder(){
		StateOrder _parentOrder = (StateOrder)getParentOrder();
		if(_parentOrder != null){
			_parentOrder.removeFromLocked(getAmount());
		}
	}

//-------------------------------------------------------------------------------------
	public void setExecuted(){
		if((getType() == TYPE_SELL) && (getPosition() == POSITION_LONG)){
			setState(STATE_CONSUMED);
		} else {
			setState(STATE_EXECUTED);
		}
	}
//-------------------------------------------------------------------------------------
	public void setAmountLockedInShadowBalance(BigDecimal _amountLockedInShadowBalance){
		amountLockedInShadowBalance = _amountLockedInShadowBalance;
	}

//-------------------------------------------------------------------------------------
	public BigDecimal getAmountLockedInShadowBalance(){
		return amountLockedInShadowBalance;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<Long> getBackwardPeers() {
        return backwardPeers;
    }

//-------------------------------------------------------------------------------------
    public String getBackwardPeersAsString() {
        return StringUtils.convertLongListToString(backwardPeers);
    }

//-------------------------------------------------------------------------------------
    public void addBackwardPeer(long _id) {
        backwardPeers.add(_id);
    }

//-------------------------------------------------------------------------------------
    public void setBackwardPeer(long _id) {
        backwardPeers.clear();
        addBackwardPeer(_id);
    }

//-------------------------------------------------------------------------------------
    public void removeBackwardPeer(long _id) {
        backwardPeers.remove((Long)_id);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<Long> getForwardPeers() {
        return forwardPeers;
    }

//-------------------------------------------------------------------------------------
    public String getForwardPeersAsString() {
        return StringUtils.convertLongListToString(forwardPeers);
    }
//-------------------------------------------------------------------------------------
    public void addForwardPeer(long _id) {
        forwardPeers.add(_id);
    }

//-------------------------------------------------------------------------------------
    public void setForwardPeer(long _id) {
        forwardPeers.clear();
        addForwardPeer(_id);
    }

//-------------------------------------------------------------------------------------
    public void removeForwardPeer(long _id) {
		debug.outln(Debug.INFO, "OrderX.removeForwardPeer...0 _id="+_id+", fp="+forwardPeers);
        forwardPeers.remove((Long)_id);
		debug.outln(Debug.INFO, "OrderX.removeForwardPeer...1 _id="+_id+", fp="+forwardPeers);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public static int convertStateFromString(String _state) {
        switch(_state){
        	case "NON": return STATE_NONE;  				
        	case "INI": return STATE_INIT;  				
        	case "WFE": return STATE_WAITING_FOR_EXECUTION;
        	case "EXE": return STATE_EXECUTED;
        	case "CAN": return STATE_CANCELLED;
        	case "LOK": return STATE_LOCKED;
        	case "CON": return STATE_CONSUMED;
        	case "MTR": return STATE_MANUAL_TRADE;
        	case "RES": return STATE_RESERVED;
        	default: return -1;
        }
    }

//-------------------------------------------------------------------------------------
    public static String convertStateFromInt(int _state) {
        switch(_state){
        	case STATE_NONE  					: return "NON";
        	case STATE_INIT  					: return "INI";
        	case STATE_WAITING_FOR_EXECUTION	: return "WFE";
        	case STATE_EXECUTED 				: return "EXE";
        	case STATE_CANCELLED 				: return "CAN";
        	case STATE_LOCKED 					: return "LOK";
        	case STATE_CONSUMED 				: return "CON";
        	case STATE_MANUAL_TRADE				: return "MTR";
        	case STATE_RESERVED					: return "RES";
        	default: return "ERR";
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return super.toString() +",state="+getStateAsString()+", "+getBackwardPeersAsString()+", "+getForwardPeersAsString()+", "+type;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
