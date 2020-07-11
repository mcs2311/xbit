//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level7.tactics;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.knowm.xchange.currency.CurrencyPair;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
//import codex.xbit.api.server.trader.common.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public class Tactics extends AbstractCluster<TacticConfiguration, Tactic> {

//-------------------------------------------------------------------------------------
    public Tactics(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "tactics");
    	String _path = _resolver.getUserHome() + "/tactics";
        configurationLoader = new ConfigurationLoader<TacticConfiguration>(_debug, _path, ".yaml", TacticConfiguration.class);
//        load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Tactic getTactic(String _name){
    	Key _key = new Key(_name);
		Tactic _tactic = getEntity(_key);
    	return _tactic;
    }

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter, String _tacticName){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_ORDERBOOKS: 
			case NetCommand.COMMAND_SHOW_CURRENCYPAIRS: {
        		return getTactic(_tacticName).getInfo(_netCommandParameter);
			}
			case NetCommand.COMMAND_SHOW_TACTICS: {
				List<Object> _list = new ArrayList<Object>();
		        List<Tactic> _tactics = getEntities();
		        for (int i = 0; i < _tactics.size(); i++) {
		            Tactic _tactic = _tactics.get(i);
		            if(_tactic != null){
//			    		debug.outln(Debug.IMPORTANT3, "_tactic="+_tactic.getName()+", _tacticName="+_tacticName);
			            _list.add(_tactic.getConfiguration());
			        }
		        }
		        return (Object)_list;
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
    public Tactic loadEntity(TacticConfiguration _tacticConfiguration) {
        Tactic _tactic = new Tactic(debug, resolver, _tacticConfiguration, configurationLoader);
    	Key _key = new Key(_tacticConfiguration.getName());
//    	debug.outln("Adding tactic to "+resolver.getUserName()+"...:"+_tactic);
		addEntity(_key, _tactic);
		return _tactic;
    }

//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
