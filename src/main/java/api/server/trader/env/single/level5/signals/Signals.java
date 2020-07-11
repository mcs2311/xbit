//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


//import codex.xbit.api.server.trader.common.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level5.signals.helpers.internal.*;
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.*;
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.hps.*;
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.manual.*;
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.depthchart.*;

//-------------------------------------------------------------------------------------
public class Signals extends AbstractCluster<SignalConfiguration, Signal> {
	private SignalsConfiguration configuration;
	private WhaleAlertCrawler whaleAlertCrawler;

@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    public Signals(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
//    	Debug _debug0 = new Debug(_debug, "WHALE_ALERT", Debug.IMPORTANT3);
        String _home = SystemUtils.getHomeDirectory();

		String _path = _home + "/" + resolver.getServerConfiguration().getPathToSettings();
    	ConfigurationLoader<SignalsConfiguration> _configurationLoader = new ConfigurationLoader(debug, _path, SignalsConfiguration.class);
    	configuration = _configurationLoader.load("signals");


        Debug _debug0 = new Debug(_home + "/.xbit/logs/whalealert.log", true, _debug, "WHALE_ALERT", Debug.IMPORTANT3);
    	SignalConfiguration _configuration = configuration.get("whalealert");
    	whaleAlertCrawler = new WhaleAlertCrawler(_debug0, _resolver, _configuration);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<Signal> getSignals(CurrencyPair _currencyPair, List<Map<String, String>> _args) {
    	List<Signal> _signals = new ArrayList<Signal>();
    	_args.forEach(_arg -> _signals.add(getSignal(_currencyPair, _arg)));
		return _signals;
	}

//-------------------------------------------------------------------------------------
    public synchronized Signal getSignal(CurrencyPair _currencyPair, Map<String, String> _arg) {
		debug.outln(Debug.INFO, "getSignal_arg=["+_arg+"]...");
		Key _key = new Key(_currencyPair, _arg);
		Signal _signal = getEntity(_key);
		if(_signal == null){
			_signal = createSignal(_currencyPair, _arg);
			addEntity(_key, _signal);
		}
		return _signal;
	}

//-------------------------------------------------------------------------------------
    private Signal createSignal(CurrencyPair _currencyPair, Map<String, String> _arg) {
    	String _type = _arg.get("type");
//    	List<String> _arg0 = _arg.get(1, _arg.size());
    	SignalConfiguration _configuration = configuration.get(_type);
    	switch(_type){
    		case "internal": {
    			return new InternalSignal(debug, resolver, _configuration, _currencyPair, _arg);
    		}
    		case "manual": {
    			return new ManualSignal(debug, resolver, _configuration, _currencyPair, _arg);
    		}
    		case "whalealert": {
    			return whaleAlertCrawler.getWhaleAlertSignal(_currencyPair);
    		}
    		case "hps": {
    			return new HPS(debug, resolver, _configuration, _currencyPair, _arg);
    		}
    		case "depthchart": {
    			return new DepthChartSignal(debug, resolver, _configuration, _currencyPair, _arg);
    		}
    		default: {
    			debug.outln(Debug.ERROR, "Cannot fing signal type:" + _type);
    			return null;
    		}
    	}
	}

//-------------------------------------------------------------------------------------
    public void sendSignal(String[] _args) {
    	List<Signal> _signals = getEntities();
    	_signals.forEach(_signal -> {
    		if(_signal instanceof ManualSignal){
    			((ManualSignal)_signal).sendSignal(_args);
    		}
    	});
	}

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_WHALES: {
				return whaleAlertCrawler.getInfo(_netCommandParameter);
			}
			default: {
				return (Object)(new ArrayList<String>(Arrays.asList("Command not found:" + _netCommandParameter)));
			}
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void registerToAllSignals(CurrencyPair _currencyPair, List<List<String>> _signalsNames, EventListenerAspect _eventListener) {
    	List<Signal> _signals = getSignals(_currencyPair, _signalsNames);
    	_signals.forEach(_signal -> {
    			Event _event = new Event(Event.ANY, _currencyPair, null);
	    		_signal.register(_event, _eventListener);
    	});
	}
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void loadEntity(Configuration _configuration) {
//        return _signal;
    }*/

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//    	debug.outln(Debug.INFO, "Side....load...");
        super.load();
        whaleAlertCrawler.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
        super.save();
        whaleAlertCrawler.save();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
