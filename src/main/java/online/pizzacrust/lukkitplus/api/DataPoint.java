package online.pizzacrust.lukkitplus.api;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.lukkitplus.api.data.DataAttachment;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class DataPoint extends LuaLibrary.StaticLibrary {

    public static final List<DataAttachment> ATTACHMENTS = new ArrayList<>();

    public DataPoint() {
        newFunction(new AttachFunction());
    }

    @Override
    public String getName() {
        return "data";
    }

    public static class AttachFunction implements FunctionController {

        @Override
        public String getName() {
            return "attach";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            LuaPlugin plugin = (LuaPlugin) parameters.arg(1);
            DataAttachment dataAttachment = new DataAttachment(plugin);
            ATTACHMENTS.add(dataAttachment);
            return dataAttachment;
        }
    }

}
