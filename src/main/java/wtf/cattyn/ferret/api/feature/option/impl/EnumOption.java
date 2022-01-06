package wtf.cattyn.ferret.api.feature.option.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import wtf.cattyn.ferret.api.feature.Feature;
import wtf.cattyn.ferret.api.feature.option.Option;

import java.util.function.Predicate;

public class EnumOption extends Option<Enum> {

    private EnumOption(Feature feature, String name, String desc, Enum value, Predicate<Enum> visibility) {
        super(feature, name, desc, value, visibility);
    }

    @Override public void setValue(Enum value) {
        this.value = value;
    }

    @Override public void setStringValue(String value) {
        for (Enum e : this.value.getClass().getEnumConstants()) {
            if(e.name().equalsIgnoreCase(value)) this.value = e;
        }
    }

    @Override public Enum getValue() {
        return value;
    }

    @Override public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("value", value.name());
        return object;
    }

    @Override public Option<Enum> fromJson(JsonObject object) {
        JsonElement element = object.get("value");
        if (element.isJsonNull()) return this;
        for (Enum e : value.getClass().getEnumConstants()) {
            if (e.name().equalsIgnoreCase(element.getAsString())) {
                this.value = e;
                break;
            }
        }
        return this;
    }

    public static int currentEnum(Enum clazz) {
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[ i ];
            if (!e.name().equalsIgnoreCase(clazz.name())) continue;
            return i;
        }
        return -1;
    }

    public static Enum increaseEnum(Enum clazz) {
        int index = currentEnum(clazz);
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[ i ];
            if (i != index + 1) continue;
            return e;
        }
        return (( Enum[] ) clazz.getClass().getEnumConstants())[ 0 ];
    }

    public static final class Builder extends OptionBuilder<Builder, Enum, EnumOption> {

        public Builder(Enum value) {
            super(value);
        }

        @Override public EnumOption build(Feature feature) {
            if (validate()) throw new NullPointerException();
            EnumOption o = new EnumOption(feature, name, description, value, visibility);
            Option.getOptions().add(o);
            return o;
        }

    }

    //TODO make luabuilder for enums (hard for me)
//    public static final class LuaBuilder extends OneArgFunction {
//
//
//        @Override public LuaValue call(LuaValue arg) {
//            return userdataOf(new BooleanOption.Builder());
//        }
//
//    }

}
