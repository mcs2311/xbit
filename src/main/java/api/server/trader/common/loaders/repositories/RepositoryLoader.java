//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.loaders.repositories;
//-------------------------------------------------------------------------------------
import java.io.*;
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

// /import codex.xbit.api.server.trader.common.*;

import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public abstract class RepositoryLoader {
    protected Debug debug;
    protected Resolver resolver;
	protected RepositoryConfiguration repositoryConfiguration;
    protected ZoneId zoneId;


    protected String directory;
    protected Duration barDuration;

    protected ZonedDateTime lastEntryTime;

//-------------------------------------------------------------------------------------
    public RepositoryLoader(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ZoneId _zoneId) {
        debug = _debug;
        resolver = _resolver;
        repositoryConfiguration = _repositoryConfiguration;
        zoneId = _zoneId;
        int _barDurationInt = repositoryConfiguration.getBarDurationAsInt();
        barDuration = Duration.ofSeconds​(_barDurationInt);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public ZonedDateTime getLastEntryTime(){
    	return lastEntryTime;
    }

//-------------------------------------------------------------------------------------
    public void setLastEntryTime(ZonedDateTime _zonedDateTime){
    	lastEntryTime = _zonedDateTime;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private String getCurrencyPairDirectory() {
    	String[] _currencies = repositoryConfiguration.getCurrencyPair().toString().toLowerCase().split("/");
    	return _currencies[1] + "/" + _currencies[0];
    }

//-------------------------------------------------------------------------------------
    private String getDirectory() {
    	if(directory == null){
    		directory = SystemUtils.getHomeDirectory() + "/" + 
    					resolver.getServerConfiguration().getPathToSingle() + 
    					"/repositories/repos/" + 
    					repositoryConfiguration.getType() +
    					"/" +
    					getCurrencyPairDirectory() +
    					"/" +
    					repositoryConfiguration.getExchange() +
    					"/";
        FileUtils.ifDoesntExistCreate(directory);
    	}
        return directory;
    }

//-------------------------------------------------------------------------------------
    public String getFilename() {
    	String _path = getDirectory() + 
    					repositoryConfiguration.getBarDuration() + 
    					".data";
//        FileUtils.ifDoesntExistCreate(_path);
        return _path;
    }

//-------------------------------------------------------------------------------------
    public void load() {
        String _filename = getFilename();
//		debug.outln("Load repository "+_filename+" ...");
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
            		if(_row.length == 1){
            			long _endTimeLong = Long.parseLong(_row[0]);
            			lastEntryTime = TimeUtils.convertLongToZonedDateTime(_endTimeLong);
            			break;
            		} else {
            			long _time = Long.parseLong(_row[0]);
            			ZonedDateTime _endTimeBar = TimeUtils.convertLongToZonedDateTime(_time);
            			ZonedDateTime _beginTimeBar = _endTimeBar.minus(barDuration);
            			if(_beginTimeBar.compareTo(ZonedDateTime.now(zoneId)) > 0){
            				debug.outln(Debug.WARNING, "Warning: Skipping Bar which start in the future: " + _beginTimeBar);
            			} else {
            				loadRow(i, _row);
            			}
            		}
            	}
//                _counter++;
            }


        } catch (IOException | CsvException _e1) {
//            Logger.getLogger(CsvTradesLoader.class.getName()).log(Level.SEVERE, "Unable to load trades from CSV", ioe);
            debug.outln(Debug.WARNING, "Warning: Unable to load trades from CSV: "+_filename);
        } finally {
            if (_csvReader != null) {
                try {
                    _csvReader.close();
                } catch (IOException _e2) {
                    _e2.printStackTrace();
                }
            }
        }
        ZonedDateTime _now = ZonedDateTime.now(zoneId);
    	if((lastEntryTime == null) || (lastEntryTime.compareTo(_now) > 0)) {
        	debug.outln(Debug.WARNING, "Repository " + repositoryConfiguration+": reseting lastEntryTime("+lastEntryTime+") to:" + _now + "...");
    		lastEntryTime = _now;
    	}
//        debug.outln(Debug.WARNING, "series="+series+" , load.lastBarLastEntryTime="+lastBarLastEntryTime);
    }

//-------------------------------------------------------------------------------------
    public void save() {
		String _filename = getFilename();
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
            String[] _row, _lastRow;
            _lastRow = null;
            for (int i = 0; i < getNumberOfRows() ; i++) {
            	_row = saveRow(i);
            	if(_row != null){
            		if(i == 0){
                		_csvWriter.writeNext(_row, false);
            		} else {
            			if((_lastRow == null) || (_row[0].equals(_lastRow[0]) == false)) {
                			_csvWriter.writeNext(_row, false);            				
            			}
            		}
                	_lastRow = _row;
                }
            }
            _row = new String[1];
            _row[0] = Long.toString(TimeUtils.convertZonedDateTimeToLong(lastEntryTime));
            _csvWriter.writeNext(_row, false);
            _csvWriter.close();
            _fileWriter.close();
//            return _filename;
        } catch (IOException _e) {
            _e.printStackTrace();
        }   
//        return null;
    }

//-------------------------------------------------------------------------------------
    public String toString() {
    	return getFilename();
    }

//-------------------------------------------------------------------------------------
    protected abstract  int getNumberOfRows();
//-------------------------------------------------------------------------------------
    protected abstract void loadRow(int _index, String[] _row);
//-------------------------------------------------------------------------------------
    protected abstract String[] saveRow(int _index);
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
