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
public class OrderbookMarketModel {
	private Debug debug;
	
	private float maxRatioLog;
	private float maxSizeSqrt;

    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public OrderbookMarketModel(Debug _debug) {
        debug = _debug;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public MarketModelEvent onNextOrderbook(OrderbookEvent _orderbookEvent){
		MarketModelEvent _marketModelEvent = null;
    	long _bidsSize = _orderbookEvent.getBidsSize();
    	long _asksSize = _orderbookEvent.getAsksSize();
		float _ratio;
		if(_bidsSize >= _asksSize){
			_ratio = ((float)_bidsSize)/_asksSize;
		} else {
			_ratio = ((float)_asksSize)/_bidsSize;
		}

		float _ratioLog = (float)Math.log(_ratio);
		maxRatioLog = Math.max(maxRatioLog, _ratioLog);


		float _sizeSqrt = (float)Math.sqrt(_bidsSize + _asksSize);
		maxSizeSqrt = (float)Math.max(maxSizeSqrt, _sizeSqrt);

		float _ratio0 = MathUtils.normalize_01f(_ratioLog, maxRatioLog);
		float _size0 = MathUtils.normalize_01f(_sizeSqrt, maxSizeSqrt);

		float _confidence = _ratio0 * _size0;

		_confidence = (float)Math.min(_confidence, 0.95f);
		
//		debug.outln(Debug.IMPORTANT3, "OrderbookMarketModel>>_ratioLog="+_ratioLog+", maxRatioLog="+maxRatioLog+", _sizeSqrt="+_sizeSqrt+", maxSizeSqrt="+maxSizeSqrt+", _ratio0="+_ratio0+", _size0="+_size0);

		if(_confidence > 0.1f){
			if(_bidsSize >= _asksSize){
				_marketModelEvent = new MarketModelEvent(MarketModelEvent.MARKET_GOING_UP, _orderbookEvent, _confidence);
			} else {
				_marketModelEvent = new MarketModelEvent(MarketModelEvent.MARKET_GOING_DOWN, _orderbookEvent, _confidence);
			}
		}


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
		return "OrderbookMarketModel:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
