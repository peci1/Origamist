/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.gui.viewer;

import cz.cuni.mff.peckam.java.origamist.gui.Java3DBootstrappingApplet;

/**
 * This applet just runs OrigamiViewer and adds support for Java3D native libraries.
 * 
 * @author Martin Pecka
 */
public class OrigamiViewerBootstrapper extends Java3DBootstrappingApplet
{

    /** */
    private static final long serialVersionUID = 859355750244428460L;

    @Override
    protected String getApplicationClassName()
    {
        return "cz.cuni.mff.peckam.java.origamist.gui.viewer.OrigamiViewer";
    }

    @Override
    protected String[] getApplicationRootPackages()
    {
        return new String[] { "cz.cuni.mff.peckam.java.origamist", "org.w3c._2001.xmlschema" };
    }

}
