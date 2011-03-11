/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.tests;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import cz.cuni.mff.peckam.java.origamist.utils.EpsilonRedBlackTree;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class EpsilonRedBlackTreeTest
{
    /**
     * Test method for {@link EpsilonRedBlackTree}.
     */
    @Test
    public void testTree()
    {
        EpsilonRedBlackTree<Integer, Integer, Integer> map = new EpsilonRedBlackTree<Integer, Integer, Integer>(5);
        map.epsilonPut(0, 100);
        map.epsilonPut(10, 100);
        map.epsilonPut(20, 100);
        map.epsilonPut(33, 100);
        map.epsilonPut(34, 100);
        map.epsilonPut(2, 100);
        map.epsilonPut(-4, 100);
        map.epsilonPut(7, 100);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new Integer[] { 0, 10, 20, 33 });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 100, 100, 100, 100 });

        map.epsilonPut(22, 99);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new Integer[] { 0, 10, 20, 33 });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 100, 100, 99, 100 });

        map.epsilonRemove(29);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new Integer[] { 0, 10, 20 });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 100, 100, 99 });

        map.epsilonRemove(-6);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new Integer[] { 0, 10, 20 });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 100, 100, 99 });

        map.epsilonRemove(-5);

        assertArraysEqual("Keys don't correspond.", map.keySet().toArray(), new Integer[] { 10, 20 });
        assertArraysEqual("Values don't correspond.", map.values().toArray(), new Integer[] { 100, 99 });
    }

    protected void assertArraysEqual(String message, Object[] a1, Object[] a2)
    {
        assertTrue(message + " a1: " + Arrays.toString(a1) + "; a2: " + Arrays.toString(a2), Arrays.deepEquals(a1, a2));
    }

    public static void main(String[] args)
    {
        new EpsilonRedBlackTreeTest().testTree();

        System.out.println("Success.");
    }
}
