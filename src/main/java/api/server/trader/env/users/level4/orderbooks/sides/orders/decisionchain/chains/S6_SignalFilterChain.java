//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;

import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;

//-------------------------------------------------------------------------------------
public class S6_SignalFilterChain {
	protected Debug debug;
	protected int type;

	private OrderX order;
	private TraderConfiguration traderConfiguration;
	private TacticConfiguration tacticConfiguration;

	private float shallIBuy_confidence;
	private float shallIBuy_wiseness;

	private float shallISell_confidence;
	private float shallISell_wiseness;

	private float recalculateBuy_confidence;
	private float recalculateBuy_wiseness;

	private float recalculateSell_confidence;
	private float recalculateSell_wiseness;

	private float boosts_bear;
	private float boosts_panic;
	private float boosts_manual;

	private boolean canDoShorts;
    //---cache:

    //---statics:
	public static final int BROKER_MODE		= 0;
	public static final int AGENT_MODE		= 1;
@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    public S6_SignalFilterChain(Debug _debug, Resolver _resolver, int _type, TacticConfiguration _tacticConfiguration, OrderX _order) {
    	debug = _debug;
    	type = _type;
		tacticConfiguration = _tacticConfiguration;
		order = _order;

		traderConfiguration = _resolver.getTraderConfiguration();
//		String _path = SystemUtils.getHomeDirectory() + "/" + _resolver.getServerConfiguration().getPathToSettings();
//    	ConfigurationLoader<FiltersConfiguration> _configurationLoader = new ConfigurationLoader(debug, _path, FiltersConfiguration.class);
    	FiltersConfiguration _configuration = (FiltersConfiguration)_resolver.getCachedConfiguration("filters");
    	Map<String, Map<String, Float>> _signals = _configuration.getSignals();
	
		shallIBuy_confidence = _signals.get("shallIBuy").get("confidence");
		shallIBuy_wiseness = _signals.get("shallIBuy").get("wiseness");

		shallISell_confidence = _signals.get("shallISell").get("confidence");
		shallISell_wiseness = _signals.get("shallISell").get("wiseness");

		recalculateBuy_confidence = _signals.get("recalculateBuy").get("confidence");
		recalculateBuy_wiseness = _signals.get("recalculateBuy").get("wiseness");

		recalculateSell_confidence = _signals.get("recalculateSell").get("confidence");
		recalculateSell_wiseness = _signals.get("recalculateSell").get("wiseness");


		boosts_bear = _signals.get("boosts").get("bear");
		boosts_panic = _signals.get("boosts").get("panic");
		boosts_manual = _signals.get("boosts").get("manual");

		canDoShorts = _tacticConfiguration.isFlagEnabled("canDoShorts");
    }

//-------------------------------------------------------------------------------------
    public int filter(int _mode, Event _event, float _wiseness){
		int _traderState = traderConfiguration.getState();
/*		int _source = -1;
		if(_event instanceof SignalEvent){
			SignalEvent _signalEvent = (SignalEvent)_event;
			_source = _signalEvent.getSource();    	
		} */
		debug.outln(Debug.ERROR, "filter: _traderState=" + _traderState + ", _event=" + _event + ", _wiseness=" + _wiseness);
    	if(type == AbstractOrder.TYPE_BUY){//Order.OrderType.ASK){
    		return filterBid(_mode, _traderState, _event, _wiseness);    		
    	} else {
    		return filterAsk(_mode, _traderState, _event, _wiseness);
    	}
    }

//-------------------------------------------------------------------------------------
    private int filterBid(int _mode, int _traderState, Event _event, float _wiseness){
		int _source = -1;
		if(_event instanceof SignalEvent){
			SignalEvent _signalEvent = (SignalEvent)_event;
			_source = _signalEvent.getSource();    	
		} 

		float _confidence = _event.getConfidence();    	

		if((_traderState == TraderConfiguration.STATE_PANIC) ||
			(_traderState == TraderConfiguration.STATE_BEAR)){
			return Decision.DECISION_ABANDON_ORDER;
		}

		if(_source == SignalEvent.SOURCE_MANUAL){
			_confidence = _confidence * boosts_manual;
			_wiseness = _wiseness;// * boosts_manual;
		}

		if(_mode == BROKER_MODE){
			if((_confidence > shallIBuy_confidence) && (_wiseness > shallIBuy_wiseness)){
				return Decision.DECISION_CHANGE_ORDER;
			} else {
				return Decision.DECISION_DONT_TOUCH_ORDER;				
			}
		} else {
			if((_confidence > recalculateBuy_confidence) && (_wiseness > recalculateBuy_wiseness)){
				return Decision.DECISION_CHANGE_ORDER;
			} else {
				return Decision.DECISION_DONT_TOUCH_ORDER;				
			}			
		}
    }

//-------------------------------------------------------------------------------------
    private int filterAsk(int _mode, int _traderState, Event _event, float _wiseness){
		int _source = -1;
		if(_event instanceof SignalEvent){
			SignalEvent _signalEvent = (SignalEvent)_event;
			_source = _signalEvent.getSource();    	
		} 

		float _confidence = _event.getConfidence();    	

		if(_traderState == TraderConfiguration.STATE_BEAR){
			_confidence = _confidence * boosts_bear;
			_wiseness = _wiseness * boosts_bear;
		} else if(_traderState == TraderConfiguration.STATE_PANIC){
			_confidence = _confidence * boosts_panic;
			_wiseness = _wiseness * boosts_panic;
		}

		if(_source == SignalEvent.SOURCE_MANUAL){
			_confidence = _confidence * boosts_manual;
			_wiseness = _wiseness;// * boosts_manual;
		}


		if(_mode == BROKER_MODE){
			if((_confidence > shallISell_confidence) && (_wiseness > shallISell_wiseness)){
				return Decision.DECISION_CHANGE_ORDER;
			} else {
				return filterAskForShorts(_mode, _traderState, _event, _wiseness);
			}			
		} else {
			if((_confidence > recalculateSell_confidence) && (_wiseness > recalculateSell_wiseness)){
				return Decision.DECISION_CHANGE_ORDER;
			} else {
				return filterAskForShorts(_mode, _traderState, _event, _wiseness);
			}			
		}
    }

//-------------------------------------------------------------------------------------
    private int filterAskForShorts(int _mode, int _traderState, Event _event, float _wiseness){
		if(canDoShorts){
			if(_event instanceof PilotEvent){
				PilotEvent _pilotEvent = (PilotEvent)_event;
				if (_pilotEvent.isASellSignal()) {
					order.setPosition(AbstractOrder.POSITION_SHORT);
					return Decision.DECISION_CHANGE_ORDER;
				}
			}
		}
		return Decision.DECISION_DONT_TOUCH_ORDER;
	}
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
