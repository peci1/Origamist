/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.unused.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cz.cuni.mff.peckam.java.origamist.unused.utils.Interval;
import cz.cuni.mff.peckam.java.origamist.unused.utils.IntervalTree;

/**
 * 
 * 
 * @author Martin Pecka
 */
public class IntervalTreeTest
{

    @Test
    public void testTree()
    {

        IntervalTree<Double, String> tree = new IntervalTree<Double, String>();
        tree.put(new TestInterval(0d, 1d), "a");
        tree.put(new TestInterval(1d, 2d), "b");
        tree.put(new TestInterval(2d, 3d), "c");
        tree.put(new TestInterval(3d, 4d), "d");
        tree.put(new TestInterval(4d, 5d), "e");
        tree.put(new TestInterval(5d, 6d), "f");
        tree.put(new TestInterval(6d, 7d), "g");
        tree.put(new TestInterval(7d, 8d), "h");
        tree.put(new TestInterval(8d, 9d), "i");

        String str;
        str = tree.intervalGet(new TestInterval(0.5, 0.5));
        assertTrue("a".equals(str));
        str = tree.intervalGet(new TestInterval(0.75, 0.75));
        assertTrue("a".equals(str));
        str = tree.intervalGet(new TestInterval(1d, 1d));
        assertTrue("a".equals(str) || "b".equals(str));
        str = tree.intervalGet(new TestInterval(-0.5, 0.5));
        assertTrue("a".equals(str));
        str = tree.intervalGet(new TestInterval(-0.5, 0d));
        assertTrue("a".equals(str));
        str = tree.intervalGet(new TestInterval(2.5, 2.5));
        assertTrue("c".equals(str));
        str = tree.intervalGet(new TestInterval(4.999, 4.999));
        assertTrue("e".equals(str));
        str = tree.intervalGet(new TestInterval(9d, 9d));
        assertTrue("i".equals(str));
        str = tree.intervalGet(new TestInterval(0.5, 5.5));
        assertTrue("a".equals(str) || "b".equals(str) || "c".equals(str) || "d".equals(str) || "e".equals(str)
                || "f".equals(str));
        str = tree.intervalGet(new TestInterval(-0.5, -0.5));
        assertTrue(str == null);
        str = tree.intervalGet(new TestInterval(9.5, 9.5));
        assertTrue(str == null);

        tree.remove(new TestInterval(2d, 3d));
        tree.remove(new TestInterval(4d, 5d));
        tree.remove(new TestInterval(5d, 6d));
        tree.remove(new TestInterval(8d, 9d));

        str = tree.intervalGet(new TestInterval(2d, 3d));
        assertTrue("b".equals(str) || "d".equals(str));
        str = tree.intervalGet(new TestInterval(2d, 2d));
        assertTrue("b".equals(str));
        str = tree.intervalGet(new TestInterval(3d, 3d));
        assertTrue("d".equals(str));
        str = tree.intervalGet(new TestInterval(4d, 4d));
        assertTrue("d".equals(str));
        str = tree.intervalGet(new TestInterval(3.5d, 5.5d));
        assertTrue("d".equals(str));
        str = tree.intervalGet(new TestInterval(7.2d, 10d));
        assertTrue("h".equals(str));
        str = tree.intervalGet(new TestInterval(2.5, 2.5));
        assertTrue(str == null);
        str = tree.intervalGet(new TestInterval(5d, 5d));
        assertTrue(str == null);
        str = tree.intervalGet(new TestInterval(4.5, 5.5));
        assertTrue(str == null);
        str = tree.intervalGet(new TestInterval(8.05, 8.05));
        assertTrue(str == null);

    }

    public static void main(String[] args)
    {
        new IntervalTreeTest().testTree();
    }

    class TestInterval implements Interval<Double>
    {
        protected Double min;
        protected Double max;

        /**
         * @param min
         * @param max
         */
        public TestInterval(Double min, Double max)
        {
            this.min = min;
            this.max = max;
        }

        /**
         * @return the min
         */
        @Override
        public Double getMin()
        {
            return min;
        }

        /**
         * @return the max
         */
        @Override
        public Double getMax()
        {
            return max;
        }

        @Override
        public boolean overlapsWith(Interval<Double> other)
        {
            return (other.getMin() >= min && other.getMin() <= max) || (other.getMax() >= min && other.getMax() <= max)
                    || (min >= other.getMin() && min <= other.getMax())
                    || (max >= other.getMin() && max <= other.getMax());
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((max == null) ? 0 : max.hashCode());
            result = prime * result + ((min == null) ? 0 : min.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TestInterval other = (TestInterval) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (max == null) {
                if (other.max != null)
                    return false;
            } else if (!max.equals(other.max))
                return false;
            if (min == null) {
                if (other.min != null)
                    return false;
            } else if (!min.equals(other.min))
                return false;
            return true;
        }

        private IntervalTreeTest getOuterType()
        {
            return IntervalTreeTest.this;
        }

        @Override
        public String toString()
        {
            return "[min=" + min + ", max=" + max + "]";
        }
    }
}
