package org.pcollections;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * A map-backed immutable and persistent set.
 * <p/>
 * If the backing map is thread-safe, then this implementation is thread-safe
 * (assuming Java's AbstractSet is thread-safe), although its iterators
 * may not be.
 *
 * @author harold
 * @author Yu Kobayashi
 */
public final class MapPSet<E> extends AbstractSet<E> implements PSet<E>, Serializable {
    private static final MapPSet<Object> EMPTY = from(HashPMap.empty());
    private static final long serialVersionUID = -1344047864698590249L;

    /**
     * @return an empty set
     */
    @SuppressWarnings("unchecked")
    public static <E> MapPSet<E> empty() {
        return (MapPSet<E>) EMPTY;
    }

    /**
     * @return empty().plus(e)
     */
    public static <E> MapPSet<E> singleton(E e) {
        return MapPSet.<E>empty().plus(e);
    }

    /**
     * @return empty().plus(list)
     */
    public static <E> MapPSet<E> from(Iterable<? extends E> list) {
        return MapPSet.<E>empty().plusAll(list);
    }

    /**
     * @return a PSet with the elements of map.keySet(), backed by map
     */
    @SuppressWarnings("unchecked")
    public static <E> MapPSet<E> from(PMap<E, ?> map) {
        return new MapPSet<E>((PMap<E, Object>) map);
    }

    /**
     * @return from(map).plus(e)
     */
    public static <E> MapPSet<E> from(PMap<E, ?> map, E e) {
        return from(map).plus(e);
    }

    /**
     * @return from(map).plusAll(list)
     */
    public static <E> MapPSet<E> from(PMap<E, ?> map, Collection<? extends E> list) {
        return from(map).plusAll(list);
    }

    private final PMap<E, Object> map;

    // not instantiable (or subclassable):
    private MapPSet(PMap<E, Object> map) {
        this.map = map;
    }

    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean contains(Object e) {
        return map.containsKey(e);
    }

    private static enum In {
        IN
    }

    public MapPSet<E> plus(E element) {
        return contains(element) ? this : new MapPSet<E>(map.plus(element, In.IN));
    }

    @Override
    public MapPSet<E> plusAll(Collection<? extends E> list) {
        return plusAll((Iterable<? extends E>) list);
    }

    public MapPSet<E> plusAll(Iterable<? extends E> iterable) {
        PMap<E, Object> map = this.map;
        for (E e : iterable)
            map = map.plus(e, In.IN);
        return from(map);
    }

    public MapPSet<E> minus(Object element) {
        return contains(element) ? new MapPSet<E>(map.minus(element)) : this;
    }

    @Override
    public MapPSet<E> minusAll(Collection<?> list) {
        return minusAll((Iterable<?>) list);
    }

    public MapPSet<E> minusAll(Iterable<?> iterable) {
        return from(map.minus(iterable));
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
