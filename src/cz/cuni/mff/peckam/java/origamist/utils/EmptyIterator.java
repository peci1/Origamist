/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over an empty collection.
 * 
 * @author Martin Pecka
 */
public final class EmptyIterator<E> implements Iterator<E>
{

    @Override
    public boolean hasNext()
    {
        return false;
    }

    @Override
    public E next()
    {
        return null;
    }

    @Override
    public void remove()
    {
        throw new NoSuchElementException();
    }

}
