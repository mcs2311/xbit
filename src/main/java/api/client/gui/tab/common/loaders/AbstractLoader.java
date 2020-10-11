//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.tab.common.loaders;
//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.client.net.*;

import codex.xbit.api.client.gui.components.panels.*;
import codex.xbit.api.client.gui.status.*;
import codex.xbit.api.client.gui.tab.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;


//-------------------------------------------------------------------------------------
public abstract class AbstractLoader implements Runnable {
	protected Debug debug;
    protected Status status;
    protected Tab tab;
    protected DefaultXbitTreeNode parentNode;
    
//-------------------------------------------------------------------------------------
    public AbstractLoader(Debug _debug, Status _status, Tab _tab, DefaultXbitTreeNode _parentNode) {
        debug = _debug;
        status = _status;
        tab = _tab;
        parentNode = _parentNode;
    }


//-------------------------------------------------------------------------------------
    public void start(){
        Thread _t = new Thread(this);
        _t.start();
	}    	

//-------------------------------------------------------------------------------------
    public void run(){
        load();
    }

//-------------------------------------------------------------------------------------
    protected void addTab(String _id, AbstractPanel _panel){
        tab.addTab(_id, _panel);
    }

//-------------------------------------------------------------------------------------
    protected abstract void load();

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
