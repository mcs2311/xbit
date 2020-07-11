//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.scheduler;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.Order;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;


import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.scheduler.*;

//-------------------------------------------------------------------------------------
public class SchedulerQueue extends AbstractItem {
	protected Scheduler scheduler;
    protected LinkedList<OrderX> queue;

    //---cache:
    protected CurrencyPair currencyPair;
    protected int type;

    protected Sides sides;
	protected Orders thisSideOrders;
	protected Orders reverseSideOrders;


    //---statics:
	private static final int STATE_INIT = 0;

//-------------------------------------------------------------------------------------
    public SchedulerQueue(Debug _debug, Resolver _resolver, Scheduler _scheduler) {
        super(_debug, _resolver);
		scheduler = _scheduler;
		queue = new LinkedList<OrderX>();

        currencyPair = _scheduler.getCurrencyPair();
        type = _scheduler.getType();

		sides = _scheduler.getSides();
		thisSideOrders = sides.getSide(type).getOrders();
		reverseSideOrders  = sides.getReverseSide(type).getOrders();

//        org.knowm.xchange.currency.Currency _currency = (type == OrderX.TYPE_BUY) ? currencyPair.base : currencyPair.counter;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public OrderX nextOrder(){
		debug.outln(Debug.INFO, "SchedulerInputQueue.nextOrder: type["+type+"]...0");
    	OrderX _order = thisSideOrders.getNextOrder();
    	if(_order == null){
    		return null;
    	}
    	BigDecimal _amount = _order.getAvailableToConsume();
//		debug.outln(Debug.INFO, "SchedulerInputQueue.nextOrder: 0-- _amount="+_amount+", minimumAmount="+minimumAmount);
/*    	if(_amount.compareTo(minimumAmount) < 0){
			debug.outln(Debug.INFO, "SchedulerInputQueue.nextOrder: 0--0");
			_order.setState(OrderX.STATE_UNUSABLE);
    		return nextOrder();
    	}*/
//		BigDecimal _amountConsumed = calculateConsumed(_order);
//		BigDecimal _amountLocked = calculateLocked(_order);
//		debug.outln(Debug.INFO, "SchedulerInputQueue.nextOrder: ...1.._amount="+ _amount );
		if(_amount.compareTo(BigDecimal.ZERO) > 0){
//			BigDecimal _difference = _amount.subtract(_amountConsumed);
//			return _order.getOrderWithAmount(_difference);
			return _order;
		}
		return null;
    }

//-------------------------------------------------------------------------------------
    public void placeOrder(OrderX _order){
//    	sides.getSide(_order.getType()).addOrder(_order);
    	_order.setState(OrderX.STATE_WAITING_FOR_EXECUTION);
		long _id = reverseSideOrders.addOrder(_order);
		List<Long> _backwardPeers = _order.getBackwardPeers();
		for(int i = 0; i < _backwardPeers.size(); i++){
			long _parentId = _backwardPeers.get(i);

			OrderX _parentOrder = thisSideOrders.getOrderById(_parentId);
			if(_parentOrder == null){
				debug.outln(Debug.INFO, "Illegal reference to parent in order "+_id+"[parentId = "+_parentId+"]");
			} else {
	//			_parentOrder.setState(OrderX.STATE_LOCKED);
				_parentOrder.addForwardPeer(_id);
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
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

//-------------------------------------------------------------------------------------
    public OrderX removeOrder(){
    	return queue.poll();
    }

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
//    	deleteNonExexecutedReferences();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "SchedulerQueue:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
