//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.components.panels;
//-------------------------------------------------------------------------------------
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import codex.common.utils.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;
import codex.xbit.api.common.packets.*;

import codex.xbit.api.client.net.*;
import codex.xbit.api.client.cli.*;

import codex.xbit.api.client.gui.status.*;
import codex.xbit.api.client.gui.tab.*;


//-------------------------------------------------------------------------------------
public abstract class AbstractPanel extends JPanel implements Runnable {
	protected Debug debug;
    protected Status status;
    protected Tab tab;

//-------------------------------------------------------------------------------------
    public AbstractPanel(Debug _debug, Status _status, Tab _tab) {
        debug = _debug;
        status = _status;
        tab = _tab;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
//        reload();
    }

//-------------------------------------------------------------------------------------
    public void start(){
        Thread _t = new Thread(this);
        _t.start();
	}    	

//-------------------------------------------------------------------------------------
    public abstract void run();

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void reload() {
        revalidate();
        repaint();
        if(tab != null){
        	tab.reload();
        }
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
