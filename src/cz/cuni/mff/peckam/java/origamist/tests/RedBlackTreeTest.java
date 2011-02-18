/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import cz.cuni.mff.peckam.java.origamist.utils.RedBlackTree;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class RedBlackTreeTest
{

    protected Map<?, ?> testMap, refMap = null;

    /**
     * Test method for {@link java.util.RedBlackTree}
     */
    @Test
    public void testTree()
    {
        Integer[] test1 = new Integer[] { 6, 2, 8, 5, 3, 9, 0, -1, 7, -3, 00000, 88888, 3333, -2222 };
        doTest(test1, test1, null);

        String[] test2 = new String[] { "a", "b", "c", "d", "e", "g", "f", "a", "f", "g", "g", "r", "s", "ttt" };
        doTest(test1, test2, null);
        doTest(test2, test1, null);
        doTest(test2, test2, null);

        Comparator<Integer> intDescCmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                return -o1.compareTo(o2);
            }
        };

        Comparator<String> strDescCmp = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2)
            {
                return -o1.compareTo(o2);
            }
        };

        doTest(test1, test1, intDescCmp);
        doTest(test1, test2, intDescCmp);
        doTest(test2, test1, strDescCmp);
        doTest(test2, test2, strDescCmp);

        List<SortedMap<Integer, String>> maps = loadMaps(test1, test2, null);
        SortedMap<Integer, String> testMap = maps.get(0);
        SortedMap<Integer, String> refMap = maps.get(1);

        assertTrue("Tree has wrong size", testMap.size() == refMap.size());
        assertTrue("Newly created empty tree has non-zero size.", new RedBlackTree<Integer, String>().size() == 0);

        Integer[] test3 = new Integer[] { -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, -10, -11 };
        for (int i : test3)
            assertTrue("Tree doesn't contain the same keys as the reference map.",
                    testMap.containsKey(i) == refMap.containsKey(i));

        try {
            testMap.put(null, "a");
            assert false : "Tree allowed to insert a null key with the default comparator.";
        } catch (NullPointerException e) {}

        assertTrue("Submaps aren't equal.", testMap.subMap(0, 6).keySet().equals(refMap.subMap(0, 6).keySet()));
        assertTrue("Submaps aren't equal.", testMap.subMap(0, 6).entrySet().equals(refMap.subMap(0, 6).entrySet()));
        assertTrue("Submaps aren't equal.", testMap.subMap(0, 0).keySet().equals(refMap.subMap(0, 0).keySet()));
        assertTrue("Submaps aren't equal.", testMap.headMap(6).keySet().equals(refMap.headMap(6).keySet()));
        assertTrue("Submaps aren't equal.", testMap.tailMap(6).keySet().equals(refMap.tailMap(6).keySet()));

        assertTrue("Linear-time building algorithm has errors.",
                testMap.equals(new RedBlackTree<Integer, String>(testMap)));

        maps = loadMaps(test1, test2, intDescCmp);
        testMap = maps.get(0);
        refMap = maps.get(1);

        assertTrue("Tree has wrong size", testMap.size() == refMap.size());
        assertTrue("Newly created empty tree has non-zero size.", new RedBlackTree<Integer, String>().size() == 0);

        for (int i : test3)
            assertTrue("Tree doesn't contain the same keys as the reference map.",
                    testMap.containsKey(i) == refMap.containsKey(i));

        try {
            testMap.put(null, "a");
            assert false : "Tree allowed to insert a null key with a comparator not allowing null keys.";
        } catch (NullPointerException e) {}

        assertTrue("Submaps aren't equal.", testMap.subMap(6, 0).keySet().equals(refMap.subMap(6, 0).keySet()));
        assertTrue("Submaps aren't equal.", testMap.subMap(6, 0).entrySet().equals(refMap.subMap(6, 0).entrySet()));
        assertTrue("Submaps aren't equal.", testMap.subMap(0, 0).keySet().equals(refMap.subMap(0, 0).keySet()));
        assertTrue("Submaps aren't equal.", testMap.headMap(6).keySet().equals(refMap.headMap(6).keySet()));
        assertTrue("Submaps aren't equal.", testMap.tailMap(6).keySet().equals(refMap.tailMap(6).keySet()));

        assertTrue("Linear-time building algorithm has errors.",
                testMap.equals(new RedBlackTree<Integer, String>(testMap)));

        testMap = new RedBlackTree<Integer, String>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                if (o1 == null) {
                    if (o2 == null)
                        return 0;
                    else
                        return -1;
                }
                if (o2 == null)
                    return 1;

                return o1.compareTo(o2);
            }
        });

        try {
            testMap.put(null, "a");
        } catch (NullPointerException e) {
            assert false : "Tree didn't allow to insert a null key with a comparator allowing null keys.";
        }

    }

    protected <K, V> void doTest(K[] keys, V[] values, Comparator<? super K> comp)
    {
        RedBlackTree<K, V> t1 = new RedBlackTree<K, V>();
        if (comp != null)
            t1 = new RedBlackTree<K, V>(comp);
        TreeMap<K, V> refTree = new TreeMap<K, V>();
        if (comp != null)
            refTree = new TreeMap<K, V>(comp);

        for (int i = 0; i < keys.length; i++) {
            t1.put(keys[i], values[i]);
            refTree.put(keys[i], values[i]);
        }

        Object[] entries = t1.entrySet().toArray();
        Object[] refEntries = refTree.entrySet().toArray();
        assertTrue("Wrong ordering of inserted values. Got " + Arrays.deepToString(entries) + ", but expected "
                + Arrays.deepToString(refEntries), Arrays.equals(entries, refEntries));
    }

    protected <K, V> List<SortedMap<K, V>> loadMaps(K[] keys, V[] values, Comparator<? super K> comp)
    {
        RedBlackTree<K, V> t1 = new RedBlackTree<K, V>();
        if (comp != null)
            t1 = new RedBlackTree<K, V>(comp);
        TreeMap<K, V> refTree = new TreeMap<K, V>();
        if (comp != null)
            refTree = new TreeMap<K, V>(comp);

        for (int i = 0; i < keys.length; i++) {
            t1.put(keys[i], values[i]);
            refTree.put(keys[i], values[i]);
        }

        List<SortedMap<K, V>> res = new ArrayList<SortedMap<K, V>>(2);
        res.add(t1);
        res.add(refTree);
        return res;
    }

    protected <T> boolean setsEqual(Set<T> s1, Set<T> s2)
    {
        if (s1.size() != s2.size())
            return false;

        Iterator<T> it1 = s1.iterator();
        Iterator<T> it2 = s2.iterator();

        while (it1.hasNext()) {
            if (!it1.next().equals(it2.next()))
                return false;
        }
        return true;
    }

    public static void main(String[] args)
    {
        new RedBlackTreeTest().testTree();
    }
}
