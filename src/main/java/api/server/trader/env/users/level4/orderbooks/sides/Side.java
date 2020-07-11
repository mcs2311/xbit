//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
//import java.util.*;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

//import io.reactivex.*;
//import io.reactivex.schedulers.*;
//import io.reactivex.disposables.*;

import codex.common.utils.*;

import org.knowm.xchange.currency.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.scheduler.*;


//-------------------------------------------------------------------------------------
public class Side extends AbstractItem {
	private CurrencyPair currencyPair;
	private int type;
	private Sides sides;

    private Orders orders;
    private Scheduler scheduler;
    
    //---cache:

//-------------------------------------------------------------------------------------
    public Side(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair, String _exchange, int _type, Sides _types) {
    	super(_debug, _resolver);
    	currencyPair = _currencyPair;
    	type = _type;
    	sides = _types;

    	String _prefix = OrderX.convertTypeFromIntLongForm(_type);
    	debug = new Debug(_debug, _prefix, Debug.IMPORTANT1);

    	orders = new Orders(debug, _resolver, _currencyPair, _exchange, _type, _types.getSidesOrdersContainer());
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public CurrencyPair getCurrencyPair() {
    	return currencyPair;
    }

//-------------------------------------------------------------------------------------
    public Orders getOrders() {
    	return orders;
    }

//-------------------------------------------------------------------------------------
    public Scheduler getScheduler() {
    	return scheduler;
    }

//-------------------------------------------------------------------------------------
    public OrderX getNextOrder() {
    	return orders.getNextOrder();
    }

//-------------------------------------------------------------------------------------
    public OrderX getOrderById(int _id) {
    	return orders.getOrderById(_id);
    }

//-------------------------------------------------------------------------------------
    public void addOrder(OrderX _order) {
    	orders.addOrder(_order);
    }

//-------------------------------------------------------------------------------------
    public void addTrade(AbstractTrade _trade) {
    	orders.addTrade(_trade);
    }

//-------------------------------------------------------------------------------------
	public List<String> getInfo(int _netCommandParameter){
        List<String> _list = new ArrayList<String>();
        String _head = debug.getPrefixes();
        _list.add(StringUtils.createBanner(_head));
        _list.addAll(orders.getInfo());
        return _list;
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
    public void load() {
//    	debug.outln(Debug.INFO, "Side....load...");
		super.load();
        orders.load();
    	scheduler = new Scheduler(debug, resolver, currencyPair, type, sides);        
        scheduler.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
//        scheduler.save();
        orders.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Side:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
