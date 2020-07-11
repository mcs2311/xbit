//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level3.agents;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;

import codex.common.utils.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;

//-------------------------------------------------------------------------------------
public class Agents extends AbstractCluster<Configuration, Agent> {
    
    //---cache:

    //---statics:


//-------------------------------------------------------------------------------------
    public Agents(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
//    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private synchronized void addAgent(OrderX _order){
    	String _exchange = _order.getExchange();
    	CurrencyPair _currencyPair = _order.getCurrencyPair();
    	Key _key = new Key(_exchange, _currencyPair, _order.getId());
    	Agent _agent = getEntity(_key);
    	if(_agent != null){
			debug.outln(Debug.ERROR, "Agents: duplicate agent with "+_order.getExchange()+","+_order.getCurrencyPair()+"," +_order.getId()+"...");
    	} else {
    		int _index = size();
    		String _name0 = _exchange + "-" + _currencyPair.toString() + "-A" + _index;
    		Debug _debug0 = new Debug(debug, _name0, Debug.IMPORTANT3);
    		_agent = new Agent(_debug0, resolver, _order);
    		addEntity(_key, _agent);    		
    	}
    }
/*
//-------------------------------------------------------------------------------------
    private List<Agent> getAgents(String _exchange, CurrencyPair _currencyPair){
    	Key _key = new Key(_exchange, _currencyPair);
    	return getEntities(_key);
    }
*/

//-------------------------------------------------------------------------------------
    private synchronized void removeAgent(OrderX _order){
    	Key _key = new Key(_order.getExchange(), _order.getCurrencyPair(), _order.getId());
    	Agent _agent = getEntity(_key);
    	if(_agent == null){
			debug.outln(Debug.ERROR, "Agents: agent missing with "+_order.getExchange()+","+_order.getCurrencyPair()+"," +_order.getId()+"...");
    	} else {
    		_agent.save();
    		removeEntity(_key);
    	}
    }
/*
//-------------------------------------------------------------------------------------
    public int getCountWithAgentsInOrderingState(String _exchange, CurrencyPair _currencyPair){
    	List<Agent> _agents = getAgents(_exchange, _currencyPair);
    	int _counter = 0;
    	for (int i = 0; i < _agents.size() ; i++) {
    		Agent _agent = _agents.get(i);
    		if(_agent.isOrdering()){
    			_counter++;
    		}
    	}
    	return _counter;
    }
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(UserOrderbookEvent _userOrderbookEvent){
    	OrderX _order = (OrderX)_userOrderbookEvent.getOrder();
    	switch(_userOrderbookEvent.getType()){
    		case UserOrderbookEvent.ORDER_ADDED: {
    			if(_order.getState() == OrderX.STATE_WAITING_FOR_EXECUTION){
    				addAgent(_order);
    			}
    			break;
    		} 
    		case UserOrderbookEvent.ORDER_REMOVED: {
				debug.outln(Debug.WARNING, "Agents.Remomoving agent with order:" + _order);
    			if(_order.getState() == OrderX.STATE_CANCELLED){
    				removeAgent(_order);
    			}
    			break;
    		} 
			default: {
				debug.outln(Debug.ERROR, "Unknown event in UserOrderbookEvent: " + _userOrderbookEvent.getName());
				break;
			}
		}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
				resolver.getOrderbooks()
				.getOrderbookEvent()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.newThread(), false)
    			.subscribe(_userOrderbookEvent -> onNext(_userOrderbookEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());



    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Agents:";
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
