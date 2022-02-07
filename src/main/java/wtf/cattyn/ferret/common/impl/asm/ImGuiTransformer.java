package wtf.cattyn.ferret.common.impl.asm;

import imgui.ImGui;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.lang.reflect.Field;

public class ImGuiTransformer {

    {

        try {
            ClassReader classReader = new ClassReader(ImGui.class.getName());
            ClassWriter writer = new ClassWriter(classReader, 0);
            MethodVisitor visitor = writer.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "endExternal", "()V", null, null);
            visitor.visitMethodInsn(Opcodes.INVOKESTATIC, "imgui/ImGui", "end", "()V", false);
            visitor.visitInsn(Opcodes.RETURN);
            visitor.visitMaxs(0, 0);
            visitor.visitEnd();
            classReader.accept(writer, 0);
            GenericClassLoader classLoader = new GenericClassLoader();
            FileUtils.writeByteArrayToFile(new File("D://shit.class"), writer.toByteArray());
            classLoader.defineClass(ImGui.class.getName(), writer.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class GenericClassLoader extends ClassLoader {

        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }

    }

}
