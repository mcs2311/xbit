//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.manual;
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
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level5.signals.*;

//-------------------------------------------------------------------------------------
public class ManualSignal extends Signal {
	private int eventCounter;

//-------------------------------------------------------------------------------------
    public ManualSignal(Debug _debug, Resolver _resolver, SignalConfiguration _configuration, CurrencyPair _currencyPair, Map<String, String> _args) {
    	super(_debug, _resolver, _configuration, _currencyPair, _args);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void sendSignal(String[] _args) {
		String _type = _args[0];
		int _count = 10;
		if(_args.length == 2){
			String _countString = _args[1];
			try{
				_count = Integer.parseInt(_countString);				
			}catch (Exception _e) {}
		}
		for (int i = 0; i < _count; i++) {
			sendSignal(_type);
		}

//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
	}

//-------------------------------------------------------------------------------------
	public void sendSignal(String _type) {
		SignalEvent _newEvent = null;
		if(_type.equals("buy")){
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_BUY, confidence, SignalEvent.SOURCE_MANUAL,  null);
		} else if(_type.equals("sell")){
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_SELL, confidence, SignalEvent.SOURCE_MANUAL,  null);			
		} else {
			debug.outln(Debug.WARNING, "Unknown signal type in ManualSignal: "+_type);			
			return;
		}
//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
		if(emitter != null){
			emitter.onNext(_newEvent);
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }


//-------------------------------------------------------------------------------------
    public void save(){
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "ManualSignal:" + 
		currencyPair + 
		" , " + 
		args;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
