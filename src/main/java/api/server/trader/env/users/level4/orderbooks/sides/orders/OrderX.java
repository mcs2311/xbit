	//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders;
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
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;

import codex.xbit.api.server.trader.env.users.level3.agents.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.*;

//-------------------------------------------------------------------------------------
public class OrderX extends StateOrder {
	private TacticConfiguration tacticConfiguration;
	private DecisionChain decisionChain;

	//---cache:

//-------------------------------------------------------------------------------------
    public OrderX(Debug _debug, Resolver _resolver, AbstractOrder _order) {
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
    public OrderX(Debug _debug, Resolver _resolver, AbstractTrade _trade) {
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
		addTradeId(_trade.getId());
    }

//-------------------------------------------------------------------------------------
    public OrderX(Debug _debug, Resolver _resolver, long _id, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, int _position) {
    	this(_debug, 
    		_resolver,
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
    public OrderX(Debug _debug, Resolver _resolver, String _id, String _time, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, String _price, String _amount, String _type, String _position, String _state, String _backwardPeers, String  _forwardPeers, String _tradeIds, String  _orderIds) {
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
    public OrderX(Debug _debug, Resolver _resolver, String _id, String _time, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, String _price, String _amount, int _type, int _position, String _state, List<Long> _backwardPeers, List<Long>  _forwardPeers, List<Long> _tradeIds, List<Long>  _orderIds) {
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
    public OrderX(Debug _debug, Resolver _resolver, long _id, String _exchange, org.knowm.xchange.currency.CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, int _position, int _state, List<Long> _backwardPeers, List<Long>  _forwardPeers, List<Long> _tradeIds, List<Long> _orderIds) {
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

        amountConsumed = BigDecimal.ZERO;
        amountLocked = BigDecimal.ZERO;
    }

//-------------------------------------------------------------------------------------
    public OrderX clone() {
		return new OrderX(debug, 
			resolver, 
			id,
			exchange,
			currencyPair,
			time,
			price,
			amount,
			type,
			position,
			state,
			backwardPeers,
			forwardPeers,
			tradeIds,
			orderIds);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void initiateDecisionChain(Debug _debug, MarketModel _marketModel, Scales _scales, TacticConfiguration _tacticConfiguration){
		decisionChain = new DecisionChain(_debug, resolver, type, _marketModel, _scales, _tacticConfiguration, this);
	}

//-------------------------------------------------------------------------------------
    public TacticConfiguration getTacticConfiguration() {
        return tacticConfiguration;
    }

//-------------------------------------------------------------------------------------
    public void setTacticConfiguration(TacticConfiguration _tacticConfiguration) {
        tacticConfiguration = _tacticConfiguration;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public boolean shallIBuy(Event _event){
    	return decisionChain.shallIBuy(_event);
    }

//-------------------------------------------------------------------------------------
    public boolean shallISell(Event _event){
    	return decisionChain.shallISell(_event);
    }

//-------------------------------------------------------------------------------------
    public Decision recalculate(Event _event){
    	return decisionChain.recalculate(_event);
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public OrderX getOrderWithAmount(BigDecimal _amount) {
    	OrderX _order = new OrderX(debug, 
    			resolver, 
    			id,
    			exchange, 
    			currencyPair,
    			System.currentTimeMillis(), 
    			price, 
    			_amount, 
    			type,
    			position,
    			STATE_INIT,
    			new ArrayList<Long>(backwardPeers), 
    			new ArrayList<Long>(forwardPeers),
    			null,
    			null);
    	_order.setParentOrder(this);
    	return _order;
    }

//-------------------------------------------------------------------------------------
    public OrderX getOrderWithReverseType() {
    	ArrayList<Long> _backwardPeers = new ArrayList<Long>(Arrays.asList(id));
    	OrderX _order = new OrderX(debug, 
    			resolver, 
    			id,
    			exchange, 
    			currencyPair,
    			System.currentTimeMillis(), 
    			price, 
    			amount, 
    			(type == TYPE_SELL) ? TYPE_BUY : TYPE_SELL,
    			position,
    			STATE_INIT,
    			_backwardPeers, 
    			null,
    			null,
    			null);
    	_order.setParentOrder(this);
    	return _order;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------


//-------------------------------------------------------------------------------------
	public List<String> getInfo(){
        List<String> _list = new ArrayList<String>();
		_list.add(toString());
        return _list;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
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
		return super.toString() + "";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
