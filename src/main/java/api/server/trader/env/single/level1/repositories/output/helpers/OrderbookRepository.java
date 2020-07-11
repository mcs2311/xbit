//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories.output.helpers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.sql.*;
import java.math.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.dto.trade.*;

import org.ta4j.core.*;

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
public class OrderbookRepository extends OutputRepository {
//	private int levels;


	private long bidsSize;
	private long asksSize;
    //---cache:

	//---statics:
	private final static int NO_OF_LEVELS = 5;
	private final static DecimalFormat df = new DecimalFormat("#.#######");
//	private final static float PI_2 = (float)(2 * Math.PI);

//-------------------------------------------------------------------------------------
    public OrderbookRepository(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ConfigurationLoader<RepositoryConfiguration> _configurationLoader) {
    	super(_debug, _resolver, _repositoryConfiguration, _configurationLoader);
//    	debug.outln(Debug.INFO, "OrderbookRepository: currencyPair="+currencyPair);
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(BeanEvent _beanEvent) {
    	OrderbookEvent _orderbookEvent = (OrderbookEvent)_beanEvent;
    	OrderBook _orderBook = _orderbookEvent.getOrderBook();
//    	debug.outln(Debug.INFO, "InputRepository.onNext - _orderBook: asks: "+asks.size() + ", bids: "+bids.size());
		
		if(_orderBook == null){
			return;
		}

    	List<LimitOrder> _bids = _orderBook.getBids();
    	List<LimitOrder> _asks = _orderBook.getAsks();

		if((_bids == null) || (_asks == null)){
			return;
		}

    	int _size = Math.min(_bids.size(), _asks.size());
    	
    	_size = Math.min(_size, NO_OF_LEVELS);

    	if(_size == 0){
    		return;
    	}

    	if((_asks != null) && (_bids != null)){
    		bidsSize = 0;
    		asksSize = 0;
    		for (int i = 0; i < _size; i++) {
    			bidsSize += _bids.get(i).getOriginalAmount().intValue();
    			asksSize += _asks.get(i).getOriginalAmount().intValue();
//	      		debug.outln("OrderbookEvent>>, _bid=" + _bids.get(i) + ", _ask= " + _asks.get(i));
    		}
//	      	debug.outln("OrderbookEvent>>, bidsSize=" + bidsSize + ", asksSize= " + asksSize);
	    	if(emitter != null){
	    		OrderbookEvent _orderbookEvent0 = new OrderbookEvent(_orderBook, bidsSize, asksSize);
				emitter.onNext(_orderbookEvent0);
	    	}
    	}
	}
/*
//-------------------------------------------------------------------------------------
    public String toString(float[] _line){
    	String _s = "";
    	for (int i = 0; i < _line.length; i++) {
    		_s += ", " + Float.valueOf(df.format(_line[i]));
    	}
    	return _s;
    }
//-------------------------------------------------------------------------------------
	private float calculateAsksAngle(float[] _line) {
//		float _y = (float)Math.log10(_line[3] - _line[1]);
		float _y = _line[3] / _line[1];
		float _a = (float)Math.atan2(_y, _line[2] - _line[0]);
		return (_a > 0 ? _a : ( PI_2 + _a)) * 360 / PI_2;
	}

//-------------------------------------------------------------------------------------
	private float calculateBidsAngle(float[] _line) {
//		float _y = (float)Math.log10(_line[3] - _line[1]);
		float _y = _line[3] / _line[1];
		float _a = (float)Math.atan2(_y, _line[0] - _line[2]);
		return (_a > 0 ? _a : ( PI_2 + _a)) * 360 / PI_2;
	}

//-------------------------------------------------------------------------------------
	private float calculateHeight(float[] _line1, float[] _line2) {
		return _line2[1] - _line1[1];
	}

//-------------------------------------------------------------------------------------
	private boolean isMinimumVariation(float _v1, float _v2) {
		float _x = Math.abs((_v1 - _v2) / _v2);
//	    debug.outln(" _x=" + _x);
		return (_x > MINIMUM_ANGLE_VARIATION);
	}
*/

//-------------------------------------------------------------------------------------
    public BarSeries getSeries(String _type){
        return null;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	resolver.getRepositories()
    			.getInputRepositories()
    			.getRepository(currencyPair, "orderbook")
    			.getBean()
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.computation(), false)
				.subscribe(_beanEvent -> onNext(_beanEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
    }

//-------------------------------------------------------------------------------------
    public void save() {

    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
