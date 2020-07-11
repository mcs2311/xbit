//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level3.strategies;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.*;
import org.ta4j.core.num.*;
import org.ta4j.core.trading.rules.*;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.helpers.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level2.indicators.*;

//-------------------------------------------------------------------------------------
public abstract class StrategyX extends AbstractEntity<StrategyConfiguration> implements FlowableOnSubscribe<StrategyEvent> {
//	protected String repositoryName;
	protected CurrencyPair currencyPair;
	protected Map<String, String> args;


	protected Strategy strategy;
	protected List<CachedIndicator<Num>> indicatorsMatrix;

	private int rebuildInterval;
	private int eventCounter;
	private long lastTimeStrategyWasRebuild;

    //---rx:
	private Flowable<StrategyEvent> flowable;
	protected FlowableEmitter<StrategyEvent> emitter;

//-------------------------------------------------------------------------------------
    public StrategyX(Debug _debug, Resolver _resolver, StrategyConfiguration _strategyConfiguration, ConfigurationLoader<StrategyConfiguration> _configurationLoader, CurrencyPair _currencyPair, Map<String, String> _args) {
    	super(_debug, _resolver, _strategyConfiguration);
//    	repositoryName = _repositoryName;
    	currencyPair = _currencyPair;
    	args = _args;
    	rebuildInterval = StringUtils.getNumber(args.get("rebuildInterval"));
    	indicatorsMatrix = new ArrayList<CachedIndicator<Num>>();
        debug.outln("StrategyX started for "+_currencyPair+": "+_strategyConfiguration);
        eventCounter = 0;
    	lastTimeStrategyWasRebuild = 0L;
        

    	flowable = Flowable.create(this, BackpressureStrategy.DROP)
    			.share()
    			.onBackpressureBuffer(16, () -> { },
              		BackpressureOverflowStrategy.DROP_OLDEST);

    	
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public synchronized void onNext(BeanEvent _beanEvent) {
		boolean _shouldEnter = false;
		boolean _shouldExit = false;
		//---
/*		if(eventCounter%2000 == 0){
			debug.outln("StrategyX.onNext: " + _beanEvent);
		}
*/
		TradeEvent _tradeEvent = (TradeEvent)_beanEvent;
		
		if(_tradeEvent.getTimestamp() - lastTimeStrategyWasRebuild > rebuildInterval){ //1h
			buildStrategy(_beanEvent);
    		lastTimeStrategyWasRebuild = _tradeEvent.getTimestamp();
		}

		eventCounter++;
		int _index = _tradeEvent.getIndex();
		CachedIndicator<Num> _indicator = indicatorsMatrix.get(0);
//		debug.outln("StrategyX.listenEvent: " + _event+" , _indicator="+_indicator.getValue(_index) + ",entry_rule:" + strategy.getEntryRule().isSatisfied(_index) +",exit_rule: " + strategy.getExitRule().isSatisfied(_index));
//		debug.outln("StrategyX.listenEvent: entry_rule: " + strategy.getEntryRule().isSatisfied(_index));
		if(emitter != null){
			try{
				_shouldEnter = strategy.shouldEnter(_index);
				_shouldExit = strategy.shouldExit(_index);
			}catch(Exception _e){
				debug.outln(Debug.WARNING, "Error in StrategyX at index #"+_index + ", ["+this+"]");
				return;
			}
			if(_shouldEnter){
				emitter.onNext(new StrategyEvent(StrategyEvent.STRATEGY_ENTER, args, _beanEvent));
	//			debug.outln("StrategyX.pushEvent: " + _newEvent);
			} else if(_shouldExit){
				emitter.onNext(new StrategyEvent(StrategyEvent.STRATEGY_EXIT, args, _beanEvent));
	//			debug.outln("StrategyX.pushEvent: " + _newEvent);
			}
		}
	}

//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<StrategyEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<StrategyEvent> getStrategyEvent() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected abstract void buildStrategy(BeanEvent _beanEvent);

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	Indicators _indicators = resolver.getIndicators();
    	//resolver.getIndicators();
    	List<List<String>> _indicatorNames = configuration.getIndicators();
		debug.outln("StrategyX._indicatorNames.size: " + _indicatorNames.size());
    	for (int i = 0; i < _indicatorNames.size(); i++) {
    		List<String> _indicator = _indicatorNames.get(i);
	    	IndicatorX _indicatorX = _indicators.getIndicator(currencyPair, _indicator);
	    	_indicatorX.getBean()
	    			.subscribeOn(Schedulers.io())
    				.observeOn(Schedulers.computation(), false)
    				.subscribe(_beanEvent -> onNext(_beanEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
	    	//register(Event.INDICATOR_UPDATE_EVENT, this);    		
	    	indicatorsMatrix.add(_indicatorX.getIndicator());
    	}    	
    }

//-------------------------------------------------------------------------------------
    public void link() {
//		debug.outln(Debug.ERROR, "repository.load:" + getName());
//    	String _repositoryName = configuration.getRepository();

//    	buildStrategy();
    }

//-------------------------------------------------------------------------------------
    public void initiate() {
    	

   	}

//-------------------------------------------------------------------------------------
    public void save(){
    }


//-------------------------------------------------------------------------------------
    public String toString() {
		return "StrategyX:" + 
		configuration;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
