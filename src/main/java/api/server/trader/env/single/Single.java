//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level0.*;
import codex.xbit.api.server.trader.env.single.level1.*;
import codex.xbit.api.server.trader.env.single.level2.*;
import codex.xbit.api.server.trader.env.single.level3.*;
import codex.xbit.api.server.trader.env.single.level4.*;
import codex.xbit.api.server.trader.env.single.level5.*;
import codex.xbit.api.server.trader.env.single.level6.*;


//-------------------------------------------------------------------------------------
/*

level 0 - exchanges(network level)
level 1 - repositories
level 2 - indicators
level 3 - signals
*/

//-------------------------------------------------------------------------------------
//public class Single extends Thread implements OperableAspect {
public class Single implements OperableAspect {
	private Debug debug;
	private Resolver resolver;

	private Level0 level0;
	private Level1 level1;
	private Level2 level2;
	private Level3 level3;
	private Level4 level4;
	private Level5 level5;
	private Level6 level6;

//-------------------------------------------------------------------------------------
    public Single(Debug _debug, Resolver _resolver) {
    	String _prefix = "single";
		debug = new Debug(null, 0, false, _debug, null, -1, _prefix);
//    	debug = _debug;
    	debug.outln(Debug.IMPORTANT3, StringUtils.createBanner("Single"), false);
//    	debug.out(Debug.INFO, "Initialize");
    	resolver = _resolver;
    	level0 = new Level0(debug, resolver);
    	level1 = new Level1(debug, resolver);
    	level2 = new Level2(debug, resolver);
    	level3 = new Level3(debug, resolver);
    	level4 = new Level4(debug, resolver);
    	level5 = new Level5(debug, resolver);
    	level6 = new Level6(_debug, resolver);
//    	load();
    }

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
    public void sendSignal(String[] _args) {
    	level5.sendSignal(_args);
	}

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_WHALES: {
				return level5.getInfo(_netCommandParameter);
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
    public void load() {
//    	debug.outln(Debug.INFO, "", false);
        level0.load();
		level1.load();
/*        level2.load();
        level3.load();
        level4.load();
        level5.load();*/
        level6.load();
    }

//-------------------------------------------------------------------------------------
    public void reload() {
    	save();
    	load();
	}

//-------------------------------------------------------------------------------------
    public void save(){
//    	debug.outln(Debug.IMPORTANT3, "Single.save...");
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
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
