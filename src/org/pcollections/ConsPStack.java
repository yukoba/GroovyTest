package org.pcollections;

import java.io.Serializable;
import java.util.*;

/**
 * A simple immutable and persistent stack. Elements can be null.
 * <p/>
 * This implementation is thread-safe (assuming Java's AbstractSequentialList is thread-safe),
 * although its iterators may not be.
 *
 * @author harold
 * @author Yu Kobayashi
 */
public final class ConsPStack<E> extends AbstractSequentialList<E> implements PStack<E>, Serializable {
    private static final ConsPStack<Object> EMPTY = new ConsPStack<Object>();
    private static final long serialVersionUID = -7306791346408620911L;

    /**
     * @return an empty stack
     */
    @SuppressWarnings("unchecked")
    public static <E> ConsPStack<E> empty() {
        return (ConsPStack<E>) EMPTY;
    }

    /**
     * @return empty().plus(e)
     */
    public static <E> ConsPStack<E> singleton(E e) {
        return ConsPStack.<E>empty().plus(e);
    }

    /**
     * @return a stack consisting of the elements of list in the order of list.iterator()
     */
    @SuppressWarnings("unchecked")
    public static <E> ConsPStack<E> from(Iterable<? extends E> list) {
        // but that's good enough for an immutable
        // (i.e. we can't mess someone else up by adding the wrong type to it)
        return from(list.iterator());
    }

    private static <E> ConsPStack<E> from(Iterator<? extends E> i) {
        if (!i.hasNext()) return empty();
        E e = i.next();
        return from(i).plus(e);
    }

    private final E first;
    private final ConsPStack<E> rest;
    private final int size;

    // not externally instantiable (or subclassable):
    private ConsPStack() { // EMPTY constructor
        if (EMPTY != null)
            throw new RuntimeException("empty constructor should only be used once");
        size = 0;
        first = null;
        rest = null;
    }

    private ConsPStack(E first, ConsPStack<E> rest) {
        this.first = first;
        this.rest = rest;

        size = 1 + rest.size;
    }

    public int size() {
        return size;
    }

    public E get(int i) {
        if (i < 0 || i >= size)
            throw new IndexOutOfBoundsException();

        return subList(i).first;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int idx = 0;
            ConsPStack<E> next = ConsPStack.this;

            public boolean hasNext() {
                return idx < size;
            }

            public E next() {
                if (idx >= size)
                    throw new NoSuchElementException();
                idx++;

                E e = next.first;
                next = next.rest;
                return e;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public ListIterator<E> listIterator(int index) {
        return new ArrayListIterator<E>((E[]) toArray(), index);
    }

    public ConsPStack<E> subList(int start, int end) {
        if (start < 0 || end > size || start > end)
            throw new IndexOutOfBoundsException();
        if (end == size) // want a substack
            return subList(start); // this is faster
        if (start == end) // want nothing
            return empty();

        if (start == 0) // want the current element
            return new ConsPStack<E>(first, rest.subList(0, end - 1));

        // otherwise, don't want the current element:
        return rest.subList(start - 1, end - 1);
    }

    public ConsPStack<E> subList(int start) {
        if (start < 0 || start > size)
            throw new IndexOutOfBoundsException();

        return start == 0 ? this : rest.subList(start - 1);
    }

    public ConsPStack<E> plus(E element) {
        return new ConsPStack<E>(element, this);
    }

    @Override
    public ConsPStack<E> plusAll(Collection<? extends E> list) {
        return plusAll((Iterable<? extends E>) list);
    }

    public ConsPStack<E> plusAll(Iterable<? extends E> iterable) {
        ConsPStack<E> result = this;
        for (E e : iterable) {
            result = result.plus(e);
        }
        return result;
    }

    public ConsPStack<E> plus(int index, E element) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        if (index == 0) // insert at beginning
            return plus(element);

        return new ConsPStack<E>(first, rest.plus(index - 1, element));
    }

    public ConsPStack<E> plusAll(int i, Collection<? extends E> list) {
        return plusAll(i, (Iterable<? extends E>) list);
    }

    public ConsPStack<E> plusAll(int index, Iterable<? extends E> iterable) {
        // TODO inefficient if iterable is empty
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException();

        if (index == 0)
            return plusAll(iterable);

        return new ConsPStack<E>(first, rest.plusAll(index - 1, iterable));
    }

    public ConsPStack<E> with(int index, E element) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

        if (index == 0)
            return objectEquals(first, element) ? this : new ConsPStack<E>(element, rest);

        ConsPStack<E> newRest = rest.with(index - 1, element);
        return newRest == rest ? this : new ConsPStack<E>(first, newRest);
    }

    public ConsPStack<E> minus(int index) {
        return minus(get(index));
    }

    public ConsPStack<E> minus(Object element) {
        if (size == 0)
            return this;

        if (objectEquals(first, element)) // found it
            return rest; // don't recurse (only remove one)

        // otherwise keep looking:
        ConsPStack<E> newRest = rest.minus(element);
        return newRest == rest ? this : new ConsPStack<E>(first, newRest);
    }

    public ConsPStack<E> minusAll(Collection<?> list) {
        return minusAll((Iterable<?>) list);
    }

    public ConsPStack<E> minusAll(Iterable<?> iterable) {
        if (size == 0)
            return this;

        Collection<?> list = Utils.asCollection(iterable);
        if (list.contains(first)) // get rid of current element
            return rest.minusAll(list); // recursively delete all

        // either way keep looking:
        ConsPStack<E> newRest = rest.minusAll(list);
        return newRest == rest ? this : new ConsPStack<E>(first, newRest);
    }

    private static boolean objectEquals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }

    @Override
    @Deprecated
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void add(int index, E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }
}
