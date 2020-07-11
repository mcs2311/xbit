//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level6.pilots.helpers.manual;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.jline.utils.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.*;

//-------------------------------------------------------------------------------------
public class ManualPilot extends Pilot {

	//---statics:

//-------------------------------------------------------------------------------------
    public ManualPilot(Debug _debug, Resolver _resolver, String _name, Pilots _pilots) {
    	super(_debug, _resolver, _resolver.getPilotConfiguration(), _name, _pilots);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	super.save();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "ManualPilot:" + configuration;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
