//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.envs.panels.single.whalealert;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.ocpsoft.prettytime.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.functions.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.datas.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;
import codex.xbit.api.common.packets.*;

import codex.xbit.api.client.net.*;
import codex.xbit.api.client.cli.*;

import codex.xbit.api.client.gui.components.panels.*;
import codex.xbit.api.client.gui.components.tables.models.*;
import codex.xbit.api.client.gui.status.*;
import codex.xbit.api.client.gui.tab.*;
import codex.xbit.api.client.gui.tab.tabs.users.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class WhaleAlertSummaryPanel extends AbstractPanel implements Consumer<NetPacket> {
	private DefaultXbitTreeNode parentNode;
	private ClientConfiguration clientConfiguration;
	private ServerConfiguration serverConfiguration;

    private NetClient netClient;
    private NetCommandRequester netCommandRequester;

    private JTextField timeAgo;
    private WhaleAlertSummaryTableModel tableModel;
    private JTable table;
    private PrettyTime prettyTime;
    private Date timeAgoDate;

//-------------------------------------------------------------------------------------
    public WhaleAlertSummaryPanel(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration, ServerConfiguration _serverConfiguration) {
    	super(_debug, _status, _tab);
    	parentNode = _parentNode;
    	clientConfiguration = _clientConfiguration;
    	serverConfiguration = _serverConfiguration;
    	prettyTime = new PrettyTime();

        setLayout(new BorderLayout());
        add(getInfoPanel(), BorderLayout.PAGE_START);

    	netClient = new NetClient(_debug, _serverConfiguration);
    	netCommandRequester = netClient.getNetCommandRequester();

        tableModel = new WhaleAlertSummaryTableModel(_debug, netCommandRequester);
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(false);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<>();
         
        int columnIndexToSort = 4;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.DESCENDING));
         
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        table.getColumnModel().getColumn(0).setWidth(80);
        table.getColumnModel().getColumn(3).setWidth(100);


        table.getColumn("Currency").setMaxWidth(80);
        table.getColumn("in").setMaxWidth(200);
        table.getColumn("out").setMaxWidth(80);
        table.getColumn("unk").setMaxWidth(150);
        table.getColumn("ex").setMaxWidth(150);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment( JLabel.LEFT );

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment( JLabel.RIGHT );

        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );


        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setMaximumSize( new Dimension(Integer.MAX_VALUE, 400));
        add(scrollPane, BorderLayout.PAGE_END);
        setMaximumSize( new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));        
        start();
    }

//-------------------------------------------------------------------------------------
    public void run() {
    	NetCommand _netCommand = new NetCommand(NetCommand.COMMAND_SHOW, NetCommand.COMMAND_SHOW_WHALES, NetCommand.COMMAND_SHOW_WHALES_SUMMARY, "all");
        netCommandRequester.send(_netCommand, this);
    	while(clientConfiguration.isRunning()){
    		String _timeAgo = "-";
    		if(timeAgoDate != null){
        		_timeAgo = prettyTime.format(timeAgoDate);
        	}
        	timeAgo.setText(_timeAgo);
    		try{
    			Thread.sleep(500);
    		}catch(InterruptedException _e){}
    	}
    }

//-------------------------------------------------------------------------------------
    public void accept(NetPacket _netPacket) {
    	if(_netPacket == null){
    		debug.outln("NetPacket is null in accept...");
    		return;
    	}
    	NetCommand _netCommand = (NetCommand)_netPacket;
    	WhaleAlertSummaryData _whaleAlertSummaryData = (WhaleAlertSummaryData)_netCommand.getMessage();
    	setWhaleAlertSummary(_whaleAlertSummaryData);
    }

//-------------------------------------------------------------------------------------
    private JPanel getInfoPanel() {
        JPanel _p = new JPanel();
        _p.setLayout(new BorderLayout());
        timeAgo = new JTextField("-");
        _p.add(getJTextFieldPanel("Last update: ", timeAgo), BorderLayout.PAGE_START);
//        _p.add(getJTextFieldPanel("HashOut: ", hashOut), BorderLayout.CENTER);
        Border _border = BorderFactory.createLineBorder(Color.LIGHT_GRAY , 2);
        _p.setBorder(_border);
        _p.setMaximumSize( new Dimension(Integer.MAX_VALUE, 200));        
        return _p;
    }

//-------------------------------------------------------------------------------------
    private JPanel getJTextFieldPanel(String _name, JTextField _tf) {
        JPanel _p = new JPanel();
        _p.setLayout(new FlowLayout(FlowLayout.LEADING));
        _p.add(new JLabel(_name, SwingConstants.RIGHT));
        _tf.setHorizontalAlignment(JTextField.RIGHT);
        _tf.setEditable(false);
        _tf.setEnabled(false);
        _tf.setOpaque(false);
        _tf.setBackground(new Color(0, 0, 0, 0));
        _tf.setBorder(null);
        _p.add(_tf);
        return _p;
    }

//-------------------------------------------------------------------------------------
    public void setWhaleAlertSummary(WhaleAlertSummaryData _whaleAlertSummaryData) {
        tableModel.clear();
        for (int i = 0; i < _whaleAlertSummaryData.size(); i++) {
            WhaleAlertSummaryEntryData _entry = _whaleAlertSummaryData.get(i);
//            List<Object> _cryptoInfo = _netHash.getAsArrayList();
            tableModel.addRow(_entry.getAsArrayList());
        }
        timeAgoDate = new Date();
        tableModel.reload();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
