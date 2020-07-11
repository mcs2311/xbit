//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories.output.helpers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
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
import codex.xbit.api.server.trader.env.single.level1.repositories.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;

//-------------------------------------------------------------------------------------
public class TradeRepository extends OutputRepository {
    protected TradeRepositoryLoader repositoryLoader;
    private BarSeries series;
    private Bar lastBar;

    //---cache:

//-------------------------------------------------------------------------------------
    public TradeRepository(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ConfigurationLoader<RepositoryConfiguration> _configurationLoader) {
    	super(_debug, _resolver, _repositoryConfiguration, _configurationLoader);
    	if(!isInstant){
	        series = new BaseBarSeriesBuilder().withName(configuration.getCurrencyPair().toString() + "." + barDuration).build();
	//        series = new BaseBarSeries();
	        int _maxBarCount = resolver.getTraderConfiguration().getMaxBarCount();
	        series.setMaximumBarCount(_maxBarCount);
	    	repositoryLoader = new TradeRepositoryLoader(_debug, _resolver, _repositoryConfiguration, zoneId, series);
	    	repositoryLoader.load();
	    	lastBar = repositoryLoader.getLastBar();
	    	lastBarEndTime = repositoryLoader.getLastBarEndTime();
	    	lastBarLastEntryTime = repositoryLoader.getLastEntryTime();
    	}
    	load();
    }

//-------------------------------------------------------------------------------------
    public BarSeries getSeries(String _type){
        return series;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized void onNext(BeanEvent _beanEvent) {
//    	BeanEvent _beanEvent = _beanEvent.clone();
    	TradeEvent _tradeEvent = new TradeEvent((TradeEvent)_beanEvent);
        ZonedDateTime _zonedDateTime = _tradeEvent.getTime();
//        debug.outln("Repository.pushBean:...["+this+"]: "+_beanEvent);
        if(_zonedDateTime == null){
        	debug.outln("Abnormal BeanEvent in TradeRepository:...["+this+"]: "+_beanEvent);
        	return;
        }
        if((lastBar == null) || (_zonedDateTime.compareTo(lastBarEndTime) > 0)){
	        try{
	        	addNewBar(_zonedDateTime);
		    }catch (Exception _e) {
	        	debug.outln("Exception in TradeRepository:...["+this+"]: "+_e.getMessage());	    	
	        	return;
		    }
        }
//    	displayBean(_beanEvent);

//        debug.outln("Repository.pushBean:...1: "+lastBar.getTimePeriod()+", _beginT:"+lastBar.getBeginTime()+", _endT:"+lastBar.getEndTime());
        if(lastBar.inPeriod(_zonedDateTime)){
//        	debug.outln("Repository.pushBean:...2");
        	if(_zonedDateTime.compareTo(lastBarLastEntryTime) > 0){
//        	debug.outln("Repository.pushBean:...3");
		        PrecisionNum _price = PrecisionNum.valueOf(_tradeEvent.getPrice());
		        PrecisionNum _amount = PrecisionNum.valueOf(_tradeEvent.getAmount());
		        series.addTrade(_amount, _price);

		        repositoryLoader.setLastEntryTime(_zonedDateTime);
//		        if(_zonedDateTime.compareTo(ZonedDateTime.now(zoneId)) > 0){
//		        int _index = series.getEndIndex();
//		        if(_index > 1) {
//     	displayBean(_beanEvent);
//		        	pushEvent(_beanEvent, _index);
//		        }
//		        }
        	}
        }
		int _index = series.getEndIndex();
//        debug.outln("TradeRepository ["+this+"]emit:" + _beanEvent + ", _index="+_index+",configuration="+configuration);
		_tradeEvent.setIndex(_index);
		if(emitter != null){
			emitter.onNext(_tradeEvent);
		}		
	}

//-------------------------------------------------------------------------------------
    public void addNewBar(ZonedDateTime _zonedDateTime) {
		while(_zonedDateTime.compareTo(lastBarEndTime) > 0){
        	lastBarEndTime = lastBarEndTime.plus(barDuration);
//        	debug.outln("_zonedDateTime=["+_zonedDateTime+"], lastBarEndTime=["+lastBarEndTime+"]");
    	}
//        lastBarEndTime = _zonedDateTime.plus(barDuration);
        lastBar = new BaseBar(barDuration, lastBarEndTime, series.function());
        series.addBar(barDuration, lastBarEndTime);
//        debug.outln("Repository.addNewBar:"+ this +" : " + _zonedDateTime);
    }

//-------------------------------------------------------------------------------------
    protected void displayTrade(Trade _trade){
        debug.out("["+configuration.getExchange()+"."+configuration.getCurrencyPair()+"."+configuration.getType()+"] ");
//        debug.outln(" ] [" + _ticker.getCurrencyPair().toString() + "]: Bid=" + _ticker.getBid() + " Ask=" + _ticker.getAsk() + " BidSize=" + _ticker.getBidSize() + " AskSize=" + _ticker.getAskSize());
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	if(!configuration.isEnabled()){
    		return;
    	}
    	super.load();
    	resolver.getRepositories()
    			.getInputRepositories()
    			.getRepository(currencyPair, "trade")
    			.getBean()
				.subscribeOn(Schedulers.io())
//				.onBackpressureDrop()
//				.observeOn(Schedulers.computation(), false)
				.observeOn(Schedulers.newThread(), false)
				.subscribe(_beanEvent -> onNext(_beanEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());    	
//    	String _exchange = configuration.getExchange();
    }

//-------------------------------------------------------------------------------------
    public void save() {
//    	super.save();
    	if(repositoryLoader != null){
    		repositoryLoader.save();
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return super.toString() + 
			"endIndex=" +
			series.getEndIndex();
	}
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
