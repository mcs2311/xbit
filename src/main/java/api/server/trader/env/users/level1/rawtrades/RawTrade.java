//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.rawtrades;
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

//import codex.xbit.api.server.trader.env.users.level1.raworders.orderexecutor.*;

//-------------------------------------------------------------------------------------
public class RawTrade extends AbstractTrade implements FlowableOnSubscribe<AbstractTrade> {

	//---cache:

    //---rx:
	private Flowable<AbstractTrade> flowable;
	private FlowableEmitter<AbstractTrade> emitter;

	//---statics:

//-------------------------------------------------------------------------------------
    public RawTrade(Debug _debug, Resolver _resolver, String _exchange, CurrencyPair _currencyPair, UserTrade _trade) {
    	this(_debug, 
    		_resolver, 
    		StringUtils.getLongNumber(_trade.getId()),
    		_exchange,
    		_currencyPair,
    		_trade.getTimestamp().getTime(),
    		_trade.getPrice(),
    		_trade.getOriginalAmount(),
    		(_trade.getType() == Order.OrderType.BID) ? AbstractOrder.TYPE_BUY : AbstractOrder.TYPE_SELL,
    		StringUtils.getLongNumber(_trade.getOrderId()));
    }

//-------------------------------------------------------------------------------------
    public RawTrade(Debug _debug, Resolver _resolver, long _id, String _exchange, CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, long _orderId) {
    	super(_debug, _resolver, _id, _exchange, _currencyPair, _time, _price, _amount, _type, _orderId);
//        setState(STATE_NOT_EXECUTED);
//        debug.outln(Debug.IMPORTANT3, "OrderX state="+_state+", int="+state);
    	load();
    }

//-------------------------------------------------------------------------------------
    public RawTrade clone() {
		return new RawTrade(debug, 
			resolver, 
			id,
			exchange,
			currencyPair,
			time,
			price,
			amount,
			type,
			orderId);
    }

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
	public void subscribe(FlowableEmitter<AbstractTrade> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<AbstractTrade> getRawTrade() {
		return flowable;
    }



//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public RawTrade getOrderWithAmount(BigDecimal _amount) {
    	RawTrade _rawTrade = new RawTrade(debug, 
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
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//        debug.outln(Debug.IMPORTANT3, "RawTrade created... "+this);

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);

/*    	_resolver.getUserExchanges().getExchange(_exchange.getShortName())
    			.getOrderChanges(_currencyPair)
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation())
    			.subscribe(_order -> onNext(_order),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());*/
    }

//-------------------------------------------------------------------------------------
    public void save() {
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "RawTrade: "+super.toString();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
