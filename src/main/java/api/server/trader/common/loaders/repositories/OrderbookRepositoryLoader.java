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
public class OrderbookRepositoryLoader extends RepositoryLoader {
	private BarSeries series;

    private Bar lastBar;

//-------------------------------------------------------------------------------------
    public OrderbookRepositoryLoader(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ZoneId _zoneId, BarSeries _series) {
        super(_debug, _resolver, _repositoryConfiguration, _zoneId);
        series = _series;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Bar getLastBar(){
    	return lastBar;
    }

//-------------------------------------------------------------------------------------
    public ZonedDateTime getLastBarEndTime(){
    	if(lastBar != null){
    		return lastBar.getEndTime();
    	}
    	return ZonedDateTime.now(zoneId);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected int getNumberOfRows(){
    	return series.getBarCount();
    }

//-------------------------------------------------------------------------------------
    protected void loadRow(int _index, String[] _row) {
        if(_row != null) {
            long _endTimeLong = Long.parseLong(_row[0]);
            ZonedDateTime _endTime = TimeUtils.convertLongToZonedDateTime(_endTimeLong);
            Num _openPrice = PrecisionNum.valueOf(_row[1]);
            Num _highPrice = PrecisionNum.valueOf(_row[2]);
            Num _lowPrice = PrecisionNum.valueOf(_row[3]);
            Num _closePrice = PrecisionNum.valueOf(_row[4]);
            Num _volume = PrecisionNum.valueOf(_row[5]);
            Num _amount = PrecisionNum.valueOf(_row[6]);
            lastBar = new BaseBar(barDuration, _endTime, _openPrice, _highPrice, _lowPrice, _closePrice, _volume, _amount);
//            debug.outln("_endTime= " + _endTime);
            try{
            	series.addBar(lastBar);
            }catch(IllegalArgumentException _e){
            	debug.outln(Debug.WARNING, "["+this+"]:Cannot add bar "+_index+" : " + _e.getMessage());
            }
        }
    }


//-------------------------------------------------------------------------------------
    protected String[] saveRow(int _index) {
        Bar _bar = series.getBar(_index);
        if(_bar == null){
        	return null;
        }
        String[] _list = new String[7];

        ZonedDateTime _endTime = _bar.getEndTime();
//        long _timestamp = Timestamp.valueOf(_endTime.toLocalDateTime()).getTime();
        long _timestamp = TimeUtils.convertZonedDateTimeToLong(_endTime);
        _list[0] = Long.toString(_timestamp);


        try {
	        _list[1] = _bar.getOpenPrice().toString();
	        _list[2] = _bar.getHighPrice().toString();
	        _list[3] = _bar.getLowPrice().toString();
	        _list[4] = _bar.getClosePrice().toString();
	        _list[5] = _bar.getVolume().toString();
	        _list[6] = _bar.getAmount().toString();
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
