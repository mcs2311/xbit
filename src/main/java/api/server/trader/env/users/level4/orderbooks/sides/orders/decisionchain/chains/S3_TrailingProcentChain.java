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

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.*;

//-------------------------------------------------------------------------------------
public class S3_TrailingProcentChain {
	protected Debug debug;
	protected int type;

    //---cache:
    protected float targetProfit;
    protected float minimumProfit;
    protected boolean trailingEnabled;
    protected float trailingDeviation;


//    protected float useCounterCurrencyPercent;

    protected int trailingState;
	protected float trailingStopLossProcent;

    //---cache:

    //---statics:
    public static final int TRAILING_STATE_NOT_ARMED 		= 0;
    public static final int TRAILING_STATE_ARMED 			= 1;

//-------------------------------------------------------------------------------------
    public S3_TrailingProcentChain(Debug _debug, int _type, TacticConfiguration _tacticConfiguration) {
    	debug = _debug;
    	type = _type;

    	targetProfit = _tacticConfiguration.getTargetProfit().floatValue();
    	minimumProfit = _tacticConfiguration.getMinimumProfit().floatValue();
    	trailingEnabled = _tacticConfiguration.trailingEnabled();
    	trailingDeviation = _tacticConfiguration.getTrailingDeviation().floatValue();
//		useCounterCurrencyPercent = _tacticConfiguration.getUseCounterCurrencyPercent().floatValue();

    	trailingState = TRAILING_STATE_NOT_ARMED;
    	if(type == AbstractOrder.TYPE_BUY){
			trailingStopLossProcent = 0f;
		} else {
			trailingStopLossProcent = targetProfit;			
		}
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public float trail(float _currentProfit){
		debug.outln(Debug.INFO, "S3_TriggerProcentChain: _currentProfit="+_currentProfit+", trailingState=" + trailingState);
    	if(!trailingEnabled){
    		return targetProfit;
    	} else if(type == AbstractOrder.TYPE_BUY){
    		return trailBid(_currentProfit);
    	} else {
    		return trailAsk(_currentProfit);
    	}
    }

//-------------------------------------------------------------------------------------
	private float trailBid(float _currentProfit){
    	switch(trailingState){
    		case TRAILING_STATE_NOT_ARMED: {
    			if(_currentProfit <= 0){
		    		trailingState = TRAILING_STATE_ARMED;
		    		trailingStopLossProcent = BigDecimal.valueOf(_currentProfit).setScale(DecisionChain.SCALE_TRAILING_STOP_LOSS_PROCENT, RoundingMode.UP).floatValue();
		    	}
    			break;
    		}
    		case TRAILING_STATE_ARMED: {
    			if(_currentProfit < trailingStopLossProcent){
    				trailingStopLossProcent = BigDecimal.valueOf(_currentProfit).setScale(DecisionChain.SCALE_TRAILING_STOP_LOSS_PROCENT, RoundingMode.UP).floatValue();
    			}
    			break;
    		}
    	}
    	debug.outln(Debug.INFO, "S3_TriggerProcentChain.Bid>>currentProfit:"+_currentProfit+",targetProfit="+targetProfit + ", trailingState="+trailingState+",trailingStopLossProcent="+trailingStopLossProcent);
    	return trailingStopLossProcent;
	}
	
//-------------------------------------------------------------------------------------
	private float trailAsk(float _currentProfit){
    	switch(trailingState){
    		case TRAILING_STATE_NOT_ARMED: {
		    	if(_currentProfit >= targetProfit){
		    		trailingState = TRAILING_STATE_ARMED;
		    		trailingStopLossProcent = BigDecimal.valueOf(_currentProfit).setScale(DecisionChain.SCALE_TRAILING_STOP_LOSS_PROCENT, RoundingMode.DOWN).floatValue();
		    	}	    		
    			break;
    		}
    		case TRAILING_STATE_ARMED: {
    			if(_currentProfit >= trailingStopLossProcent){
    				trailingStopLossProcent = BigDecimal.valueOf(_currentProfit).setScale(DecisionChain.SCALE_TRAILING_STOP_LOSS_PROCENT, RoundingMode.DOWN).floatValue();
    			}
    			break;
    		}
    	}
    	debug.outln(Debug.INFO, "S3_TriggerProcentChain.Ask>>currentProfit:"+_currentProfit+",targetProfit="+targetProfit + ", trailingState="+trailingState+",trailingStopLossProcent="+trailingStopLossProcent);
    	return trailingStopLossProcent;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
