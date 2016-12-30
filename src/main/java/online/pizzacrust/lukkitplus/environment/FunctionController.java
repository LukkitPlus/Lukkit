package online.pizzacrust.lukkitplus.environment;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public interface FunctionController {

    String getName();

    LuaValue onCalled(Varargs parameters);

}
