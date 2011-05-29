/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.editor;

import cz.cuni.mff.peckam.java.origamist.gui.common.Java3DBootstrappingApplet;

/**
 * This applet just runs OrigamiEditor and adds support for Java3D native libraries.
 * 
 * @author Martin Pecka
 */
public class OrigamiEditorBootstrapper extends Java3DBootstrappingApplet
{

    /** */
    private static final long serialVersionUID = 859355750244428460L;

    @Override
    protected String getApplicationClassName()
    {
        return "cz.cuni.mff.peckam.java.origamist.gui.editor.OrigamiEditor";
    }

    @Override
    protected String[] getApplicationRootPackages()
    {
        return new String[] { "cz.cuni.mff.peckam.java.origamist", "org.w3c._2001.xmlschema", "javax.swing.origamist" };
    }

    /**
     * Run this applet if the application has been started from the command-line.
     * 
     * @param args The command-line arguments.
     */
    public static void main(String[] args)
    {
        main(new OrigamiEditorBootstrapper(), "Origamist Editor", args);
    }
}
