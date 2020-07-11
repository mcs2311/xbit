//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import org.knowm.xchange.dto.Order;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;

import codex.xbit.api.server.trader.env.users.level2.shadowbalances.*;

//-------------------------------------------------------------------------------------
public class S2_ProfitExtractorChain {
	protected Debug debug;
    protected float openingPrice;

    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public S2_ProfitExtractorChain(Debug _debug, BigDecimal _openingPrice) {
    	debug = _debug;
    	openingPrice = _openingPrice.floatValue();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public float profit(float _newPrice){
    	if(openingPrice == Integer.MAX_VALUE){
    		openingPrice = _newPrice;
    	}
    	float _currentProfit = MathUtils.profit(openingPrice, _newPrice);
    	return _currentProfit;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
