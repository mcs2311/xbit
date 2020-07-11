//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users;
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
public class Users extends AbstractCluster<UserConfiguration, User> {

//-------------------------------------------------------------------------------------
    public Users(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "users");
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public User getUser(String _name){
    	Key _key = new Key(_name);
		User _user = getEntity(_key);
    	return _user;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public User loadEntity(UserConfiguration _userConfiguration) {
//    	Debug _debug = new Debug(debug, _userConfiguration.getUserName(), Debug.IMPORTANT3);
        User _user = new User(debug, resolver, _userConfiguration);
    	Key _key = new Key(_userConfiguration.getUserName());
		addEntity(_key, _user);
		_user.load();
		return _user;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public Object getInfo(int _netCommandParameter, Object _message){
		if(_netCommandParameter == NetCommand.COMMAND_SHOW_USERS){
			List<UserConfiguration> _userConfigurations = new ArrayList<UserConfiguration>();
			List<User> _usersEntities = getEntities();
			for (int i = 0; i < _usersEntities.size() ; i++) {
				_userConfigurations.add(_usersEntities.get(i).getConfiguration());
			}
			debug.outln(Debug.ERROR, "Users.getInfo... i=" + _userConfigurations.size());
			return (Object)_userConfigurations;
		}
		String _username = null;
		String _tactic = null;
		if(_message instanceof String){
			_username = (String)_message;
		} else if(_message instanceof ArrayList){
			List<String> _list = (List<String>)_message;
			_username = _list.get(0);
			_tactic = _list.get(1);
		} else {
			return (Object)(new ArrayList<String>(Arrays.asList("No such user exists!")));			
		}
		
		User _user = getUser(_username);
		if(_user == null){
			return (Object)(new ArrayList<String>(Arrays.asList("No such user exists!")));
		} else {
			return _user.getInfo(_netCommandParameter, _tactic);
		}
	}

//-------------------------------------------------------------------------------------
    public void changeOrderbook(String[] _args) {
    	getUser(_args[0]).changeOrderbook(_args);
	}

//-------------------------------------------------------------------------------------
    public void changeProfit(String[] _args) {
    	getUser(_args[0]).changeProfit(_args);
	}

//-------------------------------------------------------------------------------------
    public void changeStrategy(String[] _args) {
    	getUser(_args[0]).changeStrategy(_args);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//    	debug.outln(Debug.IMPORTANT3, "Users.load...");
    	super.load();
    	String _path = SystemUtils.getHomeDirectory() + "/" + resolver.getServerConfiguration().getPathToUsers();
        File _dir = new File(_path);
//        debug.outln("Load "+_path);
        File[] _files = _dir.listFiles(File::isDirectory);
        for (File _file : _files) {
        	String _filename = _file.getName();
//        	debug.outln("_filename= "+_filename);
//    		debug.outln(Debug.IMPORTANT3, "User: " + _filename + "... ");
//    		debug.outln(Debug.IMPORTANT3, StringUtils.createBanner("User: " + _filename), false);

        	UserConfiguration _userConfiguration = new UserConfiguration(_filename);
        	_userConfiguration.setEnabled(true);
    		loadEntity(_userConfiguration);
		}
    	debug.outln(Debug.IMPORTANT3, StringUtils.createBanner(""), false);
    }

//-------------------------------------------------------------------------------------
    public void save(){
//    	debug.outln(Debug.IMPORTANT3, "Users.save...");
		super.save();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
