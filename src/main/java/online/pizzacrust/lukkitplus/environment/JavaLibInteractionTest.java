package online.pizzacrust.lukkitplus.environment;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

import java.io.File;

public class JavaLibInteractionTest extends LuaLibrary {

    public JavaLibInteractionTest() {
        newFunction(new FunctionController() {
            public String getName() {
                return "helloworld";
            }

            public LuaValue onCalled(LuaValue... parameters) {
                System.out.println("Hello world!");
                return LuaValue.NIL;
            }
        }, FunctionController.Type.ZERO);
    }

    public static void main(String[] args) throws Exception {
        //Globals globals = JsePlatform.standardGlobals();
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new JavaLibInteractionTest());
        LoadState.install(globals);
        LuaC.install(globals);
        System.out.println(new File(".").getAbsolutePath());
        System.out.println(new File(args[0]).exists());
        LuaValue chunk = globals.loadfile(new File(args[0]).getAbsolutePath());
        chunk.call();
    }

    @Override
    public String getName() {
        return "examplelib";
    }
}
