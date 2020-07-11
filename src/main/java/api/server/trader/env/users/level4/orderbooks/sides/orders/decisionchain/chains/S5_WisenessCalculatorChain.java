//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import org.knowm.xchange.dto.Order;

import codex.common.utils.*;
import codex.common.math.complex.sigmoids.*;

import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.*;

//-------------------------------------------------------------------------------------
public class S5_WisenessCalculatorChain {
	protected Debug debug;
	protected int type;
    protected float minimumProfit;
    protected float targetProfit;

    protected HalfSigmoidNormalized halfSigmoidNormalized;
    //---cache:

    //---statics:
    private static final float SIGMOID_ACCELERATION = 2.75f;

//-------------------------------------------------------------------------------------
    public S5_WisenessCalculatorChain(Debug _debug, int _type, TacticConfiguration _tacticConfiguration) {
    	debug = _debug;
    	type = _type;
    	minimumProfit = _tacticConfiguration.getMinimumProfit().floatValue();
    	targetProfit = _tacticConfiguration.getTargetProfit().floatValue();
    	halfSigmoidNormalized = new HalfSigmoidNormalized(SIGMOID_ACCELERATION);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public float wiseness(float _predictedProfit, float _currentProfit, float _trailingProcent){
		debug.outln(Debug.INFO, "S5_WisenessCalculatorChain:  _predictedProfit="+_predictedProfit+", _currentProfit=" + _currentProfit);
    	if(type == AbstractOrder.TYPE_BUY){
    		return wisenessBid(_predictedProfit, _currentProfit, _trailingProcent);
    	} else {
    		return wisenessAsk(_predictedProfit, _currentProfit, _trailingProcent);
    	}
    }

//-------------------------------------------------------------------------------------
	public float wisenessBid(float _predictedProfit, float _currentProfit, float _trailingProcent){
		float _wiseness = 0f;
		if(_currentProfit >= 0){
			_wiseness = 0f;
		} else {
			_wiseness = halfSigmoidNormalized.value(Math.abs(_currentProfit));
//			(float)sigmoid.value(Math.abs(_currentProfit) - 2.0);
		}
/*		if(_currentProfit < _predictedProfit){
			_wiseness = 1f;
		} else {
			float _diff = Math.abs(_currentProfit - _predictedProfit);
			_wiseness = MathUtils.normalize_Infinity0(_diff * 1000);
		}
		*/
    	debug.outln(Debug.INFO, "S5_WisenessCalculatorChain.Bid>>_predictedProfit="+_predictedProfit+", currentProfit:"+_currentProfit+", wiseness="+_wiseness);
    	return _wiseness;
	}
	
//-------------------------------------------------------------------------------------
	public float wisenessAsk(float _predictedProfit, float _currentProfit, float _trailingProcent){
		float _wiseness = 0f;
		if(_currentProfit < minimumProfit){
			_wiseness = 0f;
		} else if(_currentProfit < targetProfit){
			_wiseness = MathUtils.normalize_MinMax(_currentProfit, minimumProfit, targetProfit, 0.3f, 1f);
		} else {
			if((_currentProfit < _trailingProcent) || (_predictedProfit < _trailingProcent)){
				_wiseness = 1f;
			} else {
				_wiseness = MathUtils.normalize_MinMax(_currentProfit, targetProfit, _predictedProfit, 0.5f, 1f);
			}
		}

    	debug.outln(Debug.INFO, "S5_WisenessCalculatorChain.Ask>>_predictedProfit="+_predictedProfit+", currentProfit:"+_currentProfit+", wiseness="+_wiseness);
    	return _wiseness;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*	public float wisenessBid(float _predictedProfit, float _currentProfit, float _trailingProcent){
		float _wiseness = 0f;
		if(_currentProfit > -minimumProfit){
			_wiseness = 0f;
		} else if(_currentProfit > -targetProfit){
			_wiseness = MathUtils.normalize_MinMax(Math.abs(_currentProfit), minimumProfit, targetProfit, 0.3f, 1f);
		} else {
		if(_currentProfit > -targetProfit){
			_wiseness = MathUtils.normalize_01f(Math.abs(_currentProfit), targetProfit);
		} else {
			if((_currentProfit > -_trailingProcent) || (_predictedProfit > -_trailingProcent)){
				_wiseness = 1f;
			} else {
				_wiseness = MathUtils.normalize_MinMax(Math.abs(_currentProfit), targetProfit, Math.abs(_predictedProfit), 0.5f, 1f);
			}
//		}
    	debug.outln(Debug.INFO, "S5_WisenessCalculatorChain.Bid>>_predictedProfit="+_predictedProfit+", currentProfit:"+_currentProfit+", wiseness="+_wiseness);
    	return _wiseness;
	}*/