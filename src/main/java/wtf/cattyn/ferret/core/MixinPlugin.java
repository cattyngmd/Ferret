package wtf.cattyn.ferret.core;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import wtf.cattyn.ferret.api.manager.impl.MappingManager;
import wtf.cattyn.ferret.asm.ScriptMixin;
import wtf.cattyn.ferret.asm.ScriptMixinParser;
import wtf.cattyn.ferret.asm.ScriptMixinWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MixinPlugin implements IMixinConfigPlugin
{
    public static Logger LOGGER = LogManager.getLogger( "Ferret" );

    private boolean called = false;
    private List< String > mixinnames = new ArrayList< >( ); // script mixin names
    public static List< ScriptMixin > MIXINS;

    @Override
    public void onLoad( String mixinPackage )
    {
        if( called ) return;
        called = true;

        LOGGER.info( "Initializing remapper" );
        long start = System.currentTimeMillis( );
        MappingManager.getInstance( ).load( );
        LOGGER.info( "Initialized remapper in " + ( System.currentTimeMillis( ) - start ) + "ms" );

        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            return;

        MIXINS = ScriptMixinParser.getMixins( );
        if( MIXINS != null && MIXINS.size( ) > 0 )
        {
            try
            {
                InputStream refmapstream = this.getClass( ).getResourceAsStream( "/Ferret-refmap.json" );
                ScriptMixinWriter.REFMAP = IOUtils.toString( refmapstream, StandardCharsets.UTF_8 );

                File mixinfile = File.createTempFile( "ferret", "scriptmixins.jar" );
                mixinfile.deleteOnExit( );
                ZipOutputStream zos = new ZipOutputStream( new FileOutputStream( mixinfile ) );

                for( ScriptMixin mixin : MIXINS )
                {
                    Object[ ] obj = ScriptMixinWriter.createMixin( mixin );
                    if( obj != null )
                    {
                        String mixinname = ( String )obj[ 0 ];

                        ZipEntry entry = new ZipEntry( mixinname + ".class" );
                        zos.putNextEntry( entry );

                        byte[ ] bytes = ( byte[ ] )obj[ 1 ];
                        zos.write( bytes, 0, bytes.length );
                        zos.closeEntry( );

                        mixinnames.add( mixinname.substring( mixinname.lastIndexOf( "/" ) + 1 ) );
                    }
                }

                ZipEntry entry = new ZipEntry( "mixins.ferret.refmap.json" );
                zos.putNextEntry( entry );
                byte[ ] refmapbytes = ScriptMixinWriter.REFMAP.getBytes( StandardCharsets.UTF_8 );
                zos.write( refmapbytes, 0, refmapbytes.length );
                zos.closeEntry( );

                zos.close( );

                ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );
                Method m = cl.getClass( ).getDeclaredMethod( "addUrlFwd", URL.class );
                m.setAccessible( true );
                m.invoke( cl, mixinfile.toURI( ).toURL( ) );
            }
            catch( Exception e )
            {
                e.printStackTrace( );
            }
        }
    }

    @Override
    public String getRefMapperConfig( )
    {
        if( mixinnames.size( ) > 0 )
            return "mixins.ferret.refmap.json";

        return "Ferret-refmap.json"; // default name
    }

    @Override
    public boolean shouldApplyMixin( String targetClassName, String mixinClassName )
    {
        return true;
    }

    @Override
    public void acceptTargets( Set< String > myTargets, Set< String > otherTargets )
    {

    }

    @Override
    public List< String > getMixins( )
    {
        if( mixinnames.size( ) > 0 )
            return mixinnames;

        return null;
    }

    @Override
    public void preApply( String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo )
    {

    }

    @Override
    public void postApply( String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo )
    {

    }
}
