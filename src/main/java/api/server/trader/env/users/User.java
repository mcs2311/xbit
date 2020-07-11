//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;

import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.*;
import codex.xbit.api.server.trader.env.users.level1.*;
import codex.xbit.api.server.trader.env.users.level2.*;
import codex.xbit.api.server.trader.env.users.level3.*;
import codex.xbit.api.server.trader.env.users.level4.*;
import codex.xbit.api.server.trader.env.users.level5.*;
import codex.xbit.api.server.trader.env.users.level6.*;
import codex.xbit.api.server.trader.env.users.level7.*;


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
public class User extends AbstractEntity<UserConfiguration> {
	private Level0 level0;
	private Level1 level1;
	private Level2 level2;
	private Level3 level3;
	private Level4 level4;
	private Level5 level5;
	private Level6 level6;
	private Level7 level7;

//-------------------------------------------------------------------------------------
    public User(Debug _debug, Resolver _resolver, UserConfiguration _userConfiguration) {
    	super(null, null, _userConfiguration);
    	_debug.outln(Debug.IMPORTANT3, StringUtils.createBanner("User: " + _userConfiguration.getUserName()), false);

//    	debug = _debug;
//    	debug = new Debug(null, 0, false, _debug, null, 0, _level);

    	String _prefix = _userConfiguration.getUserName();
    	debug = new Debug(null, 0, false, _debug, _prefix, Debug.IMPORTANT3, _prefix);

//    	debug.out(Debug.INFO, "Initialize");
    	resolver = new Resolver(_resolver, this);
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
    public Resolver getResolver() {
    	return resolver;
	}

//-------------------------------------------------------------------------------------
    public Level0 getLevel0() {
    	return level0;
	}

//-------------------------------------------------------------------------------------
    public Level1 getLevel1() {
    	return level1;
	}

//-------------------------------------------------------------------------------------
    public Level2 getLevel2() {
    	return level2;
	}

//-------------------------------------------------------------------------------------
    public Level3 getLevel3() {
    	return level3;
	}

//-------------------------------------------------------------------------------------
    public Level4 getLevel4() {
    	return level4;
	}


//-------------------------------------------------------------------------------------
    public Level5 getLevel5() {
    	return level5;
	}

//-------------------------------------------------------------------------------------
    public Level6 getLevel6() {
    	return level6;
	}

//-------------------------------------------------------------------------------------
    public Level7 getLevel7() {
    	return level7;
	}

//-------------------------------------------------------------------------------------
    public String getUserName() {
    	return configuration.getUserName();
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter, String _tactic){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_EXCHANGES: {
				return level0.getInfo(_netCommandParameter);
			}
			case NetCommand.COMMAND_SHOW_ORDERBOOKS: 
			case NetCommand.COMMAND_SHOW_TACTICS: 
			case NetCommand.COMMAND_SHOW_CURRENCYPAIRS: {
				return level2.getInfo(_netCommandParameter, _tactic);
			}

			default: {
				return (Object)(new ArrayList<String>(Arrays.asList("Command not found:" + _netCommandParameter)));
			}
		}
	}

//-------------------------------------------------------------------------------------
    public void changeOrderbook(String[] _args) {
//    	level4.changeOrderbook(_args);
	}

//-------------------------------------------------------------------------------------
    public void changeProfit(String[] _args) {
//    	level4.changeProfit(_args);
	}

//-------------------------------------------------------------------------------------
    public void changeStrategy(String[] _args) {
//    	level2.changeStrategy(_args);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//    	debug.outln(Debug.INFO, "", false);
    	level0 = new Level0(debug, resolver);
    	level1 = new Level1(debug, resolver);
    	level2 = new Level2(debug, resolver);
    	level3 = new Level3(debug, resolver);
    	level4 = new Level4(debug, resolver);
    	level5 = new Level5(debug, resolver);
    	level6 = new Level6(debug, resolver);
    	level7 = new Level7(debug, resolver);
		
		resolver.load();
    	level3.load();
		level7.load();
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	if(level0 != null){
        	level0.save();
    	}
    	if(level1 != null){
        	level1.save();
        }
    	if(level2 != null){
        	level2.save();
        }
    	if(level3 != null){
        	level3.save();
    	}
    	if(level4 != null){
        	level4.save();
        }
    	if(level5 != null){
        	level5.save();
        }
    	if(level6 != null){
        	level6.save();
        }
    	if(level7 != null){
        	level7.save();
        }
	}

//-------------------------------------------------------------------------------------
    public String toString() {
		return "User:" + 
		getUserName();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
