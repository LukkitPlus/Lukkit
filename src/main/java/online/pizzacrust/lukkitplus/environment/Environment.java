package online.pizzacrust.lukkitplus.environment;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

public class Environment {

    public static final Globals GLOBAL_PATH = new Globals();

    public static void loadCoreLibs() {
        GLOBAL_PATH.load(new JseBaseLib());
        GLOBAL_PATH.load(new PackageLib());
        GLOBAL_PATH.load(new Bit32Lib());
        GLOBAL_PATH.load(new TableLib());
        GLOBAL_PATH.load(new StringLib());
        GLOBAL_PATH.load(new CoroutineLib());
        GLOBAL_PATH.load(new JseMathLib());
        GLOBAL_PATH.load(new JseIoLib());
        GLOBAL_PATH.load(new JseOsLib());
    }

    public static void loadLukkitLibs() {
        GLOBAL_PATH.load(new JavaLibInteractionTest());

    }

}
