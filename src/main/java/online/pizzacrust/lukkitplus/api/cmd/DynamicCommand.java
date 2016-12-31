package online.pizzacrust.lukkitplus.api.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.lukkitplus.api.LuaAccessor;

public class DynamicCommand extends Command {
    private final LuaValue function;

    public DynamicCommand(String name, String description, String usageMessage, LuaValue luaValue) {
        super(name, description, usageMessage, new ArrayList<String>());
        this.function = luaValue;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        List<LuaValue> args = new ArrayList<>();
        for (String string :strings) {
            args.add(LuaValue.valueOf(string));
        }
        function.call(new LuaAccessor(commandSender), LuaValue.listOf(args.toArray(new
                LuaValue[args.size()])));
        return true;
    }
}
