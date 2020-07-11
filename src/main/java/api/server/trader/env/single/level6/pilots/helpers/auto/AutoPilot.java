//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto;
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
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.marketanalyzer.*;

//-------------------------------------------------------------------------------------
public abstract class AutoPilot extends Pilot {
	protected MarketAnalyzer marketAnalyzer;
	protected float bearProbability;
	protected float bullProbability;


	protected int lastTraderState;
	protected float lastConfidence;
	//---statics:

//-------------------------------------------------------------------------------------
    public AutoPilot(Debug _debug, Resolver _resolver, String _name, Pilots _pilots, MarketAnalyzer _marketAnalyzer) {
    	super(_debug, _resolver, _resolver.getPilotConfiguration(), _name, _pilots);
    	marketAnalyzer = _marketAnalyzer;
    	Map<String, Map<String, Double>> _probabilities = configuration.getProbabilities();
    	Map<String, Double> _probabilitiesThisPilot = _probabilities.get(_name);
    	if(_probabilitiesThisPilot != null){
    		bearProbability = _probabilitiesThisPilot.get("bear").floatValue();
	    	bullProbability = _probabilitiesThisPilot.get("bull").floatValue();
    	} else {
    		debug.outln(Debug.ERROR, "Could not fid probabilities for pilot: " + _name);
	    	bearProbability = 0f;
	    	bullProbability = 0f;
    	}
    	lastTraderState = -1;
    	lastConfidence = 0f;
    }

//-------------------------------------------------------------------------------------
    protected abstract void takeDecisionOnMarketChanges(int _traderState, float _bearProbability, float _bullProbability, Event _event);

//-------------------------------------------------------------------------------------
    public void takeDecisionOnMarketChanges(Event _event){
    	int _traderStateInitial = traderConfiguration.getState();
    	float _bearProbability = marketAnalyzer.getBearProbability();
    	float _bullProbability = marketAnalyzer.getBullProbability();
    	takeDecisionOnMarketChanges(_traderStateInitial, _bearProbability, _bullProbability, _event);
    	if(_traderStateInitial != lastTraderState){
			changeTraderState(lastTraderState, lastConfidence);
    	}
    }

//-------------------------------------------------------------------------------------
    protected void changeTraderState(int _state, float _confidence){
    	if(state == STATE_ACTIVE){
    		setTraderState(_state, "indefinetly", _confidence);
//    		PilotEvent _pilotEvent = new PilotEvent(null, null);
//    		onNext(_pilotEvent);
    	}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized void resume(){
    	super.resume();
    	marketAnalyzer.setAutoPilot(this);
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
		return "AutoPilot:" + configuration;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
