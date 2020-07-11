//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level0.userexchanges;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//import codex.xbit.api.server.trader.env.users.level1.repositories.*;


//-------------------------------------------------------------------------------------
public class UserExchanges extends AbstractCluster<AccountConfiguration, UserExchange> {

//-------------------------------------------------------------------------------------
    public UserExchanges(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "accounts");
    	String _path = resolver.getUserHome() + "/accounts";
        configurationLoader = new ConfigurationLoader<AccountConfiguration>(_debug, _path, ".yaml", AccountConfiguration.class);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<UserExchange> getUserExchanges(List<String> _names) {
    	List<UserExchange> _list = new ArrayList<UserExchange>();
    	for (int i = 0; i < _names.size(); i++) {
    		_list.add(getUserExchange(_names.get(i)));
    	}
    	return _list;
	}

//-------------------------------------------------------------------------------------
    public synchronized UserExchange getUserExchange(String _name) {
//		debug.outln("UserExchanges.getUserExchange ...."+_name);
		Key _key = new Key(_name);
		UserExchange _userExchange = getEntity(_key);
		if(_userExchange == null){
			_userExchange = load(_name);
		}
		return _userExchange;
	}

//-------------------------------------------------------------------------------------
    public Object getInfo(int _netCommandParameter) {
        List<String> _list = new ArrayList<String>();
        for (int i = 0; i < entities.size(); i++) {
            UserExchange _exchange = entities.get(i);
            _list.addAll(_exchange.getInfo());
        }
        return (Object)_list;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public UserExchange loadEntity(AccountConfiguration _accountConfiguration) {
		UserExchange _exchange = new UserExchange(debug, resolver, _accountConfiguration, configurationLoader);
		Key _key = new Key(_accountConfiguration.getName());
//		debug.outln("UserExchanges.loadEntity ...."+_accountConfiguration.getName());
		addEntity(_key, _exchange);
		return _exchange;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void initiate() {
    	super.initiate();
    }*/
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
