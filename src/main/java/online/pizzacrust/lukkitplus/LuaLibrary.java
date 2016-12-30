package online.pizzacrust.lukkitplus;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a library in Lua from Java.
 */
public abstract class LuaLibrary extends TwoArgFunction {

    public abstract String getName();

    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue libInstance = tableOf();
        for (Map.Entry<String, LuaValue> entry : values.entrySet()) {
            libInstance.set(entry.getKey(), entry.getValue());
        }
        env.set(getName(), libInstance);
        return libInstance;
    }

    public final Map<String, LuaValue> values = new HashMap<String, LuaValue>();

    public void newFunction(final FunctionController controller, FunctionController.Type type) {
        switch (type) {
            case ZERO:
                values.put(controller.getName(), new ZeroArgFunction() {
                    @Override
                    public LuaValue call() {
                        return controller.onCalled();
                    }
                });
            case ONE:
                values.put(controller.getName(), new OneArgFunction() {
                    @Override
                    public LuaValue call(LuaValue luaValue) {
                        return controller.onCalled(luaValue);
                    }
                });
                break;
            case TWO:
                values.put(controller.getName(), new TwoArgFunction() {
                    @Override
                    public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
                        return controller.onCalled(luaValue, luaValue1);
                    }
                });
                break;
            case THREE:
                values.put(controller.getName(), new ThreeArgFunction() {
                    @Override
                    public LuaValue call(LuaValue luaValue, LuaValue luaValue1, LuaValue luaValue2) {
                        return controller.onCalled(luaValue, luaValue1, luaValue2);
                    }
                });
                break;
        }
    }

}
