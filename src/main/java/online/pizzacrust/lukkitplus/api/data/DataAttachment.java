package online.pizzacrust.lukkitplus.api.data;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Properties;

import online.pizzacrust.lukkitplus.LukkitPlus;
import online.pizzacrust.lukkitplus.api.LuaPlugin;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class DataAttachment extends LuaLibrary {

    private final LuaPlugin plugin;

    public DataAttachment(LuaPlugin plugin)
    {
        this.plugin = plugin;
        newFunction(new GetInfo(this.plugin));
        newFunction(new StoreInfo(this.plugin));
    }

    public static class GetInfo implements FunctionController {

        private final LuaPlugin plugin;

        public GetInfo(LuaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "get";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String key = parameters.arg(1).tojstring();
            Properties map = new Properties();
            File dir = new File(LukkitPlus.PLUGINS_FOLDER, plugin.getName());
            if (!dir.exists()) {
                return LuaValue.NIL;
            }
            File file = new File(dir, "datastore.data");
            if (!file.exists()) {
                return LuaValue.NIL;
            }
            try {
                map = StoreInfo.getData(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!map.containsKey(key)) {
                return LuaValue.NIL;
            }
            return LuaValue.valueOf(map.getProperty(key));
        }
    }

    public static class StoreInfo implements FunctionController {

        private final LuaPlugin plugin;

        public StoreInfo(LuaPlugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "store";
        }

        public static Properties getData(File file) throws Exception {
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            return properties;
        }

        private void setupDat(File file, String k, String v) throws Exception {
            if (!file.exists()) {
                Properties properties = new Properties();
                properties.setProperty(k, v);
                properties.store(new FileOutputStream(file), null);
                return;
            }
            Properties map = getData(file);
            map.put(k, v);
            map.store(new FileOutputStream(file), null);
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String key = parameters.arg(1).tojstring();
            String value = parameters.arg(2).tojstring();
            File dir = new File(LukkitPlus.PLUGINS_FOLDER, plugin.getName());
            if (!dir.exists()) {
                dir.mkdir();
            }
            File datFile = new File(dir, "datastore.data");
            try {
                setupDat(datFile, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return LuaValue.NIL;
        }
    }


}
