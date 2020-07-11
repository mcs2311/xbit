//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.loaders.repositories;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.sql.*;
import java.util.*;
import java.time.*;

import com.opencsv.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.*;
import org.ta4j.core.num.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

//import codex.xbit.api.server.trader.common.*;

import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public class TickerRepositoryLoader extends RepositoryLoader {
	private BarSeries bidSeries, askSeries;
    private Bar bidLastBar, askLastBar;

//-------------------------------------------------------------------------------------
    public TickerRepositoryLoader(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ZoneId _zoneId, BarSeries _bidSeries, BarSeries _askSeries) {
        super(_debug, _resolver, _repositoryConfiguration, _zoneId);
        bidSeries = _bidSeries;
        askSeries = _askSeries;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Bar getBidLastBar(){
    	return bidLastBar;
    }

//-------------------------------------------------------------------------------------
    public Bar getAskLastBar(){
    	return askLastBar;
    }

//-------------------------------------------------------------------------------------
    public ZonedDateTime getLastBarEndTime(){
    	if(bidLastBar != null){
    		return bidLastBar.getEndTime();
    	}
    	return ZonedDateTime.now(zoneId);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected int getNumberOfRows(){
    	return bidSeries.getBarCount();
    }

//-------------------------------------------------------------------------------------
    protected void loadRow(int _index, String[] _row) {
        if(_row != null) {
        	try {
            	long _endTimeLong = Long.parseLong(_row[0]);
	            ZonedDateTime _endTime = TimeUtils.convertLongToZonedDateTime(_endTimeLong);
	            Num _openPriceBid = PrecisionNum.valueOf(_row[1]);
	            Num _highPriceBid = PrecisionNum.valueOf(_row[2]);
	            Num _lowPriceBid = PrecisionNum.valueOf(_row[3]);
	            Num _closePriceBid = PrecisionNum.valueOf(_row[4]);
	            Num _volumeBid = PrecisionNum.valueOf(_row[5]);
	            Num _amountBid = PrecisionNum.valueOf(_row[6]);
	            bidLastBar = new BaseBar(barDuration, _endTime, _openPriceBid, _highPriceBid, _lowPriceBid, _closePriceBid, _volumeBid, _amountBid);
	//            debug.outln("_endTime= " + _endTime);
	            try{
	            	bidSeries.addBar(bidLastBar);
	            }catch(IllegalArgumentException _e){
	            	debug.outln(Debug.WARNING, "["+this+"]["+bidSeries.getSeriesPeriodDescription()+"] Cannot add bar "+_index+" / "+_endTimeLong+" ");
	            }

	            Num _openPriceAsk = PrecisionNum.valueOf(_row[7]);
	            Num _highPriceAsk = PrecisionNum.valueOf(_row[8]);
	            Num _lowPriceAsk = PrecisionNum.valueOf(_row[9]);
	            Num _closePriceAsk = PrecisionNum.valueOf(_row[10]);
	            Num _volumeAsk = PrecisionNum.valueOf(_row[11]);
	            Num _amountAsk = PrecisionNum.valueOf(_row[12]);
	            askLastBar = new BaseBar(barDuration, _endTime, _openPriceAsk, _highPriceAsk, _lowPriceAsk, _closePriceAsk, _volumeAsk, _amountAsk);
	//            debug.outln("_endTime= " + _endTime);
	            try{
	            	askSeries.addBar(askLastBar);
	            }catch(IllegalArgumentException _e){
	            	debug.outln(Debug.WARNING, "["+this+"]["+askSeries.getSeriesPeriodDescription()+"] Cannot add bar "+_index+" / "+_endTimeLong+" ");
	            }
        	}catch(Exception _e2){
	            debug.outln(Debug.WARNING, "["+this+"][Exception:] Cannot add bar in "+askSeries.getSeriesPeriodDescription() + " at line: "+_index);
        	}
        }
    }


//-------------------------------------------------------------------------------------
    protected String[] saveRow(int _index) {
        Bar _bidBar = bidSeries.getBar(_index);
        Bar _askBar = bidSeries.getBar(_index);
        if((_bidBar == null) || (_askBar == null)){
        	return null;
        }
        String[] _list = new String[13];

        ZonedDateTime _endTime = _bidBar.getEndTime();
//        long _timestamp = Timestamp.valueOf(_endTime.toLocalDateTime()).getTime();
        long _timestamp = TimeUtils.convertZonedDateTimeToLong(_endTime);
        _list[0] = Long.toString(_timestamp);


        try {
	        _list[1] = _bidBar.getOpenPrice().toString();
	        _list[2] = _bidBar.getHighPrice().toString();
	        _list[3] = _bidBar.getLowPrice().toString();
	        _list[4] = _bidBar.getClosePrice().toString();
	        _list[5] = _bidBar.getVolume().toString();
	        _list[6] = _bidBar.getAmount().toString();

	        _list[7] = _askBar.getOpenPrice().toString();
	        _list[8] = _askBar.getHighPrice().toString();
	        _list[9] = _askBar.getLowPrice().toString();
	        _list[10] = _askBar.getClosePrice().toString();
	        _list[11] = _askBar.getVolume().toString();
	        _list[12] = _askBar.getAmount().toString();

	    } catch (NullPointerException _e) {
//            System.out.print("Caught the NullPointerException");
	    	return null;
        }
        return _list;
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
