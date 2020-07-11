//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto.risky;
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
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.marketanalyzer.*;

//-------------------------------------------------------------------------------------
public class RiskyPilot extends AutoPilot {

	//---statics:

//-------------------------------------------------------------------------------------
    public RiskyPilot(Debug _debug, Resolver _resolver, String _name, Pilots _pilots, MarketAnalyzer _marketAnalyzer) {
    	super(_debug, _resolver, _name, _pilots, _marketAnalyzer);
    }

//-------------------------------------------------------------------------------------
    protected void takeDecisionOnMarketChanges(int _traderState, float _bearProbability, float _bullProbability, Event _event){
    	if(_bearProbability >= bearProbability){
    		lastTraderState = TraderConfiguration.STATE_BEAR;
    		lastConfidence = _bearProbability;
    	} else if(_bullProbability >= bullProbability){
    		lastTraderState = TraderConfiguration.STATE_BULL;
    		lastConfidence = _bullProbability;
    	} else {
    		lastTraderState = TraderConfiguration.STATE_NORMAL;
    		lastConfidence = 1.0f;
    	}
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
		return "RiskyPilot:" + configuration;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
