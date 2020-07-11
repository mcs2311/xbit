//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.*;

//-------------------------------------------------------------------------------------
public abstract class Signal extends AbstractEntity<SignalConfiguration> implements FlowableOnSubscribe<SignalEvent> {
	protected CurrencyPair currencyPair;
	protected Map<String, String> args;
	protected float confidence;

    //---rx:
	protected Flowable<SignalEvent> flowable;
	protected FlowableEmitter<SignalEvent> emitter;


//-------------------------------------------------------------------------------------
    public Signal(Debug _debug, Resolver _resolver, SignalConfiguration _configuration) {
    	this(_debug, _resolver, _configuration, null, null);
    }

//-------------------------------------------------------------------------------------
    public Signal(Debug _debug, Resolver _resolver, SignalConfiguration _configuration, CurrencyPair _currencyPair, Map<String, String> _args) {
    	super(_debug, _resolver, _configuration);
    	currencyPair = _currencyPair;
    	args = _args;


    	confidence = configuration.getConfidence();
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
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<SignalEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<SignalEvent> getSignalEvent() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
	public void onNext(SignalEvent _signalEvent) {
		if(emitter != null){
			emitter.onNext(_signalEvent);
		}
	}
	
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	super.save();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "Signal:" + 
		currencyPair;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
