package org.pcollections;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * An immutable and persistent set.
 *
 * @author Yu Kobayashi
 */
public final class OrderedPSet<E> extends AbstractSet<E> implements POrderedSet<E>, Serializable {
    private static final OrderedPSet<Object> EMPTY = new OrderedPSet<Object>();
    private static final long serialVersionUID = -5920090981526873189L;

    @SuppressWarnings("unchecked")
    public static <E> OrderedPSet<E> empty() {
        return (OrderedPSet<E>) EMPTY;
    }

    public static <E> OrderedPSet<E> singleton(E e) {
        return OrderedPSet.<E>empty().plus(e);
    }

    @SuppressWarnings("unchecked")
    public static <E> OrderedPSet<E> from(Iterable<? extends E> list) {
        if (list instanceof OrderedPSet)
            return (OrderedPSet<E>) list;
        return OrderedPSet.<E>empty().plusAll(list);
    }

    private final MapPSet<E> contents;
    private final TreePVector<E> order;

    private OrderedPSet() {
        this(MapPSet.<E>empty(), TreePVector.<E>empty());
    }

    private OrderedPSet(MapPSet<E> contents, TreePVector<E> order) {
        this.contents = contents;
        this.order = order;
    }

    @Override
    public Iterator<E> iterator() {
        return order.iterator();
    }

    @Override
    public int size() {
        return contents.size();
    }

    public E get(int index) {
        return order.get(index);
    }

    public int indexOf(Object o) {
        if (!contents.contains(o))
            return -1;
        return order.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        if (!contents.contains(o))
            return -1;
        return order.lastIndexOf(o);
    }

    public OrderedPSet<E> plus(E element) {
        if (contents.contains(element))
            return this;
        return new OrderedPSet<E>(contents.plus(element), order.plus(element));
    }

    @Override
    public OrderedPSet<E> plusAll(Collection<? extends E> list) {
        return plusAll((Iterable<? extends E>) list);
    }

    public OrderedPSet<E> plusAll(Iterable<? extends E> iterable) {
        OrderedPSet<E> s = this;
        for (E e : iterable) {
            s = s.plus(e);
        }
        return s;
    }

    public OrderedPSet<E> minus(Object element) {
        if (!contents.contains(element))
            return this;
        return new OrderedPSet<E>(contents.minus(element), order.minus(element));
    }

    @Override
    public OrderedPSet<E> minusAll(Collection<?> list) {
        return minusAll((Iterable<? extends E>) list);
    }

    public OrderedPSet<E> minusAll(Iterable<?> iterable) {
        OrderedPSet<E> s = this;
        for (Object e : iterable) {
            s = s.minus(e);
        }
        return s;
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
}
