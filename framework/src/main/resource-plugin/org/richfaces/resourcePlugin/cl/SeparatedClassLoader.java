package org.richfaces.resourcePlugin.cl;

import java.util.Arrays;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.runners.model.InitializationError;

public class SeparatedClassLoader {

    public static ClassLoader newInstance(JavaArchive... archives) throws InitializationError {
        ClassLoader shrinkWrapClassLoader = getSeparatedClassLoader(archives);
        return shrinkWrapClassLoader;
    }

    private static ClassLoader getSeparatedClassLoader(JavaArchive[] archives) throws InitializationError {
        try {
            ClassLoader bootstrapClassLoader = ClassLoader.getSystemClassLoader().getParent();

            JavaArchive baseArchive = ShrinkWrap.create(JavaArchive.class);
            // ShrinkWrap - JavaArchive
            baseArchive.addClasses(SecurityActions.getAncestors(JavaArchive.class));

            archives = Arrays.copyOf(archives, archives.length + 1);
            archives[archives.length - 1] = baseArchive;

            ShrinkWrapClassLoader shrinkwrapClassLoader = new ShrinkWrapClassLoader(bootstrapClassLoader, archives);
            return shrinkwrapClassLoader;
        } catch (Exception e) {
            throw new InitializationError(e);
        }
    }
}