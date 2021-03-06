//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level6;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.env.users.level6.pairs.*;

//-------------------------------------------------------------------------------------
public class Level6 extends AbstractLevel {

    private Pairs pairs;

//-------------------------------------------------------------------------------------
    public Level6(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "L6");
        pairs = new Pairs(debug, _resolver);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public Pairs getPairs(){
		return pairs;
	}

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter, String _tactic){
//		return pairs.getInfo(_netCommandParameter, _tactic);
		return null;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	debug.outln(Debug.IMPORTANT3, "Level6.load...");
        pairs.load();
	}

//-------------------------------------------------------------------------------------
    public void save(){
//    	debug.outln(Debug.IMPORTANT3, "Level2.save...");
    	if(pairs != null){
        	pairs.save();
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------