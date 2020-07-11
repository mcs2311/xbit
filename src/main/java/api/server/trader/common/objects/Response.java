//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.objects;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


//-------------------------------------------------------------------------------------
public class Response {
	private int code;
	private long id;

    //---statics:
	public static final int RESPONSE_CODE_ERROR 		=  -1;
    public static final int RESPONSE_CODE_PLACED 		=   1;
	public static final int RESPONSE_CODE_CANCELLED 	=   0;
    public static final int RESPONSE_CODE_EXECUTED 		=   2;
    public static final int RESPONSE_CODE_ABANDONED 	=   3;

	public static Response RESPONSE_ERROR 		= new Response(Response.RESPONSE_CODE_ERROR);
	public static Response RESPONSE_PLACED 		= new Response(Response.RESPONSE_CODE_PLACED);
	public static Response RESPONSE_CANCELLED 	= new Response(Response.RESPONSE_CODE_CANCELLED);
	public static Response RESPONSE_EXECUTED 	= new Response(Response.RESPONSE_CODE_EXECUTED);
	public static Response RESPONSE_ABANDONED 	= new Response(Response.RESPONSE_CODE_ABANDONED);

	public static final int ERROR_CODE_INSUFFICIENT_FUNDS = 0;
	
//-------------------------------------------------------------------------------------
    public Response(int _code) {
    	this(_code, 0L);
    }

//-------------------------------------------------------------------------------------
    public Response(int _code, long _id) {
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
    	return "Response: " + code + " : " + id;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
