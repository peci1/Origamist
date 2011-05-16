/**
 * 
 */
package cz.cuni.mff.peckam.java.origamist.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A linked list that can be alternatively viewed as a list of another type elements.
 * 
 * Due to Java generics limitations, this list cannot perform type checking for both types, so most
 * {@link ClassCastException}s will occur on get operations.
 * 
 * @author Martin Pecka
 * 
 * @param E The primary type of list elements.
 * @param I The secondary type of list elements.
 */
public class LinkedListWithAdditionalBounds<E, I> extends LinkedList<E> implements ListWithAdditionalBounds<E, I>
{

    /** The alternative view of this list. */
    private transient List<I> altView          = null;

    /** */
    private static final long serialVersionUID = 211660684866191895L;

    @Override
    public List<I> getAltView()
    {
        if (altView == null) {
            altView = createAltView();
        }
        return altView;
    }

    /**
     * @return The new alternative view of this list.
     */
    protected List<I> createAltView()
    {
        return new List<I>() {

            @Override
            public int size()
            {
                return LinkedListWithAdditionalBounds.this.size();
            }

            @Override
            public boolean isEmpty()
            {
                return LinkedListWithAdditionalBounds.this.isEmpty();
            }

            @Override
            public boolean contains(Object o)
            {
                return LinkedListWithAdditionalBounds.this.contains(o);
            }

            @Override
            public Iterator<I> iterator()
            {
                final Iterator<E> it = LinkedListWithAdditionalBounds.this.iterator();
                return new Iterator<I>() {
                    @Override
                    public boolean hasNext()
                    {
                        return it.hasNext();
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public I next()
                    {
                        return (I) it.next();
                    }

                    @Override
                    public void remove()
                    {
                        it.remove();
                    }

                };
            }

            @Override
            public Object[] toArray()
            {
                return LinkedListWithAdditionalBounds.this.toArray();
            }

            @Override
            public <T> T[] toArray(T[] a)
            {
                return LinkedListWithAdditionalBounds.this.toArray(a);
            }

            @SuppressWarnings("unchecked")
            @Override
            public boolean add(I e)
            {
                return LinkedListWithAdditionalBounds.this.add((E) e);
            }

            @Override
            public boolean remove(Object o)
            {
                return LinkedListWithAdditionalBounds.this.remove(o);
            }

            @Override
            public boolean containsAll(Collection<?> c)
            {
                return LinkedListWithAdditionalBounds.this.containsAll(c);
            }

            @Override
            public boolean addAll(Collection<? extends I> c)
            {
                int oldSize = size();
                for (I i : c)
                    add(i);
                return size() != oldSize;
            }

            @SuppressWarnings("unchecked")
            @Override
            public boolean addAll(int index, Collection<? extends I> c)
            {
                if (c.size() == 0)
                    return false;
                int i = index;
                for (Iterator<? extends I> it = c.iterator(); it.hasNext();) {
                    E e = (E) it.next();
                    LinkedListWithAdditionalBounds.this.add(i, e);
                    i++;
                }
                return true;
            }

            @Override
            public boolean removeAll(Collection<?> c)
            {
                return LinkedListWithAdditionalBounds.this.removeAll(c);
            }

            @Override
            public boolean retainAll(Collection<?> c)
            {
                return LinkedListWithAdditionalBounds.this.retainAll(c);
            }

            @Override
            public void clear()
            {
                LinkedListWithAdditionalBounds.this.clear();
            }

            @SuppressWarnings("unchecked")
            @Override
            public I get(int index)
            {
                return (I) LinkedListWithAdditionalBounds.this.get(index);
            }

            @SuppressWarnings("unchecked")
            @Override
            public I set(int index, I element)
            {
                return (I) LinkedListWithAdditionalBounds.this.set(index, (E) element);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void add(int index, I element)
            {
                LinkedListWithAdditionalBounds.this.add(index, (E) element);
            }

            @SuppressWarnings("unchecked")
            @Override
            public I remove(int index)
            {
                return (I) LinkedListWithAdditionalBounds.this.remove(index);
            }

            @Override
            public int indexOf(Object o)
            {
                return LinkedListWithAdditionalBounds.this.indexOf(o);
            }

            @Override
            public int lastIndexOf(Object o)
            {
                return LinkedListWithAdditionalBounds.this.lastIndexOf(o);
            }

            @Override
            public ListIterator<I> listIterator()
            {
                final ListIterator<E> listIt = LinkedListWithAdditionalBounds.this.listIterator();
                return new ListIterator<I>() {

                    @Override
                    public boolean hasNext()
                    {
                        return listIt.hasNext();
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public I next()
                    {
                        return (I) listIt.next();
                    }

                    @Override
                    public boolean hasPrevious()
                    {
                        return listIt.hasPrevious();
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public I previous()
                    {
                        return (I) listIt.previous();
                    }

                    @Override
                    public int nextIndex()
                    {
                        return listIt.nextIndex();
                    }

                    @Override
                    public int previousIndex()
                    {
                        return listIt.previousIndex();
                    }

                    @Override
                    public void remove()
                    {
                        listIt.remove();
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void set(I e)
                    {
                        listIt.set((E) e);
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void add(I e)
                    {
                        listIt.add((E) e);
                    }

                };
            }

            @Override
            public ListIterator<I> listIterator(int index)
            {
                ListIterator<I> it = listIterator();
                for (int i = 0; i < index; i++)
                    it.next();
                return it;
            }

            /**
             * Not supported.
             */
            @Override
            public List<I> subList(int fromIndex, int toIndex)
            {
                throw new UnsupportedOperationException(); // TODO implement
            }

        };
    }

}
