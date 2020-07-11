//-------------------------------------------------------------------------------------
package codex.xbit.api.common.datas;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import com.fasterxml.jackson.annotation.*;

import codex.common.utils.*;


//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class WhaleAlertSummaryData extends AbstractData {
	protected List<WhaleAlertSummaryEntryData> entries;
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public WhaleAlertSummaryData(){
    	entries = new ArrayList<WhaleAlertSummaryEntryData>();
    }

//-------------------------------------------------------------------------------------
    public int size(){
    	return entries.size();
    }

//-------------------------------------------------------------------------------------
    public void add(WhaleAlertSummaryEntryData _whaleAlertSummaryEntryData){
    	entries.add(_whaleAlertSummaryEntryData);
    }

//-------------------------------------------------------------------------------------
    public WhaleAlertSummaryEntryData get(int _index){
    	return entries.get(_index);
    }

//-------------------------------------------------------------------------------------
	public String toString(){
		String _text = "";
		for (int i = 0; i < entries.size(); i++) {
			WhaleAlertSummaryEntryData _entry = entries.get(i);
			_text += _entry.toString();
		}
		return _text;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
