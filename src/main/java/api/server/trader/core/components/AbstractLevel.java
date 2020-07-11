//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.server.trader.*;
import codex.xbit.api.common.aspects.*;

//-------------------------------------------------------------------------------------
public abstract class AbstractLevel extends AbstractItem implements LevelAspect {
	protected String level;


//-------------------------------------------------------------------------------------
    public AbstractLevel(Debug _debug, Resolver _resolver, String _level){
    	super(null, _resolver, _level);
    	String _prefixDirectory = _level.toLowerCase();
    	debug = new Debug(null, 0, false, _debug, null, 0, _prefixDirectory);

    	level = _level;
//    	_debug.out(Debug.INFO, "..." + _level, false);
    }

//-------------------------------------------------------------------------------------
    public String getLevel(){
    	return level;
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
