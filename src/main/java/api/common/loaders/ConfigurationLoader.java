//-------------------------------------------------------------------------------------
package codex.xbit.api.common.loaders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.aspects.*;

//-------------------------------------------------------------------------------------
public class ConfigurationLoader<T extends Configuration> {
    private Debug debug;
    private String path;
//    private Configuration c;
    private String extension;
    private String orderByField;
	private final Class<T> configurationClass;

    //---cache:
    private File dir;
    private ObjectMapper mapper;
    private FilenameFilter filenameFilter;

//-------------------------------------------------------------------------------------
    public ConfigurationLoader(Debug _debug, String _path, Class<T> _configurationClass){
    	this(_debug, _path, ".yaml", null, _configurationClass);
    }

//-------------------------------------------------------------------------------------
    public ConfigurationLoader(Debug _debug, String _path, String _extension, Class<T> _configurationClass){
    	this(_debug, _path, _extension, null, _configurationClass);
    }

@SuppressWarnings("deprecation")
//-------------------------------------------------------------------------------------
    public ConfigurationLoader(Debug _debug, String _path, String _extension, String _orderByField, Class<T> _configurationClass){
    	debug = _debug;
//    	path = SystemUtils.getHomeDirectory() + "/" + _path;
    	path = _path;
//    	c = _c; 
    	extension = _extension;
    	orderByField = _orderByField;
		configurationClass = _configurationClass;

    	dir = new File(_path);
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
//		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
//		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

		filenameFilter = new FilenameFilter() {
			public boolean accept(File _dir, String _name) {
//				debug.out("FilenameFilter.accept: "+_name);
				String _lowercaseName = _name.toLowerCase();
				if (_lowercaseName.endsWith(extension)) {
//					debug.out("FilenameFilter.accept: 0");
					return true;
				} 
				File _file = new File(_dir, _name);
				if (_file.isDirectory()) {
//					debug.out("FilenameFilter.accept: 1");
					return true;
				} 
//					debug.out("FilenameFilter.accept: 2");
				return false;
			}
		};
	}

//-------------------------------------------------------------------------------------
    public List<T> load() {
//        String _home = SystemUtils.getHomeDirectory();
//        String _fullpath = _home + "/" + path;
//        debug.outln("Load "+path);
        File _dir = new File(path);
        return load(_dir, null);
    }

//-------------------------------------------------------------------------------------
    private List<T> load(File _dir, String _field) {
        File[] _files = _dir.listFiles(filenameFilter);
//        debug.outln("Load "+_field);
        List<T> _list = new ArrayList<T>();
        if(_files == null){
        	return null;
        }
        for (File _file : _files) {
        	if((orderByField != null) && _file.isDirectory() && !_file.isHidden()){
        		_field = _file.getName();
        		_list.addAll(load(_file, _field));
        	} else if(_file.isFile() && !_file.isHidden()){
	            T _t = loadFile(_file, _field);
	            _list.add(_t);
        	}
        }
        return _list;
    }

//-------------------------------------------------------------------------------------
    public T load(String _name) {
        File _dir = new File(path);
        File _file = new File(_dir.getAbsolutePath() + "/"+ _name + extension);
//        debug.outln(Debug.WARNING, " Loading "+_file.getAbsolutePath() + " ...");
        if(!_file.exists()){
        	debug.outln(Debug.ERROR, " Configuration file "+_file.getAbsolutePath() + " does not extists!!!");
        	return null;
        }
//		debug.outln(Debug.ERROR, " Loading... "+_file.getAbsolutePath());
		T _t = loadFile(_file, null);
		_t.setDebug(debug);
		return _t;
    }

//-------------------------------------------------------------------------------------
    private T loadFile(File _file, String _field) {
//        Path _path = Paths.get(rootDirectory.toPath() + "/api.yaml");
        String _pathString = _file.toString();
//        debug.out(Debug.IMPORTANT3, "Loading "+_pathString+" ... ");
//        File _file = new File(_path.toString());
        T _t = null;
        try {
            _t = mapper.readValue(_file, configurationClass);
            String _name = _file.getName();
            _name = _name.replace(extension, "");
            _t.initiate(debug, _name, this);
//            debug.out(Debug.IMPORTANT3, "OK [ ", false);
            if(_t.isEnabled()){
//                debug.out(Debug.IMPORTANT1, "enabled", false);
            } else {
//                debug.out(Debug.ERROR, "disabled", false);       
            }
 //           debug.outln(Debug.IMPORTANT3, " ]", false);
        	return _t;
        } catch (Exception e) {
            debug.outln(Debug.ERROR, "error", false);
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void save(List<T> _list) {
        String _home = SystemUtils.getHomeDirectory();
        String _fullpath = _home + path;
        _list.forEach(_t -> {
            save(_t);
        }); 
    }

//-------------------------------------------------------------------------------------
    public void save(T _t) {
//        String _pathDir = dir.getPath();
//        String _filename = StringUtils.formatTimeAsFilename(_time) + ".orderbook";
//        String _filename = Long.toString(_time) + ".orderbook";
//        debug.out(Debug.IMPORTANT3, "PATH= "+path+" ... ");
        String _filename = _t.getName() + extension;
        String _path = path + "/";
        if(orderByField != null){//!orderByField.equals("")){
        	String _orderByFieldValue;
			try {
				java.lang.reflect.Field _field = _t.getClass().getDeclaredField(orderByField);
				_field.setAccessible(true);
				_orderByFieldValue = _field.get(_t).toString();
				_orderByFieldValue = _orderByFieldValue.replace("/","_").toLowerCase();
			} catch (Exception _e)	{
				debug.outln(Debug.ERROR, "ConfigurationLoader.save cannot find field: ["+orderByField+"]");
				System.exit(0);
				return;
			}
        	_path = _path + _orderByFieldValue + "/";
        }
        FileUtils.ifDoesntExistCreate(_path);
        _path = _path + _filename;
        File _file = new File(_path);
//        String _path = _file.getPath();
        debug.out(Debug.IMPORTANT3, "Saving "+_path+" ... ");
        try {
//            _orderbook.pack();
            mapper.writeValue(_file, _t);
            debug.out(Debug.IMPORTANT3, "OK [ ", false);
            if(_t.isEnabled()){
                debug.out(Debug.IMPORTANT1, "enabled", false);
            } else {
                debug.out(Debug.ERROR, "disabled", false);       
            }
            debug.outln(Debug.IMPORTANT3, " ]", false);
        } catch (Exception e) {
            debug.outln(Debug.ERROR, "error", false);
            e.printStackTrace();
            System.exit(0);
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//    private getField save(T _t) {

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
