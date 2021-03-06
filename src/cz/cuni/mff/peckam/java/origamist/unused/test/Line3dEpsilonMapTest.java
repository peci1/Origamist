/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.unused.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.vecmath.Point3d;

import org.junit.Test;

import cz.cuni.mff.peckam.java.origamist.math.Line3d;
import cz.cuni.mff.peckam.java.origamist.math.Vector;
import cz.cuni.mff.peckam.java.origamist.unused.math.CanonicLine3d;
import cz.cuni.mff.peckam.java.origamist.unused.utils.EpsilonMap;
import cz.cuni.mff.peckam.java.origamist.unused.utils.Line3dEpsilonMap;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class Line3dEpsilonMapTest
{
    /**
     * Test method for {@link cz.cuni.mff.peckam.java.origamist.utils.Line3dEpsilonMap}.
     */
    @Test
    public void testTree()
    {
        EpsilonMap<Vector<Double>, Integer> map = new Line3dEpsilonMap<Integer>();

        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)), 100);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(-1, -1, -1)), 99);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0.5d, 0.5d, 0.5d)), 98);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)), 100);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)), 100);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)), 100);
        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(0, 0, 3)), 99);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 100 });

        map.epsilonPut(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)), 101);
        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 101, 100 });

        map.epsilonRemove(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 0, 1)));
        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 101, 100 });

        map.epsilonRemove(new Line3d(new Point3d(0, 0, 0), new Point3d(1, 0, 0)));
        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new CanonicLine3d[] {
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 0, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(0, 1, 0)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 1)),
                new CanonicLine3d(new Point3d(0, 0, 0), new Point3d(1, 1, 0)) });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 99, 100, 98, 101 });
    }

    protected void assertArraysEqual(String message, Object[] a1, Object[] a2)
    {
        assertTrue(message + " a1: " + Arrays.toString(a1) + "; a2: " + Arrays.toString(a2), Arrays.deepEquals(a1, a2));
    }

    public static void main(String[] args)
    {
        new Line3dEpsilonMapTest().testTree();

        System.out.println("Success.");
    }
}
