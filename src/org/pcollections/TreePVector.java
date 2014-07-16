package org.pcollections;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;

/**
 * An immutable and persistent list. Elements can be null.
 * <p/>
 * This implementation is backed by an ImmutableIntTreeMap and
 * supports logarithmic-time querying, setting, insertion,
 * and removal.
 * <p/>
 * This implementation is thread-safe (assuming Java's AbstractList is thread-safe)
 * although its iterators may not be.
 *
 * @author harold
 * @author Yu Kobayashi
 */
public final class TreePVector<E> extends AbstractList<E> implements PVector<E>, Serializable {
    private static final TreePVector<Object> EMPTY = new TreePVector<Object>();
    private static final long serialVersionUID = -6788530879100442978L;

    /**
     * @return an empty list
     */
    @SuppressWarnings("unchecked")
    public static <E> TreePVector<E> empty() {
        return (TreePVector<E>) EMPTY;
    }

    /**
     * @return empty().plus(e)
     */
    public static <E> TreePVector<E> singleton(E e) {
        return TreePVector.<E>empty().plus(e);
    }

    /**
     * @return empty().plus(list)
     */
    @SuppressWarnings("unchecked")
    public static <E> TreePVector<E> from(Iterable<? extends E> list) {
        if (list instanceof TreePVector)
            return (TreePVector<E>) list; // (actually we only know it's ImmutableTreeList<? extends E>)

        // but that's good enough for an immutable
        // (i.e. we can't mess someone else up by adding the wrong type to it)
        return TreePVector.<E>empty().plusAll(list);
    }

    private final IntTreePMap<E> map;

    private TreePVector() {
        this(IntTreePMap.<E>empty());
    }

    private TreePVector(IntTreePMap<E> map) {
        this.map = map;
    }

    public int size() {
        return map.size();
    }

    public E get(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException();

        return map.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return map.values().iterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ListIterator<E> listIterator(int index) {
        return new ArrayListIterator<E>((E[]) toArray(), index);
    }

    public TreePVector<E> subList(int start, int end) {
        int size = size();
        if (start < 0 || end > size || start > end)
            throw new IndexOutOfBoundsException();
        if (start == end)
            return empty();
        if (start == 0) {
            if (end == size)
                return this;
            // remove from end:
            return this.minus(size - 1).subList(start, end);
        }
        // remove from start:
        return this.minus(0).subList(start - 1, end - 1);
    }

    public TreePVector<E> subList(int start) {
        return subList(start, size());
    }

    public TreePVector<E> plus(E element) {
        return new TreePVector<E>(map.plus(size(), element));
    }

    @Override
    public TreePVector<E> plusAll(Collection<? extends E> list) {
        return plusAll((Iterable<? extends E>) list);
    }

    public TreePVector<E> plusAll(Iterable<? extends E> iterable) {
        TreePVector<E> result = this;
        for (E e : iterable) {
            result = result.plus(e);
        }
        return result;
    }

    public TreePVector<E> plus(int index, E element) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException();
        return new TreePVector<E>(map.withKeysChangedAbove(index, 1).plus(index, element));
    }

    @Override
    public TreePVector<E> plusAll(int i, Collection<? extends E> list) {
        return plusAll(i, (Iterable<? extends E>) list);
    }

    public TreePVector<E> plusAll(int index, Iterable<? extends E> iterable) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException();

        Collection<? extends E> list = Utils.asCollection(iterable);
        if (list.size() == 0)
            return this;

        IntTreePMap<E> map = this.map.withKeysChangedAbove(index, list.size());
        int i = index;
        for (E e : list) {
            map = map.plus(i++, e);
        }
        return new TreePVector<E>(map);
    }

    public TreePVector<E> with(int index, E element) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException();

        IntTreePMap<E> map = this.map.plus(index, element);
        if (map == this.map)
            return this;
        return new TreePVector<E>(map);
    }

    public TreePVector<E> minus(Object element) {
        for (Entry<Integer, E> entry : map.entrySet()) {
            if (objectEquals(entry.getValue(), element)) {
                return minus(entry.getKey());
            }
        }
        return this;
    }

    @Override
    public PVector<E> minusAll(Collection<?> list) {
        return null;
    }

    public TreePVector<E> minusAll(Iterable<?> iterable) {
        TreePVector<E> result = this;
        for (Object e : iterable) {
            result = result.minus(e);
        }
        return result;
    }

    public TreePVector<E> minus(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException();

        return new TreePVector<E>(map.minus(index).withKeysChangedAbove(index, -1));
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
