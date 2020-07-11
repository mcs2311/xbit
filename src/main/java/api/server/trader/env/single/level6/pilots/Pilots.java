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

import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.manual.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.marketanalyzer.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto.safe.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto.balanced.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.helpers.auto.risky.*;

//-------------------------------------------------------------------------------------
public class Pilots extends AbstractCluster<PilotConfiguration, Pilot> implements FlowableOnSubscribe<PilotEvent>, Runnable {
	private PilotConfiguration pilotConfiguration;
	private TraderConfiguration traderConfiguration;

	private Pilot currentPilot, lastAutoPilot;
	private int state;
	private long timer;
	private Thread thisThread;
	private MarketAnalyzer marketAnalyzer;

    //---rx:
	protected Flowable<PilotEvent> flowable;
	protected FlowableEmitter<PilotEvent> emitter;

    //---statics:
	private static final int STATE_INIT 	= 0;
	private static final int STATE_TIMER 	= 1;
	private static final int STATE_PAUSED 	= 2;
	private static final int STATE_STOPPED 	= 3;

	private static final long DEFAULT_TIMER = Integer.MAX_VALUE;

//-------------------------------------------------------------------------------------
    public Pilots(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);

        String _home = SystemUtils.getHomeDirectory();
		String _path = _home + "/" + resolver.getServerConfiguration().getPathToSettings();

		@SuppressWarnings("unchecked")
    	ConfigurationLoader<PilotConfiguration> _pilotConfigurationLoader = new ConfigurationLoader(_debug, _path, PilotConfiguration.class);
    	pilotConfiguration = _pilotConfigurationLoader.load("pilot");
    	resolver.setPilotConfiguration(pilotConfiguration);



		@SuppressWarnings("unchecked")
    	ConfigurationLoader<TraderConfiguration> _traderConfigurationLoader = new ConfigurationLoader(debug, _path, TraderConfiguration.class);
    	traderConfiguration = _traderConfigurationLoader.load("trader");
    	resolver.setTraderConfiguration(traderConfiguration);

    	marketAnalyzer = new MarketAnalyzer(debug, resolver);
    	addPilots();

    	String _type = pilotConfiguration.getType();
    	setCurrentPilot(getPilot(_type));

    	flowable = Flowable.create(this, BackpressureStrategy.DROP)
    			.share()
    			.onBackpressureBuffer(16, () -> { },
              		BackpressureOverflowStrategy.DROP_OLDEST);

    	show();
    	state = STATE_TIMER;
    	timer = DEFAULT_TIMER;
    	thisThread = new Thread(this);
    	thisThread.start();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void run(){
		while(state != STATE_STOPPED){
			try{
				long _timer = timer * 1000;
//				debug.outln("Pilot timer = " + _timer);
				Thread.sleep(_timer);
		    	timer = DEFAULT_TIMER;
		    	setCurrentPilot(lastAutoPilot);
		    	currentPilot.setPilotMode(PilotConfiguration.MODE_AUTO, "");
		    	currentPilot.setTraderState(TraderConfiguration.STATE_NORMAL, "", 1.0f);
			}catch(InterruptedException _e){

			}
		}
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private void addPilots(){
		addPilot("manual");
		addPilot("safe");
		addPilot("balanced");
		addPilot("risky");
	}

//-------------------------------------------------------------------------------------
	private void addPilot(String _name){
//		debug.outln(Debug.INFO, "getSignal_arg=["+_arg+"]...");
		Key _key = new Key(_name);
		Pilot _pilot = createPilot(_name);
		if(_pilot != null){
			addEntity(_key, _pilot);
		} else {
			debug.outln(Debug.WARNING, "null pilot in Pilots...");
		}
	}

//-------------------------------------------------------------------------------------
	private Pilot getPilotByAlias(String _alias){
		switch(_alias){
			case "saf" : {
				return getPilot("safe");
			}
			case "bal" : {
				return getPilot("balanced");
			}
			case "rsk" : {
				return getPilot("risky");
			}
		}
		return getPilot(_alias);
	}

//-------------------------------------------------------------------------------------
	private Pilot getPilot(String _name){
		Key _key = new Key(_name);
		Pilot _pilot = getEntity(_key);
		if(_pilot == null){
			debug.outln(Debug.ERROR, "Cannot find pilot: " + _name);
		}
		return _pilot;
	}

//-------------------------------------------------------------------------------------
	private Pilot createPilot(String _name){
    	switch(_name){
    		case "manual": {
    			return new ManualPilot(debug, resolver, _name, this);
    		}
    		case "safe": {
    			return new SafePilot(debug, resolver, _name, this, marketAnalyzer);
    		}
    		case "balanced": {
    			return new BalancedPilot(debug, resolver, _name, this, marketAnalyzer);
    		}
    		case "risky": {
    			return new RiskyPilot(debug, resolver, _name, this, marketAnalyzer);
    		}
    		default: {
    			debug.outln(Debug.ERROR, "Cannot fing pilot name:" + _name);
    			return null;
    		}
    	}
	}

//-------------------------------------------------------------------------------------
	private synchronized void setCurrentPilot(Pilot _pilot){
		if(currentPilot == null){
			currentPilot = _pilot;
    		lastAutoPilot = (_pilot instanceof AutoPilot) ? _pilot : getPilot("balanced");
		} else if(currentPilot instanceof AutoPilot){
    		lastAutoPilot = currentPilot;
		}
		currentPilot.pause();
		currentPilot = _pilot;
		currentPilot.resume();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void setPilotMode(int _mode, Object _message) {
    	String _timeString = extractTime(_message);
    	timer = TimeUtils.getSeconds(_timeString);

    	if(_mode == PilotConfiguration.MODE_AUTO){
    		setCurrentPilot(lastAutoPilot);    	
    	} else if(_mode == PilotConfiguration.MODE_MANUAL){
    		setCurrentPilot(getPilot("manual"));
    	}

    	currentPilot.setPilotMode(_mode, _timeString);
    	thisThread.interrupt();
    }

		@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    public void setPilotType(Object _message) {
   		if(_message == null){
   			return;
   		}
		String[] _parameters = (String[])_message;
		if(_parameters.length < 2){
			return;
		}
    	String _type = _parameters[1];
    	Pilot _pilot = getPilotByAlias(_type);
    	if(_pilot != null){
	    	setCurrentPilot(_pilot);
//	    	currentPilot.setPilotType(_type);
//	    	currentPilot.setPilotType(_type);
	    	thisThread.interrupt();
    	}
    }

//-------------------------------------------------------------------------------------
    public void setTraderMode(int _mode, Object _message) {
    	currentPilot.setTraderMode(_mode, "");
    }

//-------------------------------------------------------------------------------------
    public void setTraderState(int _state, Object _message) {
    	String _timeString = extractTime(_message);
    	timer = TimeUtils.getSeconds(_timeString);

    	setCurrentPilot(getPilot("manual"));
    	currentPilot.setPilotMode(PilotConfiguration.MODE_MANUAL, _timeString);
    	currentPilot.setTraderState(_state, "", 0.1f);

    	thisThread.interrupt();
    }

@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    private String extractTime(Object _message) {
    	String _timeString = "indefinitely";
    	if(_message != null){
    		String[] _parameters = (String[])_message;
    		if(_parameters.length > 1){
    			_timeString = _parameters[1];
    		}
    	}
    	return _timeString;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private void show(){
		if(currentPilot == null){
			debug.outln(Debug.ERROR, "Cannot start Pilots without a velid pilot!");
			return;
		}
        debug.out(Debug.IMPORTANT1, "Switching pilot to the [");
        debug.out(pilotConfiguration.getModeColor(), pilotConfiguration.getDescriptor(), false);
        debug.out(Debug.IMPORTANT1, "][", false);

        debug.out(traderConfiguration.getModeColor(), traderConfiguration.getModeAsString(), false);
        debug.out(Debug.IMPORTANT1, "][", false);

        debug.out(traderConfiguration.getStateColor(), traderConfiguration.getStateAsString(), false);
        debug.outln(Debug.IMPORTANT1, "] mode...", false);

        debug.setPrefix(Pilot.DEBUG_PREFIX_INDEX_PILOT_MODE		, pilotConfiguration.getModeColor()		, pilotConfiguration.getDescriptor());		
        debug.setPrefix(Pilot.DEBUG_PREFIX_INDEX_TRADER_MODE	, traderConfiguration.getModeColor()	, traderConfiguration.getModeAsString());		
        debug.setPrefix(Pilot.DEBUG_PREFIX_INDEX_TRADER_STATE	, traderConfiguration.getStateColor()	, traderConfiguration.getStateAsString());
	}

//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<PilotEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<PilotEvent> getPilotEvent() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
	public void onNext(PilotEvent _pilotEvent) {
		if(emitter != null){
			emitter.onNext(_pilotEvent);
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
 		marketAnalyzer.load();
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	super.save();
    	state = STATE_STOPPED;
//    	try{
    		thisThread.interrupt();
//    	}catch(InterruptedException _e){}
 		marketAnalyzer.save();
    	pilotConfiguration.save();
    	traderConfiguration.save();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "Pilots:";
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
