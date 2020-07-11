//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

//-------------------------------------------------------------------------------------
public class S8_VariationFilterChain {
	protected Debug debug;
	protected int type;
    protected Decision decision;
	protected float passThresholdIfVariationIsPositiveBid;
	protected float passThresholdIfVariationIsNegativeBid;
	protected float passThresholdIfVariationIsPositiveAsk;
	protected float passThresholdIfVariationIsNegativeAsk;

    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public S8_VariationFilterChain(Debug _debug, int _type, Decision _decision, float _passThresholdIfVariationIsPositiveBid, float _passThresholdIfVariationIsNegativeBid, float _passThresholdIfVariationIsPositiveAsk, float _passThresholdIfVariationIsNegativeAsk) {
    	debug = _debug;
    	type = _type;
    	decision = _decision;
    	passThresholdIfVariationIsNegativeBid = _passThresholdIfVariationIsNegativeBid;
    	passThresholdIfVariationIsPositiveBid = _passThresholdIfVariationIsPositiveBid;
    	passThresholdIfVariationIsNegativeAsk = _passThresholdIfVariationIsNegativeAsk;
    	passThresholdIfVariationIsPositiveAsk = _passThresholdIfVariationIsPositiveAsk;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------- 
//-------------------------------------------------------------------------------------
    public Decision filter(float _newPrice){
		debug.outln(Debug.ERROR, "VariationOutFilterChain.filter:  _newPrice=" + _newPrice);
    	if(type == AbstractOrder.TYPE_BUY){//Order.OrderType.ASK){
    		return filterBid(_newPrice);
    	} else {
    		return filterAsk(_newPrice);    		
    	}
    }

//-------------------------------------------------------------------------------------
    private Decision filterBid(float _newPrice) {
    	float _oldPrice = decision.getPrice().floatValue();
    	float _variationPrice = MathUtils.variation(_newPrice, _oldPrice);
    	float _variationPriceAbs = Math.abs(_variationPrice);

		if(_variationPrice > 0){
			if(_variationPriceAbs > passThresholdIfVariationIsPositiveBid){
				decision.setDecision(Decision.DECISION_CHANGE_ORDER);
				decision.setPrice(_newPrice);				
				return decision;
			}
		} else if(_variationPrice < 0){
			if(_variationPriceAbs > passThresholdIfVariationIsNegativeBid){
				decision.setDecision(Decision.DECISION_CHANGE_ORDER);
				decision.setPrice(_newPrice);				
				return decision;
			}
		} 
		decision.setDecision(Decision.DECISION_DONT_TOUCH_ORDER);
		return decision;		
	}


//-------------------------------------------------------------------------------------
    private Decision filterAsk(float _newPrice) {
    	float _oldPrice = decision.getPrice().floatValue();
    	float _variationPrice = MathUtils.variation(_newPrice, _oldPrice);
    	float _variationPriceAbs = Math.abs(_variationPrice);

		if(_variationPrice > 0){
			if(_variationPriceAbs > passThresholdIfVariationIsPositiveAsk){
				decision.setDecision(Decision.DECISION_CHANGE_ORDER);
				decision.setPrice(_newPrice);				
				return decision;
			}
		} else if(_variationPrice < 0){
			if(_variationPriceAbs > passThresholdIfVariationIsNegativeAsk){
				decision.setDecision(Decision.DECISION_CHANGE_ORDER);
				decision.setPrice(_newPrice);				
				return decision;
			}
		} 
		decision.setDecision(Decision.DECISION_DONT_TOUCH_ORDER);
		return decision;		
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
