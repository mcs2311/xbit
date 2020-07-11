//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level4.marketmodels.helpers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;


//-------------------------------------------------------------------------------------
public class TradeMarketModel {
	private Debug debug;

	private float lastTradePrice;
	private float maxPositiveVariation;
	private float maxNegativeVariation;
    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public TradeMarketModel(Debug _debug) {
        debug = _debug;
        lastTradePrice = 0f;
        maxPositiveVariation = 0f;
        maxNegativeVariation = 0f;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public MarketModelEvent onNextTrade(TradeEvent _tradeEvent){
		MarketModelEvent _marketModelEvent = null;
    	float _currentPrice = _tradeEvent.getPrice().floatValue();

    	if(lastTradePrice == 0f){
    		lastTradePrice = _currentPrice;
    		return null;
    	}

    	float _variation = MathUtils.variation(_currentPrice, lastTradePrice);
    	float _variationAbs = Math.abs(_variation);

    	if(_variation < 0){//lastTradePrice < _currentPrice
			if(_variationAbs > 0.005){
				maxNegativeVariation = Math.max(maxNegativeVariation, _variationAbs);
				float _confidence = MathUtils.normalize_01f(_variationAbs, maxNegativeVariation);
				_confidence = Math.min(_confidence, 0.95f);
				_marketModelEvent = new MarketModelEvent(MarketModelEvent.MARKET_GOING_UP, _tradeEvent, _confidence);
			}
    	} else {//lastTradePrice > _currentPrice
			if(_variationAbs > 0.005){
				maxPositiveVariation = Math.max(maxPositiveVariation, _variationAbs);
				float _confidence = MathUtils.normalize_01f(_variationAbs, maxPositiveVariation);
				_confidence = Math.min(_confidence, 0.95f);
				_marketModelEvent = new MarketModelEvent(MarketModelEvent.MARKET_GOING_DOWN, _tradeEvent, _confidence);
			}
    	}
		
		lastTradePrice = _currentPrice;
		
		return _marketModelEvent;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {

	}

//-------------------------------------------------------------------------------------
    public void save() {
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "TradeMarketModel:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
