//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.rawbalances;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.dto.account.*;
import org.knowm.xchange.dto.Order;
//import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.trade.*;
import org.knowm.xchange.service.trade.params.orders.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;

//-------------------------------------------------------------------------------------
public class RawBalance extends AbstractEntity<Configuration> implements FlowableOnSubscribe<UserBalanceEvent> {
	private String exchange;
	private Currency currency;

	//---cache:
	private BigDecimal amount;


    //---rx:
	private Flowable<UserBalanceEvent> flowable;
	private FlowableEmitter<UserBalanceEvent> emitter;

//-------------------------------------------------------------------------------------
    public RawBalance(Debug _debug, Resolver _resolver, String _exchange, Currency _currency) {
    	super(_debug, _resolver, null, null);
    	exchange = _exchange;
    	currency = _currency;

    	amount = BigDecimal.ZERO;

		UserExchange _userExchange = resolver.getUserExchanges().getUserExchange(exchange);

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);

    	ExchangeConfiguration _exchangeConfiguration = _userExchange.getExchangeConfiguration();
		Currency _currencyCode = _exchangeConfiguration.getCurrencyCode(currency);

		if(_exchangeConfiguration.supportForStreamingBalanceChanges()){
			io.reactivex.Observable<Balance> _balanceChanges = _resolver.getUserExchanges().getUserExchange(_exchange)
	    			.getBalanceChanges(_currencyCode);

	    	if(_balanceChanges != null){
	    			_balanceChanges.subscribeOn(Schedulers.io())
	    			.observeOn(Schedulers.computation())
	    			.subscribe(_balance -> onNext(_balance),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());
	    	} else {
	    		debug.outln(Debug.ERROR, "RawBalance of exchange "+exchange + "/" + currency +" has no way to update!!!!");
	    	}
	    }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(Balance _balance){
    	BigDecimal _newAmount = _balance.getAvailable();
//    	debug.outln("RawBalance.onNext.0:"+ toString()+", _newAmount="+_newAmount);    	
    	if(amount != _newAmount){
    		amount = _newAmount;
	    	if(emitter != null){
	    		UserBalanceEvent _userBalanceEvent = new UserBalanceEvent(UserBalanceEvent.BALANCE_UPDATE, amount);
	    		emitter.onNext(_userBalanceEvent);
	    	}
	    }
    }

//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserBalanceEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserBalanceEvent> getRawBalance() {
		return flowable;
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Currency getCurrency() {
    	return currency;
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getAvailable() {
//        return balances.get(_currency).getAvailable().floatValue();
/*        if(shadowBalance == null){
            debug.outln("0");
            return null;
        }
        BigDecimal _available = shadowBalance.getAvailable();
        if(_available == null){
            debug.outln("1");
            return null;
        }
        debug.outln("2");
        debug.outln("Balances.getAvailable.."+currency+"..."+_available);
        return _available;*/
        return amount;
    }
/*
//-------------------------------------------------------------------------------------
    public String setBalance(BigDecimal _amount) {
    	String _info = "";
//    	boolean _thereIsAChange = ((shadowBalance == null) || (!shadowBalance.getAvailable().equals(_balance.getAvailable())));
//    	BigDecimal _amount = _balance.getAvailable();
    	boolean _thereIsAChange = (!_amount.equals(amount));
	    amount = _amount;
    	if(!_amount.equals(BigDecimal.ZERO)){
	//		debug.outl(Debug.WARNING, "Cannot update shadowBalance for currency=" + _currency + " and exchange = " +_exchange.getShortName());	
	    	_info = "[" + currency + " : " + amount+"] ";
    	}
    	if(_thereIsAChange){
	    	SignalEvent _event = new Event(Event.BALANCE_UPDATE, null, null);
	    	pushEvent(_event);
    	}
    	return _info;
    }
*/
//-------------------------------------------------------------------------------------
    public String toString() {
		return "RawBalance@" + 
		": " +
		exchange +
		"," +
		currency + 
		"," +
		amount;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
