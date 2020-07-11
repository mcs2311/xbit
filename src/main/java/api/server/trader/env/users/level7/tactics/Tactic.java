//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level7.tactics;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;
import java.text.*;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;
import org.ta4j.core.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level6.pairs.*;

//-------------------------------------------------------------------------------------
public class Tactic extends AbstractEntity<TacticConfiguration> {

    //---cache:
    private Pairs pairs;

//-------------------------------------------------------------------------------------
    public Tactic(Debug _debug, Resolver _resolver, TacticConfiguration _tacticConfiguration, ConfigurationLoader<TacticConfiguration> _configurationLoader) {
    	super(_debug, _resolver, _tacticConfiguration.getName(), _tacticConfiguration);
//    	debug.outln(Debug.IMPORTANT3, "Loading tactic #"+getName()+" ...");
    	if(_tacticConfiguration.isEnabled()){
	    	String _prefix = _tacticConfiguration.getName();
	    	debug = new Debug(_debug, _prefix, Debug.IMPORTANT2);

	        pairs = resolver.getPairs();
	        pairs.addPairs(debug, _tacticConfiguration);
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_ORDERBOOKS: 
			case NetCommand.COMMAND_SHOW_CURRENCYPAIRS: {
        		return pairs.getInfo(_netCommandParameter);
			}
			case NetCommand.COMMAND_SHOW_TACTICS: {
				return getConfiguration();
			}
			default: {
				return new ArrayList<String>(Arrays.asList("Command not found:" + _netCommandParameter));
			}
		}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//		debug.outln(Debug.INFO, "Tactic.load...");
		super.load();
		pairs.load();
    }


//-------------------------------------------------------------------------------------
    public void save() {
//    	debug.outln(Debug.IMPORTANT3, "Tactic.save...");
//    	super.save();
    	pairs.save();
    	//disable call of the super in order not to save the tactic file
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "Tactic:" + configuration;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
