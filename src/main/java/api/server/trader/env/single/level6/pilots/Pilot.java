//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level6.pilots;
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


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.*;

//-------------------------------------------------------------------------------------
public class Pilot extends AbstractEntity<PilotConfiguration> {
	protected Pilots pilots;

	protected TraderConfiguration traderConfiguration;
	protected int state;

	//---statics:
	protected static final int STATE_ACTIVE 	= 1;
	protected static final int STATE_PAUSED 	= 2;

	public static final int DEBUG_PREFIX_INDEX_PILOT_MODE 		= 0;
	public static final int DEBUG_PREFIX_INDEX_TRADER_MODE 		= 1;
	public static final int DEBUG_PREFIX_INDEX_TRADER_STATE 	= 2;

//-------------------------------------------------------------------------------------
    public Pilot(Debug _debug, Resolver _resolver, PilotConfiguration _configuration, String _name, Pilots _pilots) {
    	super(_debug, _resolver, _name, _configuration);
    	pilots = _pilots;
    	traderConfiguration = _resolver.getTraderConfiguration();

    	state = STATE_PAUSED;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void setPilotMode(int _mode, String _timeText) {
    	configuration.setMode(_mode, prepareTimeText(_timeText));
        debug.setPrefix(DEBUG_PREFIX_INDEX_PILOT_MODE, configuration.getModeColor(), configuration.getDescriptor());
    }

//-------------------------------------------------------------------------------------
    public void setPilotType(String _type) {
    	configuration.setType(_type);
        debug.setPrefix(DEBUG_PREFIX_INDEX_PILOT_MODE, configuration.getModeColor(), configuration.getDescriptor());
    }

//-------------------------------------------------------------------------------------
    public void setTraderMode(int _mode, String _timeText) {
    	traderConfiguration.setMode(_mode, prepareTimeText(_timeText));
        debug.setPrefix(DEBUG_PREFIX_INDEX_TRADER_MODE, traderConfiguration.getModeColor(), traderConfiguration.getModeAsString());		
    }

//-------------------------------------------------------------------------------------
    public void setTraderState(int _state, String _timeText, float _confidence) {
    	int _oldState = traderConfiguration.getState();
    	traderConfiguration.setState(_state, prepareTimeText(_timeText));
        debug.setPrefix(DEBUG_PREFIX_INDEX_TRADER_STATE, traderConfiguration.getStateColor(), traderConfiguration.getStateAsString());		
//        sendPilotEvent(_oldState, _state, _confidence);
    }
    
//-------------------------------------------------------------------------------------
    public String prepareTimeText(String _timeText) {
    	if(!_timeText.isEmpty() && !_timeText.equals("indefinitely")){
    		_timeText = "for " + _timeText;
    	}
    	return _timeText + " by [" + getName() + "] pilot";
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized void pause() {
    	state = STATE_PAUSED;
    }

//-------------------------------------------------------------------------------------
    public synchronized void resume(){
		setPilotType(getName());		
    	state = STATE_ACTIVE;
    }

//-------------------------------------------------------------------------------------
    protected void sendPilotEvent(int _oldState, int _newState, float _confidence){
    	PilotEvent _pilotEvent = null;
    	if(_oldState == _newState){
    		return;
    	}
    	if(_oldState == TraderConfiguration.STATE_BEAR){
    		_pilotEvent = new PilotEvent(PilotEvent.PILOT_BEAR_END, _confidence);
    	} else if(_oldState == TraderConfiguration.STATE_BULL){
    		_pilotEvent = new PilotEvent(PilotEvent.PILOT_BULL_END, _confidence);
    	} else if(_newState == TraderConfiguration.STATE_BEAR){
    		_pilotEvent = new PilotEvent(PilotEvent.PILOT_BEAR_BEGIN, _confidence);
    	} else if(_newState == TraderConfiguration.STATE_BULL){
    		_pilotEvent = new PilotEvent(PilotEvent.PILOT_BULL_BEGIN, _confidence);
    	}
    	if(_pilotEvent != null){
			pilots.onNext(_pilotEvent);
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
//    	super.save();//disable pilotConfiguration save()
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "Pilot:" + configuration;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
