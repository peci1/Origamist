/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.unused.utils;

import java.util.Comparator;

/**
 * A wrapper for creating a {@link Comparable} object from a value and a comparator that can compare that value to other
 * values of the same type.
 * 
 * @author Martin Pecka
 */
public class ComparableWrapper<T> implements Comparable<ComparableWrapper<T>>
{

    /**
     * @param value The value.
     * @param comparator The comparator.
     */
    public ComparableWrapper(T value, Comparator<? super T> comparator)
    {
        this.value = value;
        this.comparator = comparator;
    }

    /** The value. */
    protected T                     value;
    /** The comparator. */
    protected Comparator<? super T> comparator;

    /**
     * @return the value
     */
    public T getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(T value)
    {
        this.value = value;
    }

    /**
     * @return the comparator
     */
    public Comparator<? super T> getComparator()
    {
        return comparator;
    }

    /**
     * @param comparator the comparator to set
     */
    public void setComparator(Comparator<? super T> comparator)
    {
        this.comparator = comparator;
    }

    @Override
    public int compareTo(ComparableWrapper<T> o)
    {
        return comparator.compare(this.getValue(), (o == null ? null : o.getValue()));
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((comparator == null) ? 0 : comparator.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        @SuppressWarnings("unchecked")
        ComparableWrapper<T> other = (ComparableWrapper<T>) obj;
        if (comparator == null) {
            if (other.comparator != null)
                return false;
        } else if (!comparator.equals(other.comparator))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "ComparableWrapper [" + value + "]";
    }
}
