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
public class ProbabilityAnalyzer extends AbstractItem {
	private float probability;

	private DerivateAnalyzer derivateUp;
	private DerivateAnalyzer derivateDown;

	//---statics:
	public static final float DEFAULT_MIN_PROBABILITY = 0.000001f;
	public static final float DEFAULT_MAX_PROBABILITY = 0.999999f;

//-------------------------------------------------------------------------------------
    public ProbabilityAnalyzer(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    	probability = DEFAULT_MIN_PROBABILITY;
    	derivateUp = new DerivateAnalyzer(_debug, _resolver);
    	derivateDown = new DerivateAnalyzer(_debug, _resolver);
    }


//-------------------------------------------------------------------------------------
    public void up(float _value, long _time){
//    	debug.outln(Debug.INFO, "ProbabilityAnalyzer.up.0: _value="+_value+", _time="+_time+",probability="+probability);
    	float _drivate = derivateUp.derive(_value, _time);
		probability = MathUtils.slowIncrementValue(probability, _drivate, DEFAULT_MAX_PROBABILITY);
//    	debug.outln(Debug.INFO, "ProbabilityAnalyzer.up.1: probability="+probability);
    }

//-------------------------------------------------------------------------------------
    public void down(float _value, long _time){
//    	debug.outln(Debug.INFO, "ProbabilityAnalyzer.down.0: _value="+_value+", _time="+_time+",probability="+probability);
    	float _drivate = derivateDown.derive(_value, _time);
	    probability = MathUtils.slowDecrementValue(probability, _drivate, DEFAULT_MIN_PROBABILITY);
//    	debug.outln(Debug.INFO, "ProbabilityAnalyzer.down.1: probability="+probability);
    }

//-------------------------------------------------------------------------------------
    public float get(){
    	return probability;
    }

//-------------------------------------------------------------------------------------
    public void set(float _probability, long _time){
    	probability = _probability;
//    	derivateUp.setTime(_time);
//    	derivateDown.setTime(_time);
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
		return Float.toString(probability);
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
