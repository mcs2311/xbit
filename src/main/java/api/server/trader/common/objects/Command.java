//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.objects;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


//-------------------------------------------------------------------------------------
public class Command {
	private int code;
	private long id;

    //---statics:
	public static final int COMMAND_CODE_PLACE 							= 0;
	public static final int COMMAND_CODE_CANCEL 						= 1;
	public static final int COMMAND_CODE_CHANGE 						= 2;

	public static final int COMMAND_CODE_RESET 							= 3;
	public static final int COMMAND_CODE_STOP 							= 4;

	public static Command COMMAND_PLACE 		= new Command(Command.COMMAND_CODE_PLACE 	);
	public static Command COMMAND_CANCEL 		= new Command(Command.COMMAND_CODE_CANCEL 	);
	public static Command COMMAND_CHANGE 		= new Command(Command.COMMAND_CODE_CHANGE 	);

	public static Command COMMAND_RESET 		= new Command(Command.COMMAND_CODE_RESET 	);
	public static Command COMMAND_STOP 			= new Command(Command.COMMAND_CODE_STOP 	);

//-------------------------------------------------------------------------------------
    public Command(int _code) {
    	this(_code, 0L);
    }

//-------------------------------------------------------------------------------------
    public Command(int _code, long _id) {
    	code = _code;
    	id = _id;
    }

//-------------------------------------------------------------------------------------
    public int getCode() {
    	return code;
    }

//-------------------------------------------------------------------------------------
    public void setCode(int _code) {
    	code = _code;
    }

//-------------------------------------------------------------------------------------
    public long getId() {
    	return id;
    }

//-------------------------------------------------------------------------------------
    public String toString() {
    	return "Command: " + code + " : " + id;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
