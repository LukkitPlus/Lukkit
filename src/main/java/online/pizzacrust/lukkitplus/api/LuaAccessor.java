package online.pizzacrust.lukkitplus.api;

import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

import static org.luaj.vm2.lib.jse.CoerceLuaToJava.coerce;

public class LuaAccessor extends LuaLibrary {

    private final Object object;

    public Object getObject() {
        return this.object;
    }

    public static LuaValue convertType(Object object) {
        return CoerceJavaToLua.coerce(object);
    }

    public LuaAccessor(Object object) {
        this.object = object;
        newFunction(new SetPrimitiveField(object));
        newFunction(new InvokeVoidMethod(object));
        newFunction(new AccessPrimitiveField(object));
        newFunction(new AccessJavaTypeField(object));
        newFunction(new AccessJavaTypeMethod(object));
        newFunction(new AccessPrimitiveMethod(object));
    }

    public static class SetPrimitiveField implements FunctionController {

        private final Object object;

        public SetPrimitiveField(Object object) {
            this.object = object;
        }

        @Override
        public String getName() {
            return "setPrimitiveField";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            Object value = fromType(parameters.arg(2));
            for (Field field : object.getClass().getFields()) {
                if (field.getName().equals(name)) {
                    field.setAccessible(true);
                    try {
                        field.set(object, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return LuaValue.NIL;
        }
    }

    public static Object fromType(LuaValue luaValue) {
        if (luaValue.isboolean()) {
            return coerce(luaValue, Boolean.class);
        }
        if (luaValue.isint()) {
            return coerce(luaValue, Integer.class);
        }
        if (luaValue.isnumber()) {
            return coerce(luaValue, Double.class);
        }
        if (luaValue.isstring()) {
            return LuaLogger.colorString(luaValue.tojstring());
        }
        return null;
    }

    public static List<Class<?>> convertObjectsToTypes(List<Object> object) {
        List<Class<?>> classes = new ArrayList<>();
        for (Object aObject : object) {
            classes.add(aObject.getClass());
        }
        return classes;
    }

    public static Class[] toArray(List<Class<?>> classes) {
        return classes.toArray(new Class[classes.size()]);
    }

    public static class InvokeVoidMethod implements FunctionController {

        private final Object object;

        public InvokeVoidMethod(Object object) {
            this.object = object;
        }

        @Override
        public String getName() {
            return "invokeVoidMethod";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            List<Object> objParameters = new ArrayList<Object>();
            if (parameters.narg() != 1) {
                int index = 1;
                while (index != parameters.narg()) {
                    objParameters.add(fromType(parameters.arg(index + 1)));
                    index++;
                }
            }
            for (Method method : object.getClass().getMethods()) {
                method.setAccessible(true);
                    if (Arrays.equals(toArray(convertObjectsToTypes(objParameters)), method
                            .getParameterTypes())) {
                    if (method.getName().equals(name)) {
                        try {
                            method.invoke(object, objParameters.toArray(new
                                    Object[objParameters.size
                                    ()]));
                            return LuaValue.NIL;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    }
                }
            return LuaValue.NIL;
        }
    }

    public static class AccessPrimitiveMethod implements FunctionController {

        private final Object object;

        public AccessPrimitiveMethod(Object object) {
            this.object = object;
        }

        @Override
        public String getName() {
            return "accessPrimitiveMethod";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            List<Object> objParameters = new ArrayList<Object>();
            if (parameters.narg() != 1) {
                int index = 1;
                while (index != parameters.narg()) {
                    objParameters.add(fromType(parameters.arg(index + 1)));
                    index++;
                }
            }
            for (Method method : object.getClass().getMethods()) {
                method.setAccessible(true);
                if (Arrays.equals(toArray(convertObjectsToTypes(objParameters)), method
                        .getParameterTypes())) {
                    if (method.getName().equals(name)) {
                        try {
                            return convertType(method.invoke(object, objParameters.toArray(new
                                    Object[objParameters.size
                                    ()])));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return LuaValue.NIL;
        }
    }

    public static class AccessPrimitiveField implements FunctionController {

        private final Object object;

        public AccessPrimitiveField(Object object) {
            this.object = object;
        }

        @Override
        public String getName() {
            return "accessPrimitiveField";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            for (Field field : object.getClass().getFields()) {
                field.setAccessible(true);
                if (field.getName().equals(name)) {
                    try {
                        return convertType(field.get(object));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return LuaValue.NIL;
        }
    }

    public static class AccessJavaTypeMethod implements FunctionController {

        private final Object object;

        public AccessJavaTypeMethod(Object object) {
            this.object = object;
        }

        @Override
        public String getName() {
            return "accessJavaTypeMethod";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            List<Object> objParameters = new ArrayList<Object>();
            if (parameters.narg() != 1) {
                int index = 1;
                while (index != parameters.narg()) {
                    objParameters.add(fromType(parameters.arg(index + 1)));
                    index++;
                }
            }
            for (Method method : object.getClass().getMethods()) {
                method.setAccessible(true);
                if (Arrays.equals(toArray(convertObjectsToTypes(objParameters)), method
                        .getParameterTypes())) {
                    if (method.getName().equals(name)) {
                        try {
                            return new LuaAccessor(method.invoke(object, objParameters.toArray(new
                                    Object[objParameters.size
                                    ()])));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return LuaValue.NIL;
        }
    }

    public static class AccessJavaTypeField implements FunctionController {

        private final Object object;

        public AccessJavaTypeField(Object object) {
            this.object = object;
        }

        @Override
        public String getName() {
            return "accessJavaTypeField";
        }

        @Override
        public LuaValue onCalled(Varargs parameters) {
            String name = parameters.arg(1).tojstring();
            for (Field field : object.getClass().getFields()) {
                field.setAccessible(true);
                if (field.getName().equals(name)) {
                    try {
                        return new LuaAccessor(field.get(this.object));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return LuaValue.NIL;
        }
    }


}
