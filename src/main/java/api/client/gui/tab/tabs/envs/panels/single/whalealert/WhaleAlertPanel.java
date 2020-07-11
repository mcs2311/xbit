//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.tabs.envs.panels.single.whalealert;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import codex.common.utils.*;

import codex.xbit.api.common.configs.*;
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
import codex.xbit.api.client.gui.tree.nodes.*;


//-------------------------------------------------------------------------------------
public class WhaleAlertPanel extends AbstractPanel {

    private JTabbedPane tabbedPane;

//-------------------------------------------------------------------------------------
    public WhaleAlertPanel(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode, ClientConfiguration _clientConfiguration, ServerConfiguration _serverConfiguration) {
    	super(_debug, _status, _tab);
//    	parentNode = _parentNode;
//    	serverConfiguration = _serverConfiguration;

        setLayout(new BorderLayout(0, 0));
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);

        WhaleAlertSummaryPanel _whaleAlertSummaryPanel = new WhaleAlertSummaryPanel(_debug, _status, _tab, _parentNode, _clientConfiguration, _serverConfiguration);
        tabbedPane.add("Summary", _whaleAlertSummaryPanel);

        WhaleAlertTransactionListPanel _whaleAlertTransactionListPanel = new WhaleAlertTransactionListPanel(_debug, _status, _tab, _parentNode, _clientConfiguration, _serverConfiguration);
        tabbedPane.add("WhaleAlertTransactionData List", _whaleAlertTransactionListPanel);

        add(tabbedPane, BorderLayout.CENTER);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));        
    }

//-------------------------------------------------------------------------------------
    public void run() {
    }
    
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
