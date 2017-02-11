package online.pizzacrust.lukkitplus.api;

import org.bukkit.attribute.AttributeInstance;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import online.pizzacrust.lukkitplus.environment.AutoFunc;
import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class AttributeType extends LuaLibrary {

    private final AttributeInstance instance;

    public AttributeType(AttributeInstance instance) {
        this.instance = instance;
        set("baseValue", instance.getBaseValue());
        //newFunction(new SetBaseValue(instance));
        autoFunc(this, AttributeType.class);
    }

    @AutoFunc
    public LuaValue setBaseValue(Varargs parameters) {
        instance.setBaseValue((Double) CoerceLuaToJava.coerce(parameters.arg(1), Double.class));
        return null;
    }

    @Deprecated
    public static class SetBaseValue implements FunctionController {
        private final AttributeInstance instance;

        public SetBaseValue(AttributeInstance instance) {
            this.instance = instance;
        }

        @Override
        public String getName() {
            return "setBaseValue";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            instance.setBaseValue((Double) CoerceLuaToJava.coerce(parameters.arg(1), Double.class));
            return LuaValue.NIL;
        }
    }

}
