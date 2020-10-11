//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.crawler.objects;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;

import codex.xbit.api.common.datas.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
//@SuppressWarnings("serial")
public class Result {
    private String result;
    private String cursor;
    private int count;
    private List<WhaleAlertTransactionData> transactions;

//-------------------------------------------------------------------------------------
    public Result() {
    }

//-------------------------------------------------------------------------------------
    public String getResult() {
        return result;
    }

//-------------------------------------------------------------------------------------
    public String getCursor() {
        return cursor;
    }

//-------------------------------------------------------------------------------------
    public int getCount() {
        return count;
    }

//-------------------------------------------------------------------------------------
    public List<WhaleAlertTransactionData> getTransactions() {
        return transactions;
    }

//-------------------------------------------------------------------------------------
    public void pack() {
    	if(transactions == null){
//        	System.out.println("Packing result... 0 transactions..."+cursor);
    		return;
    	}
        for (int i = 0; i < transactions.size(); i++) {
        	WhaleAlertTransactionData _transaction = transactions.get(i);
        	_transaction.pack();
        }
//        System.out.println("Packing result... " + transactions.size() + " transactions...:"+cursor);
    }

//-------------------------------------------------------------------------------------
    public String toString() {
        return null;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
