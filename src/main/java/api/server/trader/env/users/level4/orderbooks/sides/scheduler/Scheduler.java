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
//import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.scheduler.queues.*;

//-------------------------------------------------------------------------------------
public class Scheduler extends AbstractEntity<Configuration> {
    private CurrencyPair currencyPair;
    private int type;
    private Sides sides;

//	private SchedulerInputQueue schedulerInputQueue;
	private SchedulerQueue schedulerQueue;

    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public Scheduler(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair, int _type, Sides _types) {
        super(_debug, _resolver, null, null);
		currencyPair = _currencyPair;
		type = _type;
		sides = _types;
		schedulerQueue = new SchedulerQueue(_debug, _resolver, this);
//		schedulerOutputQueue = new SchedulerOutputQueue(_debug, _resolver, this);
//		brokers = new Pairs(_debug, _resolver, _tacticConfiguration, _currencyPair, _type, this);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public CurrencyPair getCurrencyPair(){
		return currencyPair;
	}

//-------------------------------------------------------------------------------------
	public int getType(){
		return type;
	}

//-------------------------------------------------------------------------------------
	public Sides getSides(){
		return sides;
	}
/*
//-------------------------------------------------------------------------------------
	public SchedulerInputQueue getSchedulerInputQueue(){
		return schedulerInputQueue;
	}

//-------------------------------------------------------------------------------------
	public SchedulerOutputQueue getSchedulerOutputQueue(){
		return schedulerOutputQueue;
	}
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized OrderX nextOrder(){
		debug.outln(Debug.INFO, "Scheduler.nextOrder: ");
		return schedulerQueue.nextOrder();
    }

//-------------------------------------------------------------------------------------
    public synchronized void placeOrder(OrderX _order){
//		debug.outln(Debug.INFO, "Scheduler.nextOrder: ");
		schedulerQueue.placeOrder(_order);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
		schedulerQueue.load();
//		schedulerOutputQueue.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
		schedulerQueue.save();
//		schedulerInputQueue.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Scheduler:" + 
		currencyPair;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
