package online.pizzacrust.lukkitplus.environment;

import org.luaj.vm2.LuaValue;

public interface FunctionController {

    String getName();

    LuaValue onCalled(LuaValue... parameters);

    enum Type {
        ZERO,
        ONE,
        TWO,
        THREE
    }

}
