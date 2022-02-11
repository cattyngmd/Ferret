package wtf.cattyn.ferret.asm;

public class ScriptMixin
{
    public String type; // inject/redirect
    public String classname;
    public String method;
    public String at;
    public String attarget; // can be null
    public boolean atremap; // default is true
    public int atordinal; // default is -1
    public int args; // function argument count (without CallbackInfo)

    public String callbackname;

    public ScriptMixin( )
    {
        this.atremap = true;
        this.atordinal = -1;
    }
}
