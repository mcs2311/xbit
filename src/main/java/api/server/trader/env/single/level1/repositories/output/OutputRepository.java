//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories.output;
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
import codex.xbit.api.server.trader.env.single.level1.repositories.*;

//-------------------------------------------------------------------------------------
public abstract class OutputRepository extends Repository {
    protected ZonedDateTime lastBarEndTime;
    protected Duration barDuration;
    protected ZoneId zoneId;
    protected CurrencyPair currencyPair;

    protected ZonedDateTime lastBarLastEntryTime;
    protected boolean isInstant = false;
    protected ZonedDateTime startupTime;
    //---cache:

//-------------------------------------------------------------------------------------
    public OutputRepository(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ConfigurationLoader<RepositoryConfiguration> _configurationLoader) {
    	super(_debug, _resolver, _repositoryConfiguration, _configurationLoader);

/*        String _zoneIdString = resolver.getServerConfiguration().getZoneId();
//        debug.outln("ZoneId: "+ _zoneIdString);
        if(_zoneIdString.isEmpty()){
        	zoneId = ZoneId.systemDefault();
        } else {
        	zoneId = ZoneId.of(_zoneIdString);
        }*/
        zoneId = resolver.getZoneId();
//        BeanEvent.setZoneId(zoneId);
        currencyPair = configuration.getCurrencyPair();
        startupTime = ZonedDateTime.now(zoneId);

    	if(configuration.getBarDuration().equals("instant")){
    		isInstant = true;
    		return;
    	}
        int _barDuration = configuration.getBarDurationAsInt();
//		debug.outln("Repository = "+getName()+", barDuration= "+_barDuration+" ...");
        barDuration = Duration.ofSeconds(_barDuration);

//    	flowable = Flowable.create(this, BackpressureStrategy.DROP);
//    			.onBackpressureBuffer(16, () -> { },
//              		BackpressureOverflowStrategy.DROP_OLDEST);

    }

//-------------------------------------------------------------------------------------
    public abstract BarSeries getSeries(String _type);

//-------------------------------------------------------------------------------------
    public void onNext(BeanEvent _beanEvent) {
        debug.out("OutputRepository.["+configuration.getExchange()+"."+configuration.getCurrencyPair()+"."+configuration.getType()+"] .... onNext");
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
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
    	super.load();
	}


//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
