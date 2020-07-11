//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level6.pilots.helpers.marketanalyzer;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.jline.utils.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;

import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.*;

//-------------------------------------------------------------------------------------
public class DerivateAnalyzer extends AbstractItem {
	private float lastValue;
	private long lastTime;

	private float derive;
	//---statics:

//-------------------------------------------------------------------------------------
    public DerivateAnalyzer(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    	lastValue = Float.NaN;
    	lastTime = 0;
    	derive = 0f;
    }

//-------------------------------------------------------------------------------------
//aprox. derive:
// the higher and close to eachother the more result will -> 1.0f
//-------------------------------------------------------------------------------------
    public float derive(float _value, long _time){
    	if(lastTime == 0){
    		lastValue = _value;
			lastTime = _time;
    		return ProbabilityAnalyzer.DEFAULT_MIN_PROBABILITY;
    	} else {
//    		debug.outln(Debug.INFO, "DerivateAnalyzer.0: _value="+_value+", _time="+_time+", lastValue="+lastValue+", lastTime="+lastTime);
			long _timeDiff = Math.abs(_time - lastTime);
			float _valueMultiplied = _value * lastValue;
			
			float _timeDiffFloat = ((float)_timeDiff)/1000;//difference of time in seconds

//    		debug.outln(Debug.INFO, "DerivateAnalyzer.1: _valueMultiplied="+_valueMultiplied+", _timeDiff="+_timeDiffFloat);
			float _derivate = _valueMultiplied * MathUtils.normalize_Infinity0(_timeDiffFloat);

			lastValue = _value;
			lastTime = _time;
//    		debug.outln(Debug.INFO, "DerivateAnalyzer.2: _derivate="+_derivate);
    		return _derivate;
    	}
    }

//-------------------------------------------------------------------------------------
    public void setTime(long _lastTime) {
    	lastTime = _lastTime;
    }

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
		return "lastValue="+lastValue + ", lastTime=" + lastTime;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
