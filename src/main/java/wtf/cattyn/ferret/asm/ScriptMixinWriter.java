package wtf.cattyn.ferret.asm;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fuck.you.yarnparser.V1Parser;
import fuck.you.yarnparser.entry.ClassEntry;
import fuck.you.yarnparser.entry.MethodEntry;
import org.objectweb.asm.*;
import wtf.cattyn.ferret.api.manager.impl.MappingManager;
import wtf.cattyn.ferret.core.MixinPlugin;

public class ScriptMixinWriter
{
    public static String REFMAP = "";
    public static Object[ ] createMixin( ScriptMixin mixin )
    {
        ClassEntry e = MappingManager.getInstance( ).getParser( ).findClass
                ( mixin.classname.replace( '.', '/' ), V1Parser.ClassFindType.NAMED );

        if( e == null )
        {
            MixinPlugin.LOGGER.warn( "Failed to build script mixin class " + mixin.classname + "/" + mixin.method + " [class remap fail]" );
            return null;
        }

        MethodEntry m = MappingManager.getInstance( ).getParser( ).findMethod(
                e.intermediary, mixin.method, V1Parser.NormalFindType.NAMED, mixin.args );

        if( m == null )
        {
            MixinPlugin.LOGGER.warn( "Failed to build script mixin class " + mixin.classname + "/" + mixin.method + " [method remap fail]" );
            return null;
        }

        String mixinname = "Mixin" + mixin.classname.replace( ".", "" ) + mixin.callbackname;
        mixinname = mixinname.replace( "/", "" );
        mixinname = "wtf/cattyn/ferret/mixins/" + mixinname;

        ClassWriter writer = new ClassWriter( 0 );
        writer.visit( 60, Opcodes.ACC_PUBLIC, mixinname,
                null, "java/lang/Object", null );

        // @Mixin( Class.class )
        // asm can be retarded sometimes
        AnnotationVisitor v = writer.visitAnnotation( "Lorg/spongepowered/asm/mixin/Mixin;", false );
        AnnotationVisitor v2 = v.visitArray( "value" );
        v2.visit( null, Type.getObjectType( e.intermediary ) );
        v2.visitEnd( );
        v.visitEnd( );

        // create a field that will contain the arguments
        {
            FieldVisitor fv = writer.visitField( Opcodes.ACC_PRIVATE, "args", "[Ljava/lang/Object;", null, null );
            fv.visitEnd( );
        }

        Type[ ] types = Type.getArgumentTypes( m.type );
        String[ ] strtypes = new String[ types.length ];

        for( int i = 0; i < types.length; i++ )
        {
            Type t = types[ i ];
            String str = t.getClassName( );
            // todo fix arrays if they are broken
            switch( str )
            {
                case "void":
                    strtypes[ i ] = "V";
                    break;
                case "boolean":
                    strtypes[ i ] = "Z";
                    break;
                case "char":
                    strtypes[ i ] = "C";
                    break;
                case "byte":
                    strtypes[ i ] = "B";
                    break;
                case "short":
                    strtypes[ i ] = "S";
                    break;
                case "int":
                    strtypes[ i ] = "I";
                    break;
                case "float":
                    strtypes[ i ] = "F";
                    break;
                case "long":
                    strtypes[ i ] = "J";
                    break;
                case "double":
                    strtypes[ i ] = "D";
                    break;
                default:
                    ClassEntry e2 = MappingManager.getInstance( ).getParser( ).findClass(
                            str, V1Parser.ClassFindType.OFFICIAL );
                    if( e2 != null )
                        strtypes[ i ] = e2.intermediary;
                    else
                        strtypes[ i ] = str;

                    break;
            }
        }

        // remap return type
        Type rettype = Type.getReturnType( m.type );
        String strtype = rettype.getClassName( );
        ClassEntry e3 = MappingManager.getInstance( ).getParser( ).findClass(
                strtype, V1Parser.ClassFindType.OFFICIAL );
        if( e3 != null )
            strtype = e3.intermediary;

        String alltypes = "(";
        for( String t : strtypes )
        {
            if( t.length( ) == 1 )
                alltypes += t;
            else
                alltypes += "L" + t.replace( '.', '/' ) + ";";
        }

        String typespreci = alltypes + ")";

        if( strtype.equals( "void" ) )
            alltypes += "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;";
        else
        {
            String clazz = getCIReturnableClass( strtype );
            alltypes += "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable<" + clazz + ">;";
        }

        alltypes += ")V";

        if( mixin.type.equalsIgnoreCase( "inject" ) )
        {
            MethodVisitor mv = writer.visitMethod( Opcodes.ACC_PUBLIC, mixin.method, alltypes, null, null );

            AnnotationVisitor av = mv.visitAnnotation( "Lorg/spongepowered/asm/mixin/injection/Inject;", true );

            // add method
            {
                AnnotationVisitor vm = av.visitArray( "method" );
                vm.visit( null, mixin.method );
                vm.visitEnd( );
            }

            // add at
            {
                AnnotationVisitor vat = av.visitArray( "at" );

                AnnotationVisitor at = vat.visitAnnotation( null, "Lorg/spongepowered/asm/mixin/injection/At;" );

                // add at value
                at.visit( "value", mixin.at );

                // add at target (if it is defined)
                if( mixin.attarget != null )
                    at.visit( "target", mixin.attarget );

                // disable at remap
                if( !mixin.atremap )
                    at.visit( "remap", false );

                // add at ordinal (if it is defined)
                if( mixin.atordinal != -1 )
                    at.visit( "ordinal", mixin.atordinal );

                at.visitEnd( );

                vat.visitEnd( );
            }

            // add cancellable
            av.visit( "cancellable", true );

            av.visitEnd( );

            // add instructions here
            populateMethod( mixinname, mixin, mv, strtypes, types );

            mv.visitEnd( );

            // create <init>
            mv = writer.visitMethod( Opcodes.ACC_PUBLIC, "<init>", "()V", null, null );
            mv.visitMaxs( 2, 1 );
            mv.visitVarInsn( Opcodes.ALOAD, 0 );
            mv.visitMethodInsn( Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V" );
            mv.visitInsn( Opcodes.RETURN );
            mv.visitEnd( );
        }

        writer.visitEnd( );

        // updating refmap
        addReference( mixinname, mixin.method,
                "L" + e.intermediary + ";" + m.intermediary + typespreci + getReturnableType( strtype ) );

        return new Object[ ]{ mixinname, writer.toByteArray( ) };
    }

    public static void populateMethod( String classname, ScriptMixin mixin, MethodVisitor mv, String[ ] strtypes, Type[ ] types )
    {
        // needed for later use
        int event = 2;
        for( int i = 0; i < strtypes.length; i++ )
            event += types[ i ].getSize( );

        Label label = new Label( );
        mv.visitLabel( label );

        // this.args = new Object[ argcount ];
        mv.visitVarInsn( Opcodes.ALOAD, 0 ); // load this
        mv.visitIntInsn( Opcodes.BIPUSH, mixin.args ); // load argcount
        mv.visitTypeInsn( Opcodes.ANEWARRAY,"java/lang/Object" ); // create new Object array
        mv.visitFieldInsn( Opcodes.PUTFIELD, classname, "args", "[Ljava/lang/Object;" ); // set args to our newly created array

        // populate args array with arguments
        int arg = 1; // fuck java
        for( int i = 0; i < strtypes.length; i++ )
        {
            Label label2 = new Label( );
            mv.visitLabel( label2 );
            storeValue( strtypes[ i ], classname, arg, mv, i );

            arg += types[ i ].getSize( );
        }

        label = new Label( );
        mv.visitLabel( label );

        // creating new event
        mv.visitTypeInsn( Opcodes.NEW, "wtf/cattyn/ferret/impl/events/MixinCallbackEvent" ); // create new MixinCallbackEvent
        mv.visitInsn( Opcodes.DUP ); // do this or die
        // initialize event object (call "public MixinCallbackEvent()")
        mv.visitMethodInsn( Opcodes.INVOKESPECIAL,
                "wtf/cattyn/ferret/impl/events/MixinCallbackEvent",
                "<init>", "()V" );
        mv.visitVarInsn( Opcodes.ASTORE, event ); // store event

        label = new Label( );
        mv.visitLabel( label );

        // setting event name to our callback name
        mv.visitVarInsn( Opcodes.ALOAD, event ); // load event
        mv.visitLdcInsn( mixin.callbackname ); // load callback name
        // set event name to callback name
        mv.visitFieldInsn( Opcodes.PUTFIELD,
                "wtf/cattyn/ferret/impl/events/MixinCallbackEvent",
                "name", "Ljava/lang/String;" );

        label = new Label( );
        mv.visitLabel( label );

        // setting event args to this.args
        mv.visitVarInsn( Opcodes.ALOAD, event ); // load event
        mv.visitVarInsn( Opcodes.ALOAD, 0 ); // load this
        mv.visitFieldInsn( Opcodes.GETFIELD, classname, "args", "[Ljava/lang/Object;" ); // get args
        // set event args to this.args
        mv.visitFieldInsn( Opcodes.PUTFIELD,
                "wtf/cattyn/ferret/impl/events/MixinCallbackEvent",
                "args", "[Ljava/lang/Object;" );

        label = new Label( );
        mv.visitLabel( label );

        // setting event CallbackInfo
        mv.visitVarInsn( Opcodes.ALOAD, event ); // load event
        mv.visitVarInsn( Opcodes.ALOAD, arg ); // loading info (its the last function argument)
        // set event CallbackInfo
        mv.visitFieldInsn( Opcodes.PUTFIELD,
                "wtf/cattyn/ferret/impl/events/MixinCallbackEvent",
                "info", "Ljava/lang/Object;" );

        label = new Label( );
        mv.visitLabel( label );

        // run event callbacks
        // Ferret.getDefault().getScripts()
        mv.visitMethodInsn( Opcodes.INVOKESTATIC,
                "wtf/cattyn/ferret/core/Ferret",
                "getDefault", "()Lwtf/cattyn/ferret/core/Ferret;" );
        mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
                "wtf/cattyn/ferret/core/Ferret",
                "getScripts", "()Lwtf/cattyn/ferret/api/manager/impl/ScriptManager;" );
        mv.visitVarInsn( Opcodes.ALOAD, event ); // load event
        // get event name (callback name)
        mv.visitFieldInsn( Opcodes.GETFIELD,
                "wtf/cattyn/ferret/impl/events/MixinCallbackEvent",
                "name", "Ljava/lang/String;" );
        mv.visitVarInsn( Opcodes.ALOAD, event ); // load event
        // CoerceJavaToLua
        mv.visitMethodInsn( Opcodes.INVOKESTATIC,
                "org/luaj/vm2/lib/jse/CoerceJavaToLua",
                "coerce", "(Ljava/lang/Object;)Lorg/luaj/vm2/LuaValue;" );
        // run callback
        mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL,
                "wtf/cattyn/ferret/api/manager/impl/ScriptManager",
                "runCallback", "(Ljava/lang/String;Lorg/luaj/vm2/LuaValue;)V" );

        // set arguments
        arg = 1;
        for( int i = 0; i < strtypes.length; i++ )
        {
            Label label2 = new Label( );
            mv.visitLabel( label2 );
            setValue( strtypes[ i ], event, arg, mv, i );

            arg += types[ i ].getSize( );
        }

        // finish
        label = new Label( );
        mv.visitLabel( label );

        mv.visitInsn( Opcodes.RETURN );
    }

    public static void storeValue( String type, String classname, int count, MethodVisitor mv, int arrcount )
    {
        // this.args
        mv.visitVarInsn( Opcodes.ALOAD, 0 ); // load this
        mv.visitFieldInsn( Opcodes.GETFIELD, classname, "args", "[Ljava/lang/Object;" ); // get args

        mv.visitIntInsn( Opcodes.BIPUSH, arrcount );
        switch( type )
        {
            case "V":
                MixinPlugin.LOGGER.error( "Are you retarded or something" );
                mv.visitInsn( Opcodes.ACONST_NULL ); // store null
                break;
            case "I": // int
                mv.visitVarInsn( Opcodes.ILOAD, count ); // get argument
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf",
                        "(I)Ljava/lang/Integer;" ); // convert to literally what ?? i dont fucking know but java does this
                break;
            case "D": // double
                mv.visitVarInsn( Opcodes.DLOAD, count );
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf",
                        "(D)Ljava/lang/Double;" );
                break;
            case "F": // float
                mv.visitVarInsn( Opcodes.FLOAD, count );
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf",
                        "(F)Ljava/lang/Float;" );
                break;
            case "J": // long
                mv.visitVarInsn( Opcodes.LLOAD, count );
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf",
                        "(J)Ljava/lang/Long;" );
                break;
            case "C": // char
                mv.visitVarInsn( Opcodes.ILOAD, count );
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf",
                        "(C)Ljava/lang/Character;" );
                break;
            case "B": // byte
                mv.visitVarInsn( Opcodes.ILOAD, count );
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf",
                        "(B)Ljava/lang/Byte;" );
                break;
            case "S": // short
                mv.visitVarInsn( Opcodes.ILOAD, count );
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf",
                        "(S)Ljava/lang/Short;" );
                break;
            case "Z":
                mv.visitVarInsn( Opcodes.ILOAD, count );
                mv.visitMethodInsn( Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf",
                        "(Z)Ljava/lang/Boolean;" );
                break;
            default: // strings and everything else
                mv.visitVarInsn( Opcodes.ALOAD, count );
                break;
        }
        mv.visitInsn( Opcodes.AASTORE ); // store argument in args array
    }

    public static void setValue( String type, int event, int count, MethodVisitor mv, int arrcount )
    {
        // event.args
        mv.visitVarInsn( Opcodes.ALOAD, event ); // load event
        // get event args
        mv.visitFieldInsn( Opcodes.GETFIELD,
                "wtf/cattyn/ferret/impl/events/MixinCallbackEvent",
                "args", "[Ljava/lang/Object;" );

        mv.visitIntInsn( Opcodes.BIPUSH, arrcount );
        mv.visitInsn( Opcodes.AALOAD );

        switch( type )
        {
            case "I": // int
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Integer" ); // cast argument to (Integer)
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I" ); // i DONT fucking know
                mv.visitVarInsn( Opcodes.ISTORE, count ); // set argument
                break;
            case "C": // char
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Character" );
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C" );
                mv.visitVarInsn( Opcodes.ISTORE, count );
                break;
            case "B": // byte
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Byte" );
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B" );
                mv.visitVarInsn( Opcodes.ISTORE, count );
                break;
            case "S": // short
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Short" );
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S" );
                mv.visitVarInsn( Opcodes.ISTORE, count );
                break;
            case "java.lang.String": // string (check if this is correct)
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/String" );
                mv.visitVarInsn( Opcodes.ASTORE, count );
                break;
            case "Z": // boolean
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Boolean" );
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z" );
                mv.visitVarInsn( Opcodes.ISTORE, count );
                break;
            case "F": // float
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Float" );
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F" );
                mv.visitVarInsn( Opcodes.FSTORE, count );
                break;
            case "J": // long
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Long" );
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J" );
                mv.visitVarInsn( Opcodes.LSTORE, count );
                break;
            case "D": // double
                mv.visitTypeInsn( Opcodes.CHECKCAST, "java/lang/Double" );
                mv.visitMethodInsn( Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D" );
                mv.visitVarInsn( Opcodes.DSTORE, count );
                break;
            default: // everything else
                mv.visitTypeInsn( Opcodes.CHECKCAST, type.replace( '.', '/' ) );
                mv.visitVarInsn( Opcodes.ASTORE, count );
                break;
        }
    }

    public static String getReturnableType( String in ) // for refmaps
    {
        if( in.startsWith( "java.lang" ) )
            return "L" + in.replace( '.', '/' ) + ";";
        else if( in.startsWith( "java/lang/" ) ) // just to be sure
            return "L" + in + ";";

        switch( in )
        {
            case "int":
                return "I";
            case "long":
                return "J";
            case "boolean":
                return "Z";
            case "char":
                return "C";
            case "byte":
                return "B";
            case "short":
                return "S";
            case "float":
                return "F";
            case "double":
                return "D";
            case "void":
                return "V";
            default:
                return "L" + in.replace( '.', '/' ) + ";";
        }
    }

    public static String getCIReturnableClass( String in )
    {
        if( in.startsWith( "java.lang" ) )
            return "L" + in.replace( '.', '/' ) + ";";
        else if( in.startsWith( "java/lang/" ) ) // just to be sure
            return "L" + in + ";";

        switch( in )
        {
            case "int":
                return "Ljava/lang/Integer;";
            case "long":
                return "Ljava/lang/Long;";
            case "boolean":
                return "Ljava/lang/Boolean;";
            case "char":
                return "Ljava/lang/Character;";
            case "byte":
                return "Ljava/lang/Byte;";
            case "short":
                return "Ljava/lang/Short;";
            case "float":
                return "Ljava/lang/Float;";
            case "double":
                return "Ljava/lang/Double;";
            default:
                return "L" + in.replace( '.', '/' ) + ";";
        }
    }

    // 0$ cattyn code
    public static void addReference(String a, String b, String c) {
        JsonObject object = JsonParser.parseString(REFMAP).getAsJsonObject();
        JsonObject niggerMapping = new JsonObject(), niggerData = new JsonObject();
        niggerMapping.addProperty(b, c);
        niggerData.addProperty(b, c);
        object.get("mappings").getAsJsonObject().add(a, niggerMapping);
        object.get("data").getAsJsonObject().get("named:intermediary").getAsJsonObject().add(a, niggerData);
        REFMAP = new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }
}