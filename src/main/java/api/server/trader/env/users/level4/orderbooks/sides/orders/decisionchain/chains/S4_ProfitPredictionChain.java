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

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.*;

//-------------------------------------------------------------------------------------
public class S4_ProfitPredictionChain {
	protected Debug debug;
	protected int type;
	protected MarketModel marketModel;

	protected float lastBidProfit;
	protected float lastAskProfit;

    protected float averageBidProfitStep;
    protected float averageAskProfitStep;
    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public S4_ProfitPredictionChain(Debug _debug, int _type, MarketModel _marketModel) {
    	debug = _debug;
    	type = _type;
    	marketModel = _marketModel;
    	lastBidProfit = 0f;
    	lastAskProfit = 0f;
		averageBidProfitStep = 0f;
		averageAskProfitStep = 0f;
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public float predict(Event _event, float _currentProfit){
    	float _marketDirection = extractMarketDirection(_event);
    	float _predictedProfit = predict(_marketDirection, _currentProfit);
    	return _predictedProfit;
    }

//-------------------------------------------------------------------------------------
    private float extractMarketDirection(Event _event){
//		SignalEvent _signalEvent = (SignalEvent)_event;
    	float _marketDirection = 0f;
    	int _direction = MarketModelEvent.MARKET_STALL;
    	float _confidence;
    	if(_event instanceof MarketModelEvent){
    		MarketModelEvent _marketModelEvent  = (MarketModelEvent)_event;
    		_direction = _marketModelEvent.getDirection();
    		_confidence = _marketModelEvent.getConfidence();
    	} else {
    		_direction = marketModel.getDirection();
    		_confidence = marketModel.getConfidence();
    	}
    	switch (_direction) {
    		case MarketModelEvent.MARKET_STALL: {
    			_marketDirection = 0f;
    			break;
    		}
    		case MarketModelEvent.MARKET_GOING_UP: {
    			_marketDirection = _confidence;
    			break;
    		}
    		case MarketModelEvent.MARKET_GOING_DOWN: {
    			_marketDirection = -_confidence;
    			break;
    		}
    	}
    	return _marketDirection;
    }

//-------------------------------------------------------------------------------------
    private float predict(float _marketDirection , float _currentProfit){
//		float _confidence = _event.getConfidence();
		debug.outln(Debug.INFO, "S4_ProfitPredictionChain:  _marketDirection="+_marketDirection+", _currentProfit=" + _currentProfit);
    	if(type == AbstractOrder.TYPE_BUY){
    		return predictBid(_marketDirection, _currentProfit);
    	} else {
    		return predictAsk(_marketDirection, _currentProfit);
    	}
    }

//-------------------------------------------------------------------------------------
	private float predictBid(float _marketDirection, float _currentProfit){
		float _predictedProfit;

		_predictedProfit = _currentProfit + (averageBidProfitStep * _marketDirection);

		averageBidProfitStep = (Math.abs(Math.abs(lastBidProfit) - Math.abs(_currentProfit)) + averageBidProfitStep) / 2;
		lastBidProfit = _currentProfit;

    	debug.outln(Debug.INFO, "S4_ProfitPredictionChain.Bid>>_marketDirection="+_marketDirection+",currentProfit:"+_currentProfit+",_predictedProfit="+_predictedProfit+", averageBidProfitStep="+averageBidProfitStep+", lastBidProfit="+lastBidProfit);
    	return _predictedProfit;
	}
	
//-------------------------------------------------------------------------------------
	private float predictAsk(float _marketDirection, float _currentProfit){
		float _predictedProfit;

		_predictedProfit = _currentProfit + (averageAskProfitStep * _marketDirection);

		averageAskProfitStep = (Math.abs(Math.abs(lastAskProfit) - Math.abs(_currentProfit)) + averageAskProfitStep) / 2;
		lastAskProfit = _currentProfit;

    	debug.outln(Debug.INFO, "S4_ProfitPredictionChain.Ask>>_marketDirection="+_marketDirection+", currentProfit:"+_currentProfit+",_predictedProfit="+_predictedProfit+", averageAskProfitStep="+averageAskProfitStep+", lastAskProfit="+lastAskProfit);
    	return _predictedProfit;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
