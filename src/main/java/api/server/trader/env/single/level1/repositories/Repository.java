//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

//import rx.*;
import io.reactivex.*;
import io.reactivex.flowables.*;
import io.reactivex.disposables.*;
import io.reactivex.schedulers.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.num.*;
import org.ta4j.core.trading.rules.*;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.helpers.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.common.loaders.repositories.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level0.influxexchanges.*;

//-------------------------------------------------------------------------------------
public abstract class Repository extends AbstractEntity<RepositoryConfiguration> implements FlowableOnSubscribe<BeanEvent> {

    //---cache:

    //---rx:
	protected Flowable<BeanEvent> flowable;
	protected FlowableEmitter<BeanEvent> emitter;

//-------------------------------------------------------------------------------------
    public Repository(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ConfigurationLoader<RepositoryConfiguration> _configurationLoader) {
    	super(_debug, _resolver, _repositoryConfiguration);
    	
    	flowable = Flowable.create(this, BackpressureStrategy.DROP)
    				.share();
//    				.publish()
//    				.autoConnect();
    	//Flowable.create(this, BackpressureStrategy.BUFFER);
//    			.onBackpressureBuffer(16, () -> { },
//              		BackpressureOverflowStrategy.DROP_OLDEST);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<BeanEvent> _emitter) throws Exception {
		emitter = _emitter;
    }
/*
//-------------------------------------------------------------------------------------
	public void onSubscribe(Subscription _s) {
//    	_s.request(Long.MAX_VALUE);
	}
*/
//-------------------------------------------------------------------------------------
	public Flowable<BeanEvent> getBean() {
		return flowable;
    }

/*
//-------------------------------------------------------------------------------------
    protected void displayTicker(Ticker _ticker){
        debug.out("["+configuration.getExchange()+"."+configuration.getCurrencyPair()+"."+configuration.getType()+"] ");
        debug.outln(" ] [" + _ticker.getCurrencyPair().toString() + "]: Bid=" + _ticker.getBid() + " Ask=" + _ticker.getAsk() + " BidSize=" + _ticker.getBidSize() + " AskSize=" + _ticker.getAskSize());
    }

//-------------------------------------------------------------------------------------
    protected void displayBean(BeanEvent _beanEvent){
//        debug.out("["+name+"] [ ");
//        debug.out(Debug.IMPORTANT1, exchangeConfiguration.getShortName());
//        debug.out(" ] [" + _ticker.getCurrencyPair().toString() + "]: Bid=" + _ticker.getBid() + " Ask=" + _ticker.getAsk());
    	long _timestamp = Timestamp.valueOf(_beanEvent.getTime().toLocalDateTime()).getTime();
        debug.outln("BeanEvent: price=" + _beanEvent.getPrice() + ", amount=" + _beanEvent.getAmount()+", time="+_timestamp);
//        debug.outln(", _currentTime="+_currentTime+", minValue="+minValue+" , maxValue="+maxValue+", _timeMax="+_timeMax+", _timeMin="+_timeMin+", _maxDiff="+_maxDiff+", _minDiff="+_minDiff, false);

    }
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	// Empty: inhibate overwriting the repository yaml
    }



//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "Repository:" + 
		configuration.getExchange() + 
		" , " + 
		configuration.getCurrencyPair() + 
		" , " + 
		configuration.getType() +
		" , " +
		configuration.getBarDuration() +
		"@" +
		hashCode();
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
