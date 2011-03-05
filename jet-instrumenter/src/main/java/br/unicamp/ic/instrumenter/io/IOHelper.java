package br.unicamp.ic.instrumenter.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;

import soot.Printer;
import soot.SootClass;
import soot.jimple.JasminClass;
import soot.util.JasminOutputStream;

/**
 * Utilitary class involving IO stuff. This class is responsible mainly for writing down a <code>.class</code> and
 * reading it with a {@link ClassLoader}.
 */
public final class IOHelper {

    /**
     * Utilitary class, private constructor. Please use {{@link #newInstance()}.
     */
    private IOHelper() {
        // private.
    }

    /**
     * Creates a custom {@link ClassLoader} to read a bytecode file <code>.class</code>.
     * 
     * @param instrumentedClasspath
     *            the base classpath to the instrumented class.
     * @param className
     *            class name. i.e.: <code>br.com.unicamp.Example</code>
     * @param originalClasspath
     *            the base {@link URL} for the original classpath where are the classes not instrumented.
     * @return a {@link ClassLoader} that loaded the class file.
     */
    public ClassLoader getCustomClassLoader(final URL instrumentedClasspath, final String className, final URL[] originalClasspath) {
        return new InternalClassLoader(instrumentedClasspath, className, originalClasspath);
    }

    /**
     * Writes out the <code>.class</code> to the classpath. Creates any necessary folders (java packages). This
     * <code>.class</code> file is readable for jvm machine and can be loaded by a {@link ClassLoader}.
     * 
     * @param classpath
     *            base path where the class will be written (after creating the folder - package - structure).
     * @param sClass
     *            the instrumented {@link SootClass}.
     */
    public void writeJavaByteCode(final URL classpath, final SootClass sClass) {
        // creates the absolute path to the file.
        final String filename = createAbsoluePath(classpath, sClass) + ".class";
        OutputStream os = null;
        try {
            // use Soot to write the bytecode into the class file.
            os = new JasminOutputStream(new FileOutputStream(filename));
            final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
            final JasminClass jClass = new JasminClass(sClass);
            jClass.print(pw);
            // closing resources.
            pw.flush();
            pw.close();
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Writes out the <code>.class</code> to the classpath. Creates any necessary folders (java packages). This
     * <code>.class</code> file is readable only for a human eyes an cannot be loaded by jvm through a
     * {@link ClassLoader}.
     * 
     * @param classpath
     *            base path where the class will be written (after creating the folder - package - structure).
     * 
     * @param sClass
     *            the instrumented {@link SootClass}.
     */
    public void writeReadableByteCode(final URL classpath, final SootClass sClass) {
        // creates the absolute path to the file.
        final String filename = createAbsoluePath(classpath, sClass) + ".readable.class";
        OutputStream so = null;
        try {
            // just write down the 'Soot Class' to the file. It is readable to human eyes.
            so = new FileOutputStream(filename);
            final PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(so));
            Printer.v().printTo(sClass, writerOut);
            // closing resources.
            writerOut.flush();
            writerOut.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (so != null) {
                try {
                    so.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates the path using the indicated classpath and the structure (package) of the class.
     * 
     * @param classpath
     *            classpath where the class is.
     * @param sClass
     *            the instrumented class.
     * @return the absolute path to the class file.
     */
    private String createAbsoluePath(final URL classpath, final SootClass sClass) {
        final String pkgName = sClass.getPackageName().replaceAll("\\.", String.valueOf(File.separatorChar));
        final File pkgDir = new File(classpath.getPath() + File.separatorChar + pkgName);
        if (!pkgDir.exists()) {
            // if the directories does not exists, try to create then.
            if (!pkgDir.mkdirs()) {
                // could not create temporary dir.
                throw new RuntimeException("Could not create temporary dir [" + pkgDir.getAbsolutePath() + "]");
            }
        }
        final String filename = pkgDir.getAbsolutePath() + File.separatorChar + sClass.getShortName();
        return filename;
    }

    /**
     * Gets a new fresh instance of {@link IOHelper}.
     * 
     * @return fresh instance of {@link IOHelper}.
     */
    public static IOHelper newInstance() {
        return new IOHelper();
    }

    /**
     * Custom {@link ClassLoader} that loads a <code>.class</code> file from the indicated classpath.
     */
    private static class InternalClassLoader extends URLClassLoader {
        private final URL    classpath;
        private final String className;

        /**
         * Create a new instance of {@link InternalClassLoader}.
         * 
         * @param instrumentedClasspath
         *            the base classpath to the instrumented class.
         * @param className
         *            class name. i.e.: <code>br.com.unicamp.Example</code>
         * @param originalClasspath
         *            the base {@link URL} for the original classpath where are the classes not instrumented.
         */
        public InternalClassLoader(final URL instrumentedClasspath, final String className, final URL[] originalClasspath) {
            super(originalClasspath);
            this.classpath = instrumentedClasspath;
            this.className = className;
        }

        @Override
        public Class<?> loadClass(final String name) throws ClassNotFoundException {
            if (this.className.equals(name)) {
                return findClass(name);
            }
            return super.loadClass(name);
        }

        @Override
        protected Class<?> findClass(final String name) throws ClassNotFoundException {
            if (this.className.equals(name)) {
                final String fileName = name.replace('.', File.separatorChar) + ".class";
                final byte[] classData = loadClassData(this.classpath.getPath() + File.separatorChar + fileName);
                return defineClass(name, classData, 0, classData.length);
            }

            return super.findClass(name);
        }

        /**
         * Loads the file as a {@link InputStream} and transforms in an array of bytes.
         * 
         * @param fileName
         *            file name. i.e.: c:/workspace/project1/br/com/unicamp/Example.class.
         * @return an array of bytes of the content of file.
         * @throws ClassNotFoundException
         *             if the file cannot be read.
         */
        private byte[] loadClassData(final String fileName) throws ClassNotFoundException {
            InputStream inputStream = null;
            byte[] classData = null;
            try {
                final File file = new File(fileName);
                inputStream = new FileInputStream(file);
                final int fileSize = (int) file.length();
                classData = new byte[fileSize];
                inputStream.read(classData, 0, fileSize);
            } catch (final IOException e) {
                throw new ClassNotFoundException("Cannot read class data", e);
            } finally {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    throw new ClassNotFoundException("Cannot read class data", e);
                }
            }
            return classData;
        }

    }

}
