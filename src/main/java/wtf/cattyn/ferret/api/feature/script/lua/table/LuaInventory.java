package wtf.cattyn.ferret.api.feature.script.lua.table;

import com.github.kevinsawicki.http.HttpRequest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import wtf.cattyn.ferret.common.Globals;
import wtf.cattyn.ferret.common.impl.trait.Enumerable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class LuaInventory extends LuaTable implements Globals, Enumerable<LuaInventory.ItemData> {

    public LuaInventory() {
        this.set("enumerate", new OneArgFunction() {
            @Override public LuaValue call(LuaValue arg) {
                if (arg.isclosure()) enumerate(( LuaClosure ) arg);
                return NIL;
            }
        });
        this.set("getStack", new OneArgFunction() {
            @Override public LuaValue call(LuaValue arg) {
                if (mc.player == null || !arg.isint()) return NIL;
                return CoerceJavaToLua.coerce(mc.player.getInventory().getStack(arg.toint()));
            }
        });
        this.set("OFFHAND", LuaValue.valueOf(45));
        this.set("HELMET", LuaValue.valueOf(39));
        this.set("CHESTPLATE", LuaValue.valueOf(38));
        this.set("LEGGINGS", LuaValue.valueOf(37));
        this.set("BOOTS", LuaValue.valueOf(36));
    }

    @NotNull @Override public Iterator<ItemData> iterator() {
        if (mc.player == null) return Collections.emptyIterator();
        final List<ItemData> dataList = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            dataList.add(new ItemData(stack, i, stack.getItem()));
        }
        return dataList.iterator();
    }

    record ItemData(ItemStack stack, int slot, Item item) { }

}
