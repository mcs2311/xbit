//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.components.tables.models;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.*;
import javax.swing.*;
import javax.swing.table.*;

import codex.common.utils.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.client.net.*;


//-------------------------------------------------------------------------------------
public class WhaleAlertSummaryTableModel extends CommonTableModel {
    private NetCommandRequester netCommandRequester;

//-------------------------------------------------------------------------------------
    public WhaleAlertSummaryTableModel(Debug _debug, NetCommandRequester _netCommandRequester){
        super(_debug);
        netCommandRequester = _netCommandRequester;

        COLUMN_NAMES = new ArrayList<String>(Arrays.asList("Currency", "in", "out", "unk", "ex"));
        COLUMN_TYPES = new ArrayList<Class<?>>(Arrays.asList(String.class, Long.class, Long.class, Long.class, Long.class));
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private JButton getButton(int _cellIndex) {
        JButton _b0 = new JButton("Set");
        _b0.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e)
              {
                    debug.outln("WhaleAlertTableModel.set..."+_cellIndex);
//                    netCommandRequester.removeWave(_cellIndex);
              }
            });
        return _b0;
    }

//-------------------------------------------------------------------------------------
    public void reload() {
        rows.clear();
		//ArrayList<Object> _row = createRow(i, wavesDumperList.get(i));
        super.reload();
//        debug.outln("WavesTableModel.reload2..."+rows.size());
    }
/*
//-------------------------------------------------------------------------------------
    public ArrayList<Object> createRow(int _index, int _cellIndex) {
        ArrayList<Object> _row = new ArrayList<Object>();

        return _row;
    }
*/
//-------------------------------------------------------------------------------------

}
//-------------------------------------------------------------------------------------
