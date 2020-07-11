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
public class TickerMarketModel {
	private Debug debug;

	private float lastAskPrice;
	private float lastBidPrice;

	private float maxAskXBidPositiveVariation;
	private float maxAskXBidNegativeVariation;

    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public TickerMarketModel(Debug _debug) {
        debug = _debug;
		lastAskPrice = 0f;
		lastBidPrice = 0f;
		maxAskXBidPositiveVariation = 0f;
		maxAskXBidNegativeVariation = 0f;
    }

//-------------------------------------------------------------------------------------
	public BigDecimal getLastAskPrice(){
		return BigDecimal.valueOf(lastAskPrice);
	}

//-------------------------------------------------------------------------------------
	public BigDecimal getLastBidPrice(){
		return BigDecimal.valueOf(lastBidPrice);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public MarketModelEvent onNextTicker(TickerEvent _tickerEvent){
		MarketModelEvent _marketModelEvent = null;
		float _currentAskPrice = _tickerEvent.getAskPrice().floatValue();
		float _currentBidPrice = _tickerEvent.getBidPrice().floatValue();

    	if((lastAskPrice == 0f) || (lastBidPrice == 0f)){
    		lastAskPrice = _currentAskPrice;
    		lastBidPrice = _currentBidPrice;
    		return null;
    	}


    	float _variationAsk = MathUtils.variation(_currentAskPrice, lastAskPrice);
    	float _variationAskAbs = Math.abs(_variationAsk);

    	float _variationBid = MathUtils.variation(_currentBidPrice, lastBidPrice);
    	float _variationBidAbs = Math.abs(_variationBid);

    	if((_variationAsk < 0) && (_variationBid < 0)){
			if((_variationAskAbs > 0.005) && (_variationBidAbs > 0.005)){
				float _askXBidPositiveVariation = _variationAskAbs * _variationBidAbs;
				maxAskXBidPositiveVariation = Math.max(maxAskXBidPositiveVariation, _askXBidPositiveVariation);
				float _confidence = MathUtils.normalize_01f(_askXBidPositiveVariation, maxAskXBidPositiveVariation);
				_confidence = Math.min(_confidence, 0.95f);
				_marketModelEvent = new MarketModelEvent(MarketModelEvent.MARKET_GOING_UP, _tickerEvent, _confidence);
			}
    	} else if((_variationAsk > 0) && (_variationBid > 0)){
			if((_variationAskAbs > 0.005) && (_variationAskAbs > 0.005)){
				float _askXBidNegativeVariation = _variationAskAbs * _variationBidAbs;
				maxAskXBidNegativeVariation = Math.max(maxAskXBidNegativeVariation, _askXBidNegativeVariation);
				float _confidence = MathUtils.normalize_01f(_askXBidNegativeVariation, maxAskXBidNegativeVariation);
				_confidence = Math.min(_confidence, 0.95f);
				_marketModelEvent = new MarketModelEvent(MarketModelEvent.MARKET_GOING_DOWN, _tickerEvent, _confidence);
			}
    	}

		lastAskPrice = _currentAskPrice;
		lastBidPrice = _currentBidPrice;

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
		return "TickerMarketModel:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
