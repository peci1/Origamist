/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.common;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cz.cuni.mff.peckam.java.origamist.utils.ExtensionClassLoader;

/**
 * This bootstrapping applet loads native Java3D libraries without the need of installing Java3D to the client machine.
 * 
 * Then it loads the contained applet using its own classloader, which is aware of the Java3D native libraries.
 * 
 * @author Martin Pecka
 */
public abstract class Java3DBootstrappingApplet extends JApplet
{

    /** */
    private static final long   serialVersionUID      = 8579150219023896739L;
    /** The bootstrapped applet. */
    private JApplet             appletApplication;

    /** Set this to true if the applet is being loaded as a standard java application from the command line. */
    protected boolean           launchedAsApplication = false;

    /** If this applet is loaded as a standard Java application from the command line, this array holds its arguments. */
    protected String[]          args                  = null;

    /** The map of parameters derived from application arguments, if launched as application. */
    private Map<String, String> parameters            = null;

    /** The window this applet is displayed in, if launched as application. */
    protected JFrame            window                = null;

    {
        System.setSecurityManager(null);
    }

    @Override
    public void init()
    {
        if (!isJava5OrSuperior()) {
            showError("<html><p>This applet may be run under Windows, Mac OS X 10.4+, Linux and Solaris. "
                    + "<br>On other systems, Java3D must be installed from https://java3d.dev.java.net/binary-builds.html. "
                    + "<br>It requires Java version 5 or superior.</p>");
        } else {
            if (launchedAsApplication) {
                parameters = new HashMap<String, String>();
                if (args != null) {
                    for (String arg : args) {
                        if (arg.contains("=")) {
                            String[] tokens = arg.split("=", 2);
                            parameters.put(tokens[0], tokens[1]);
                        }
                    }
                } else {
                    System.err
                            .println("The bootstrapper has been launched as a standard application, but the arguments"
                                    + "array (this.args) isn't set. Assuming no parameters were passed.");
                }
            }
            createAppletApplication();
            this.appletApplication.init();
        }
    }

    @Override
    public void start()
    {
        super.start();
        this.appletApplication.start();
    }

    @Override
    public void stop()
    {
        super.stop();
        this.appletApplication.stop();
    }

    @Override
    public void destroy()
    {
        if (this.appletApplication != null) {
            try {
                Method destroyMethod = this.appletApplication.getClass().getMethod("destroy", new Class[0]);
                destroyMethod.invoke(this.appletApplication, new Object[0]);
            } catch (Exception ex) {
                // Can't do better than print stack trace when applet is destroyed
                ex.printStackTrace();
            }
        }
        this.appletApplication = null;
        // Collect deleted objects (seems to be required under Mac OS X when the applet is being reloaded)
        System.gc();
    }

    /**
     * Returns <code>true</code> if current JVM version is 5+.
     */
    protected boolean isJava5OrSuperior()
    {
        String javaVersion = System.getProperty("java.version");
        String[] javaVersionParts = javaVersion.split("\\.|_");
        if (javaVersionParts.length >= 1) {
            try {
                // Return true for Java SE 5 and superior
                if (Integer.parseInt(javaVersionParts[1]) >= 5) {
                    return true;
                }
            } catch (NumberFormatException ex) {}
        }
        return false;
    }

    @Override
    public URL getDocumentBase()
    {
        if (!launchedAsApplication) {
            return super.getDocumentBase();
        } else {
            try {
                return new File("").getAbsoluteFile().toURI().toURL();
            } catch (MalformedURLException e) {
                try {
                    return new URL(System.getProperty("user.dir"));
                } catch (MalformedURLException e1) {
                    return null;
                }
            }
        }
    }

    @Override
    public URL getCodeBase()
    {
        if (!launchedAsApplication) {
            return super.getCodeBase();
        } else {
            URL location = getClass().getProtectionDomain().getCodeSource().getLocation();

            // if the URL ends with a filename, remove it, so that code base is a directory
            try {
                location = new URL(location, ".");
            } catch (MalformedURLException e) {}

            return location;
        }
    }

    @Override
    public String getParameter(String name)
    {
        if (!launchedAsApplication) {
            return super.getParameter(name);
        } else {
            if (parameters == null)
                return null;
            return parameters.get(name);
        }
    }

    /**
     * A utility method that launches the given applet if the program has been started as Java application.
     * 
     * @param applet The applet to launch.
     * @param frameTitle Title of the window containing the applet.
     * @param args Command-line arguments used when starting the application. If arguments width=xxx and height=yyy are
     *            set, they are used as the default size of the frame.
     */
    public static void main(final Java3DBootstrappingApplet applet, String frameTitle, String[] args)
    {
        applet.launchedAsApplication = true;
        applet.args = args;

        JFrame window = new JFrame(frameTitle);
        applet.window = window;
        window.setContentPane(applet);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                applet.stop();
                applet.destroy();
            }
        });

        window.pack();

        applet.init();
        applet.start();

        if (applet.getParameter("width") != null && applet.getParameter("height") != null) {
            try {
                int width = Integer.parseInt(applet.getParameter("width"));
                int height = Integer.parseInt(applet.getParameter("height"));

                window.setSize(width, height);
            } catch (NumberFormatException e) {

            }
        }

        window.setVisible(true);
    }

    /**
     * Shows the given text in a label.
     */
    protected void showError(String text)
    {
        JLabel label = new JLabel(text, JLabel.CENTER);
        setContentPane(label);
    }

    /**
     * Creates a new <code>AppletApplication</code> instance that manages this applet content.
     */
    protected void createAppletApplication()
    {
        try {
            String os = System.getProperty("os.name");
            if (os == null)
                os = "Windows";

            String systemBits = System.getProperty("sun.arch.data.model");
            if (!"32".equals(systemBits) && !"64".equals(systemBits))
                systemBits = "32";

            String arch = System.getProperty("os.arch");
            if (arch == null)
                arch = "x86";

            // we must load the JAR libraries with this classloader too, because they load the native libraries
            // using the classloader they were loaded with
            List<String> java3DFiles = new LinkedList<String>(Arrays.asList(new String[] { "lib/j3dcore.jar",
                    "lib/vecmath.jar", "lib/j3dutils.jar" }));

            if (os.startsWith("Windows") && systemBits.startsWith("32")) {
                java3DFiles.add("lib/bin/windows-i586/j3dcore-d3d.dll");
                java3DFiles.add("lib/bin/windows-i586/j3dcore-ogl.dll");
                java3DFiles.add("lib/bin/windows-i586/j3dcore-ogl-cg.dll");
                java3DFiles.add("lib/bin/windows-i586/j3dcore-ogl-chk.dll");
            } else if (os.startsWith("Windows") && systemBits.startsWith("64")) {
                java3DFiles.add("lib/bin/windows-amd64/j3dcore-ogl.dll");
            } else if (os.startsWith("Linux") && systemBits.startsWith("32")) {
                java3DFiles.add("lib/bin/linux-i586/libj3dcore-ogl.so");
                java3DFiles.add("lib/bin/linux-i586/libj3dcore-ogl-cg.so");
            } else if (os.startsWith("Linux") && systemBits.startsWith("64")) {
                java3DFiles.add("lib/bin/linux-amd64/libj3dcore-ogl.so");
            } else if (os.startsWith("Linux") && systemBits.startsWith("32")) {
                java3DFiles.add("lib/bin/linux-i586/libj3dcore-ogl.so");
            } else if (os.startsWith("SunOS") && arch.startsWith("sparcv9")) {
                java3DFiles.add("lib/bin/solaris-sparc-v9/libj3dcore-ogl.so");
            } else if (os.startsWith("SunOS") && arch.startsWith("sparc")) {
                java3DFiles.add("lib/bin/solaris-sparc/libj3dcore-ogl.so");
            } else if (os.startsWith("SunOS") && (arch.startsWith("x86_64") || arch.startsWith("amd64"))) {
                java3DFiles.add("lib/bin/solaris-x86-amd64/libj3dcore-ogl.so");
            } else if (os.startsWith("SunOS") && arch.startsWith("x86")) {
                java3DFiles.add("lib/bin/solaris-x86/libj3dcore-ogl.so");
            } else if (os.startsWith("Max OS X") && arch.startsWith("ppc")) {
                java3DFiles.add("lib/bin/macosx-ppc/libjogl.jnilib");
                java3DFiles.add("lib/bin/macosx-ppc/libjogl_awt.jnilib");
                java3DFiles.add("lib/bin/macosx-ppc/libjogl_cg.jnilib");
                java3DFiles.add("lib/bin/macosx-ppc/libgluegen-rt.jnilib");
                java3DFiles.add("lib/gluegen-rt.jar");
            } else if (os.startsWith("Max OS X")) {
                java3DFiles.add("lib/bin/macosx-universal/libjogl.jnilib");
                java3DFiles.add("lib/bin/macosx-universal/libjogl_awt.jnilib");
                java3DFiles.add("lib/bin/macosx-universal/libjogl_cg.jnilib");
                java3DFiles.add("lib/bin/macosx-ppc/libgluegen-rt.jnilib");
                java3DFiles.add("lib/gluegen-rt.jar");
            } else {
                // fallback to 32bit windows
                java3DFiles.add("lib/bin/windows-i586/j3dcore-d3d.dll");
                java3DFiles.add("lib/bin/windows-i586/j3dcore-ogl.dll");
                java3DFiles.add("lib/bin/windows-i586/j3dcore-ogl-cg.dll");
                java3DFiles.add("lib/bin/windows-i586/j3dcore-ogl-chk.dll");
                JOptionPane.showMessageDialog(rootPane,
                        "<html>Your system was not recognized by the origami application. You may either "
                                + "run the <br>application on a supported system (Windows, Mac OS X 10.4+, Linux and "
                                + "Solaris both in 32 and 64 versions) or <br>you must install Java3D manually for "
                                + "your system. You may find the appropriate download on "
                                + "<br>https://java3d.dev.java.net/binary-builds.html</html>");
            }

            List<String> applicationPackages = new LinkedList<String>(Arrays.asList(new String[] { "javax.media",
                    "javax.vecmath", "com.sun.j3d", "com.sun.opengl", "com.sun.gluegen.runtime", "javax.media.opengl",
                    "com.sun.media", "com.ibm.media", "jmpapps.util", "com.microcrowd.loader.java3d", "org.sunflow" }));
            applicationPackages.addAll(Arrays.asList(getApplicationRootPackages()));

            ClassLoader extensionsClassLoader = new ExtensionClassLoader(this.getClass().getClassLoader(), this
                    .getClass().getProtectionDomain(), java3DFiles, applicationPackages, getCodeBase());

            // Call application constructor with reflection
            Class<?> applicationClass = extensionsClassLoader.loadClass(getApplicationClassName());
            Constructor<?> applicationConstructor = applicationClass.getConstructor(new Class[] { JApplet.class });
            this.appletApplication = (JApplet) applicationConstructor.newInstance(new Object[] { this });

        } catch (Throwable ex) {
            if (ex instanceof InvocationTargetException) {
                ex = ((InvocationTargetException) ex).getCause();
            }
            if (ex instanceof AccessControlException) {
                showError("<html>If you want to run this applet, you must relaunch your browser,"
                        + "<br>display this page again and accept the displayed digital signature.");
            } else {
                showError("<html>Can't start applet:<br>Exception" + ex.getClass().getName() + " " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    /**
     * @return The window the application runs in, if launched as application.
     */
    public JFrame getWindow()
    {
        return window;
    }

    /**
     * Returns the name of the application class associated to this applet.
     * This class must have a constructor taking in parameter a <code>JApplet</code>.
     */
    protected abstract String getApplicationClassName();

    /**
     * Returns the roots of the packages the bootstrapped application consists of.
     * 
     * Eg. if your app has packages <code>my.app, my.app.gui, my.app.tools, my.app.tools.useless, javax.my, 
     * javax.my.tools</code>, this should return <code>{"my.app", "javax.my"}</code>
     * 
     * @return The roots of the packages the bootstrapped application consists of.
     */
    protected abstract String[] getApplicationRootPackages();
}
