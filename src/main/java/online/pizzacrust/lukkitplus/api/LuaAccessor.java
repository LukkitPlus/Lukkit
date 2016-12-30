package online.pizzacrust.lukkitplus.api;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.lukkitplus.environment.FunctionController;
import online.pizzacrust.lukkitplus.environment.LuaLibrary;

public class LuaAccessor extends LuaLibrary {

    private final Object object;

    public static Class<?>[] PRIMITIVE_CLASSES = new Class<?>[] {
            Integer.class,
            String.class,
            Double.class,
            Boolean.class,
    };

    public static boolean isPrimitive(Object object) {
        for (Class<?> primitive : PRIMITIVE_CLASSES) {
            if (primitive == object.getClass()) {
                return true;
            }
        }
        return false;
    }

    public static LuaValue convertType(Object object) {
        if (!isPrimitive(object)) {
            return LuaValue.NIL;
        }
        if (object instanceof Integer) {
            return LuaValue.valueOf((Integer) object);
        }
        if (object instanceof String) {
            return LuaValue.valueOf((String) object);
        }
        if (object instanceof Double) {
            return LuaValue.valueOf((Double) object);
        }
        if (object instanceof Boolean) {
            return LuaValue.valueOf((Boolean) object);
        }
        return LuaValue.NIL;
    }

    public LuaAccessor(Object object) {
        this.object = object;
        newFunction(new SetPrimitiveField(object));
        newFunction(new InvokeVoidMethod(object));
        newFunction(new AccessPrimitiveField(object));
        newFunction(new AccessJavaTypeField(object));
        newFunction(new AccessJavaTypeField(object));
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
            return luaValue.toboolean();
        }
        if (luaValue.isint()) {
            return luaValue.toint();
        }
        if (luaValue.isnumber()) {
            return luaValue.todouble();
        }
        if (luaValue.isstring()) {
            return luaValue.tostring();
        }
        return null;
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
            int index = 1;
            while (index != parameters.narg()) {
                objParameters.add(parameters.arg(index));
            }
            for (Method method : object.getClass().getMethods()) {
                method.setAccessible(true);
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
            int index = 1;
            while (index != parameters.narg()) {
                objParameters.add(parameters.arg(index));
            }
            for (Method method : object.getClass().getMethods()) {
                method.setAccessible(true);
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
            int index = 1;
            while (index != parameters.narg()) {
                objParameters.add(parameters.arg(index));
            }
            for (Method method : object.getClass().getMethods()) {
                method.setAccessible(true);
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
