package wtf.cattyn.ferret.asm;

import org.apache.commons.io.IOUtils;
import org.luaj.vm2.ast.Exp;
import org.luaj.vm2.ast.Visitor;
import org.luaj.vm2.parser.LuaParser;
import wtf.cattyn.ferret.core.MixinPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScriptMixinParser
{
    public static List< ScriptMixin > getMixins( )
    {
        File[ ] scripts = getScripts( );
        if( scripts == null || scripts.length == 0 ) return null;

        List< ScriptMixin > ret = new ArrayList< >( );
        List< ScriptMixin > current = new ArrayList< >( );

        for( File file : scripts )
        {
            current.clear( );

            if( !file.getName( ).endsWith( ".lua" ) ) continue;

            boolean failed = false;

            try
            {
                FileInputStream fis = new FileInputStream( file );
                String text = IOUtils.toString( fis, StandardCharsets.UTF_8 );
                String[ ] split = text.split( "\n" );
                fis = new FileInputStream( file ); // reset

                LuaParser parser = new LuaParser( fis );
                parser.Chunk( ).accept( new Visitor( )
                {
                    public void visit( Exp.MethodCall exp )
                    {
                        super.visit( exp );

                        if( exp.name.equals( "mixinInject" ) &&
                            split[ exp.beginLine - 1 ].contains( ":mixinInject" ) &&
                            exp.args.exps.size( ) > 0 )
                        {
                            ScriptMixin mixin = new ScriptMixin( );
                            mixin.type = "INJECT"; // todo @Redirect

                            for( int i = 0; i < exp.args.exps.size( ); i++ )
                            {
                                Exp e = exp.args.exps.get( i );
                                if( e == null ) continue;

                                if( Math.abs( e.beginLine - e.endLine ) < 2 )
                                {
                                    StringBuilder sb = new StringBuilder( );
                                    if( e.beginLine == e.endLine )
                                    {
                                        String line = split[ e.beginLine - 1 ];
                                        sb.append( line.substring( e.beginColumn, e.endColumn ) );
                                    }
                                    else
                                    {
                                        int j = e.beginLine - 1;
                                        while( j != e.endLine )
                                        {
                                            String line = null;
                                            if( j + 1 == e.endLine )
                                            {
                                                line = split[ j ];
                                                sb.append( line.substring( 0, e.endColumn ) );
                                            }
                                            else
                                            {
                                                line = split[ j ];
                                                sb.append( line.substring( e.beginColumn ) );
                                            }

                                            j++;
                                        }
                                    }

                                    String whitelist = whitelistChars( sb.toString( ) );
                                    switch (i) {
                                        case 0 -> // classname
                                                mixin.classname = whitelist;
                                        case 1 -> // method
                                                mixin.method = whitelist;
                                        case 2 -> // at
                                                mixin.at = whitelist;
                                        case 3 -> // arg count
                                                mixin.args = Integer.parseInt(whitelist);
                                        case 4 -> // (optional) @At target
                                                mixin.attarget = whitelist;
                                        case 5 -> // (optional) @At remap
                                                mixin.atremap = Boolean.parseBoolean(whitelist);
                                        case 6 -> // (optional) @At ordinal
                                                mixin.atordinal = Integer.parseInt(whitelist);
                                        default -> {
                                        }
                                    }
                                }
                            }

                            mixin.callbackname = generateCallbackName( );
                            current.add( mixin );
                        }
                    }
                } );
            }
            catch( Exception e )
            {
                failed = true;
                MixinPlugin.LOGGER.warn( "[ScriptMixinParser] Failed to parse some lua file" );
            }

            if( !failed )
            {
                for( ScriptMixin mixin : current )
                {
                    // checking for duplicates
                    boolean found = false;
                    for( ScriptMixin _mixin : ret )
                    {
                        if( mixin.at.equals( _mixin.at ) &&
                            mixin.method.equals( _mixin.method ) &&
                            mixin.classname.equals( _mixin.classname ) &&
                            mixin.attarget.equals( _mixin.attarget ) &&
                            mixin.type.equals( _mixin.type ) )
                            found = true;
                    }

                    if( found ) continue;

                    /*if( true ) // debug
                    {
                        System.out.println( "[ScriptMixinParser] Adding new mixin" );
                        System.out.println( "[ScriptMixinParser] -> class: " + mixin.classname );
                        System.out.println( "[ScriptMixinParser] -> type: " + mixin.type );
                        System.out.println( "[ScriptMixinParser] -> method: " + mixin.method );
                        System.out.println( "[ScriptMixinParser] -> at: " + mixin.at );
                        System.out.println( "[ScriptMixinParser] -> at target: " + mixin.attarget );
                        System.out.println( "[ScriptMixinParser] -> args: " + mixin.args );
                        System.out.println( "[ScriptMixinParser] -> callback: " + mixin.callbackname );
                    }*/

                    ret.add( mixin );
                }
            }
        }

        return ret;
    }

    // i dont care
    public static String whitelistChars( String input )
    {
        String whitelist = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890,.$/();:_";
        StringBuilder sb = new StringBuilder( );
        for( int i = 0; i < input.length( ); i++ )
        {
            boolean found = false;
            for( int j = 0; j < whitelist.length( ); j++ )
            {
                if( whitelist.charAt( j ) == input.charAt( i ) )
                {
                    found = true;
                    break;
                }
            }

            if( found )
                sb.append( input.charAt( i ) );
        }

        return sb.toString( );
    }

    public static String generateCallbackName( )
    {
        String whitelist = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
        StringBuilder sb = new StringBuilder( );
        while( sb.length( ) < 10 )
            sb.append( whitelist.charAt( new Random( ).nextInt( whitelist.length( ) ) ) );

        return sb.toString( );
    }

    public static File[ ] getScripts( )
    {
        File folder = new File( "ferret/scripts/" );
        if( !folder.exists( ) ) return null;

        return folder.listFiles( );
    }
}