package wtf.cattyn.ferret.impl.events;

import wtf.cattyn.ferret.api.event.Event;

public class MixinCallbackEvent extends Event
{
    public String name;
    public Object[ ] args;
    public Object info; // CallbackInfo/CallbackInfoReturnable

    @Override
    public String getName( )
    {
        return name;
    }

    public Object getArgument( int num )
    {
        return args[ num ];
    }
}
