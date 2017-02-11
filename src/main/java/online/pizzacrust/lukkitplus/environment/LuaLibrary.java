package online.pizzacrust.lukkitplus.environment;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents a library in Lua from Java.
 */
public abstract class LuaLibrary extends LuaTable {

    public void newFunction(final FunctionController controller) {
        set(controller.getName(), new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                return controller.onCalled(varargs);
            }
        });
    }

    public void newFunction(String name, Function<Varargs, LuaValue> varargsConsumer) {
        set(name, new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                return varargsConsumer.apply(varargs);
            }
        });
    }

    public void autoFunc(Object instance, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AutoFunc.class)) {
                String name = method.getName();
                newFunction(name, (varargs) -> {
                    try {
                        Object object = method.invoke(instance, varargs);
                        if (object != null) {
                            return (LuaValue) object;
                        } else return LuaValue.NIL;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return LuaValue.NIL;
                });
            }
        }
    }

    /**
     * Requires to be loaded to global classpath because uses environment object.
     */
    public static abstract class StaticLibrary extends LuaLibrary {

        public abstract String getName();

        @Override
        public LuaValue call(LuaValue modname, LuaValue env) {
            env.set(getName(), this);
            return this;
        }

    }

}
