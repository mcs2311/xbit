//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.loaders.orders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.time.*;

import com.opencsv.*;
import com.opencsv.exceptions.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.*;
import org.ta4j.core.num.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.sidesorderscontainers.*;

//-------------------------------------------------------------------------------------
public class OrdersLoader {
    protected Debug debug;
    protected Resolver resolver;
    protected ZoneId zoneId;
    protected SidesOrdersContainer sidesOrdersContainer;


    protected String directory;

    //---cache:
    private org.knowm.xchange.currency.CurrencyPair currencyPair;
    private String exchange;

//-------------------------------------------------------------------------------------
    public OrdersLoader(Debug _debug, Resolver _resolver, SidesOrdersContainer _typesOrdersContainer) {
        debug = _debug;
        resolver = _resolver;
        zoneId = _resolver.getZoneId();
        sidesOrdersContainer = _typesOrdersContainer; 

        currencyPair = sidesOrdersContainer.getCurrencyPair();
        exchange = sidesOrdersContainer.getExchange();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private String getCurrencyPairDirectory() {
    	return currencyPair.toString().toLowerCase().replace("/","_");
//    	return _currencies[1] + "/" + _currencies[0];
    }

//-------------------------------------------------------------------------------------
    private String getDirectory() {
    	if(directory == null){
    		directory = resolver.getUserHome() + 
    					"/orderbooks/" + 
    					exchange +
//    					"/" +
//    					orders.getTacticConfiguration().getName() +
//    					"/" +
//    					getCurrencyPairDirectory() +
    					"/";
        	FileUtils.ifDoesntExistCreate(directory);
    	}
        return directory;
    }

//-------------------------------------------------------------------------------------
    public String getFilename() {
    	String _path = getDirectory() + 
//    					OrderX.convertTypeFromInt(orders.getType()).toLowerCase() + 
    					currencyPair.toString().toLowerCase().replace("/","_") + 
    					".orders";
        return _path;
    }

//-------------------------------------------------------------------------------------
    public void load() {
        String _filename = getFilename();
		debug.outln(Debug.INFO, "Load orders for "+currencyPair+"/"+exchange+"...");
//		debug.outln("Load orders from "+_filename+" ...");
//		debug.out(".", false);
        CSVReader _csvReader = null;
        List<String[]> _rows;
        try {
            _csvReader = new CSVReader(new InputStreamReader(new FileInputStream(_filename)));
            _rows = _csvReader.readAll();
//            final int _counter = 0;
            if((_rows != null) && !_rows.isEmpty()) {
            	for(int i = 0; i < _rows.size(); i++) {
            		String[] _row = _rows.get(i);
            		loadRow(i, _row);
            	}
//                _counter++;
            }


        } catch (IOException | CsvException _e1) {
//            Logger.getLogger(CsvTradesLoader.class.getName()).log(Level.SEVERE, "Unable to load trades from CSV", ioe);
            debug.outln(Debug.WARNING, "Warning: Unable to load orders from CSV: "+_filename);
        } finally {
            if (_csvReader != null) {
                try {
                    _csvReader.close();
                } catch (IOException _e2) {
                    _e2.printStackTrace();
                }
            }
        }
//

//        debug.outln(Debug.WARNING, "series="+series+" , load.lastBarLastEntryTime="+lastBarLastEntryTime);
		debug.outln(sidesOrdersContainer.size() + " orders loaded ...");
    }

//-------------------------------------------------------------------------------------
    public void save() {
		String _filename = getFilename();
//		debug.outln("Save " + orders.size() + " orders to "+_filename+"...");
//		debug.outln("Save repository "+_filename+" ...");
//		debug.out(".", false);
        try {
            //new file
            FileWriter _fileWriter = new FileWriter(new File(_filename));
            CSVWriter _csvWriter = new CSVWriter(_fileWriter, 
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, 
                    CSVWriter.NO_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
//            List<String[]> _list = convertToStringArray();
//            _csvWriter.writeAll(_list);
//            List<String[]> _list = new ArrayList<String[]>();
            String[] _row;
//            _lastRow = null;
            List<OrderX> _orders = sidesOrdersContainer.getAllOrders();

			Collections.sort(_orders, new Comparator<OrderX>(){
			   public int compare(OrderX _order1, OrderX _order2){
			      return (int)(_order2.getId() - _order1.getId());
			   }
			});

            for (int i = 0; i < _orders.size() ; i++) {
    			OrderX _order = _orders.get(i);
            	_row = saveRow(i, _order);
            	if(_row != null){
                	_csvWriter.writeNext(_row, false);
                }
            }
            _csvWriter.close();
            _fileWriter.close();
//            return _filename;
        } catch (IOException _e) {
            _e.printStackTrace();
        }   
//        return null;
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected void loadRow(int _index, String[] _row) {
//    	BaseBar _bar = null;
//    	String _exchange = sidesOrdersContainer.getExchange();
//    	org.knowm.xchange.currency.CurrencyPair _currencyPair = sidesOrdersContainer.getCurrencyPair();
        if((_row != null) && (_row.length >= 10)){
        	try {
	            String _id = _row[0].trim();
	            String _type = _row[1].trim();
	            String _position = _row[2].trim();
	            String _time = _row[3].trim();
//	            String _exchange = _row[2].trim();
	            String _price = _row[4].trim();
	            String _amount = _row[5].trim();
	            String _state = _row[6].trim();
	            String _backwardPeers = _row[7].trim();
	            String _forwardPeers = _row[8].trim();
	            String _tradeIds = _row[9].trim();
	            String _ordersIds = _row[10].trim();
	            OrderX _order = new OrderX(debug, resolver, _id, _time, exchange, currencyPair, _price, _amount, _type, _position, _state, _backwardPeers, _forwardPeers, _tradeIds, _ordersIds);
//	            debug.outln("Load row= " + _order);
	            try{
	            	sidesOrdersContainer.addOrder(_order);
	            }catch(IllegalArgumentException _e){
	//            	debug.outln(Debug.WARNING, "["+this+"]: Cannot add bar "+_index+" / "+_endTimeLong+" : " + _e.getMessage());
	            	debug.outln(Debug.WARNING, "["+this+"]["+_order+"] Cannot add order: "+_index);
//	            	System.exit(0);
	            }
        	}catch(Exception _e2){
	            debug.outln(Debug.WARNING, "["+this+"]Exception: Cannot add order in "+sidesOrdersContainer + " at line: "+_index+", e="+_e2+"Row:["+_row+"]");
	            _e2.printStackTrace();
	            System.exit(0);
        	}

        }
    }


//-------------------------------------------------------------------------------------
    protected String[] saveRow(int _index, OrderX _order) {
//    	OrderX _order = orders.getOrderToSave(_index);
        String[] _list = new String[11];
        _order.resetScales();
//		_order.getPrice().setScale(scales.getPrice(), RoundingMode.HALF_UP);
//    	_order.getAmount().setScale(scales.getAmount(), RoundingMode.HALF_UP);

        try {
	        _list[0] = _order.getIdAsString();
	        _list[1] = " " + _order.getTypeAsAString();
	        _list[2] = " " + _order.getPositionAsString();
	        _list[3] = " " + Long.toString(_order.getTime());
//	        _list[3] = " " + _order.getPrice().stripTrailingZeros().toString();
//	        _list[4] = " " + _order.getAmount().stripTrailingZeros().toString();
	        _list[4] = " " + _order.getPrice().toString();
	        _list[5] = " " + _order.getAmount().toString();
	        _list[6] = " " + _order.getStateAsString();
	        _list[7] = " " + _order.getBackwardPeersAsString();
	        _list[8] = " " + _order.getForwardPeersAsString();
	        _list[9] = " " + _order.getTradeIdsAsString();
	        _list[10] = " " + _order.getOrderIdsAsString();
	    } catch (NullPointerException _e) {
	        debug.outln(Debug.WARNING, "["+this+"][NullPointerException:] Cannot save order: "+_order + " at line: "+_index);
	        _e.printStackTrace();
//	        System.exit(0);
	    	return null;
        }
        return _list;
    }
    

//-------------------------------------------------------------------------------------
    public String toString() {
    	return getFilename();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
