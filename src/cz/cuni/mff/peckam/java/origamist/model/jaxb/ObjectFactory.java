//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.10.26 at 01:06:48 dop. CEST 
//


package cz.cuni.mff.peckam.java.origamist.model.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import cz.cuni.mff.peckam.java.origamist.model.DoubleDimension;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cz.cuni.mff.peckam.java.origamist.model.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Origami_QNAME = new QName("http://www.mff.cuni.cz/~peckam/java/origamist/diagram/", "origami");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cz.cuni.mff.peckam.java.origamist.model.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.UnitDimension createUnitDimension() {
        return new cz.cuni.mff.peckam.java.origamist.model.UnitDimension();
    }

    /**
     * Create an instance of {@link Steps }
     * 
     */
    public Steps createSteps() {
        return new Steps();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.DiagramPaper createDiagramPaper() {
        return new cz.cuni.mff.peckam.java.origamist.model.DiagramPaper();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.ModelPaper createModelPaper() {
        return new cz.cuni.mff.peckam.java.origamist.model.ModelPaper();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Step }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.Step createStep() {
        return new cz.cuni.mff.peckam.java.origamist.model.Step();
    }

    /**
     * Create an instance of {@link Model }
     * 
     */
    public Model createModel() {
        return new Model();
    }

    /**
     * Create an instance of {@link DiagramColors }
     * 
     */
    public DiagramColors createDiagramColors() {
        return new DiagramColors();
    }

    /**
     * Create an instance of {@link Point2D }
     * 
     */
    public Point2D createPoint2D() {
        return new Point2D();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami createOrigami() {
        return new cz.cuni.mff.peckam.java.origamist.model.Origami();
    }

    /**
     * Create an instance of {@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Operation }
     * 
     */
    public cz.cuni.mff.peckam.java.origamist.model.jaxb.Operation createOperation() {
        return new cz.cuni.mff.peckam.java.origamist.model.Operation();
    }

    /**
     * Create an instance of {@link ModelColors }
     * 
     */
    public ModelColors createModelColors() {
        return new ModelColors();
    }

    /**
     * Create an instance of {@link Dimension }
     * 
     */
    public Dimension createDimension() {
        return new DoubleDimension();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.mff.cuni.cz/~peckam/java/origamist/diagram/", name = "origami")
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public JAXBElement<cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami> createOrigami(cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami value) {
        return new JAXBElement<cz.cuni.mff.peckam.java.origamist.model.jaxb.Origami>(_Origami_QNAME, ((Class) cz.cuni.mff.peckam.java.origamist.model.Origami.class), null, ((cz.cuni.mff.peckam.java.origamist.model.Origami) value));
    }

}
