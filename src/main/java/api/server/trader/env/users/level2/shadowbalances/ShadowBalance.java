//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level2.shadowbalances;
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
import codex.xbit.api.server.trader.env.users.level1.rawbalances.*;

//-------------------------------------------------------------------------------------
public class ShadowBalance extends AbstractEntity<Configuration> implements FlowableOnSubscribe<UserBalanceEvent> {
	private String exchange;
	private Currency currency;
	private BigDecimal rawAmount, lockedAmount, availableAmount;

    //---rx:
	private Flowable<UserBalanceEvent> flowable;
	private FlowableEmitter<UserBalanceEvent> emitter;

//-------------------------------------------------------------------------------------
    public ShadowBalance(Debug _debug, Resolver _resolver, String _exchange, Currency _currency) {
    	super(_debug, _resolver, null, null);
    	exchange = _exchange;
    	currency = _currency;

    	rawAmount = BigDecimal.ZERO;
    	lockedAmount = BigDecimal.ZERO;
    	availableAmount = BigDecimal.ZERO;

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);

    	_resolver.getRawBalances()
    			.getRawBalancesByExchange(_exchange)
    			.getRawBalance(_currency)
    			.getRawBalance()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation())
    			.subscribe(_userBalanceEvent -> onNext(_userBalanceEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void onNext(UserBalanceEvent _userBalanceEvent){
		rawAmount = _userBalanceEvent.getAmount();
		recalculate();
		if(emitter != null){
    		UserBalanceEvent _balanceEventNew = new UserBalanceEvent(UserBalanceEvent.BALANCE_UPDATE, availableAmount);
    		emitter.onNext(_balanceEventNew);
		}
	}

//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserBalanceEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserBalanceEvent> getShadowBalance() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Currency getCurrency() {
    	return currency;
    }

//-------------------------------------------------------------------------------------
    public synchronized void recalculate() {
//    	debug.outln("ShadowBalance.recalculate.before:"+ toString());
    	availableAmount = rawAmount.subtract(lockedAmount);
    	debug.outln("ShBal.recalc.after:"+ toString());
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean check(BigDecimal _amount) {
    	return (availableAmount.compareTo(_amount) >= 0);
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean lock(BigDecimal _amount) {
    	BigDecimal _lockedAmountNew = lockedAmount.add(_amount);
    	if(availableAmount.compareTo(_lockedAmountNew) >= 0){
    		lockedAmount = _lockedAmountNew;
    		recalculate();
    		return true;
    	} else {
    		return false;
    	}
    }

//-------------------------------------------------------------------------------------
    public synchronized boolean unlock(BigDecimal _amount) {
    	BigDecimal _lockedAmountNew = lockedAmount.subtract(_amount);
    	if(lockedAmount.compareTo(_amount) >= 0){
    		lockedAmount = _lockedAmountNew;
    		recalculate();
    		return true;
    	} else {
    		return false;
    	}
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getAvailable() {
        return availableAmount;
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "ShadowBalance@" + 
		exchange +
		"," +
		currency + ": " +
		"[raw=" + rawAmount + "], " +
		"[locked=" + lockedAmount + "], " +
		"[available=" + availableAmount + "]";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
