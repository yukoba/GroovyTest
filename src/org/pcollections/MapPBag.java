package org.pcollections;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A map-backed immutable and persistent bag.
 * <p/>
 * If the backing map is thread-safe, then this implementation is thread-safe
 * (assuming Java's AbstractCollection is thread-safe), although its iterators
 * may not be.
 *
 * @author harold
 * @author Yu Kobayashi
 */
public final class MapPBag<E> extends AbstractCollection<E> implements PBag<E>, Serializable {
    private static final MapPBag<Object> EMPTY = empty(HashPMap.<Object, Integer>empty());
    private static final long serialVersionUID = 7567737490423868674L;

    /**
     * @return an empty bag
     */
    @SuppressWarnings("unchecked")
    public static <E> MapPBag<E> empty() {
        return (MapPBag<E>) EMPTY;
    }

    /**
     * @return empty().plus(e)
     */
    public static <E> MapPBag<E> singleton(E e) {
        return MapPBag.<E>empty().plus(e);
    }

    /**
     * @return empty().plus(list)
     */
    public static <E> MapPBag<E> from(Iterable<? extends E> list) {
        return MapPBag.<E>empty().plusAll(list);
    }

    /**
     * @return a ImmutableMapBag backed by an empty version of map, i.e. by map.minusAll(map.keySet())
     */
    public static <E> MapPBag<E> empty(PMap<E, Integer> map) {
        return new MapPBag<E>(map.minus(map.keySet()), 0);
    }

    private final PMap<E, Integer> map;
    private final int size;

    // not instantiable (or subclassable):
    private MapPBag(PMap<E, Integer> map, int size) {
        this.map = map;
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<Entry<E, Integer>> i = map.entrySet().iterator();
        return new Iterator<E>() {
            private E e;
            private int n = 0;

            public boolean hasNext() {
                return n > 0 || i.hasNext();
            }

            public E next() {
                if (n == 0) { // finished with current element
                    Entry<E, Integer> entry = i.next();
                    e = entry.getKey();
                    n = entry.getValue();
                }
                n--;
                return e;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean contains(Object e) {
        return map.containsKey(e);
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (E e : this) {
            hashCode += e.hashCode();
        }
        return hashCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof PBag))
            return false;
        if (!(that instanceof MapPBag)) {
            // make that into a ImmutableMapBag
            that = MapPBag.empty().plus(that);
        }
        return this.map.equals(((MapPBag<?>) that).map);
    }

    public MapPBag<E> plus(E element) {
        return new MapPBag<E>(map.plus(element, count(element) + 1), size + 1);
    }

    @Override
    public MapPBag<E> plusAll(Collection<? extends E> list) {
        return plusAll((Iterable<? extends E>) list);
    }

    public MapPBag<E> plusAll(Iterable<? extends E> iterable) {
        MapPBag<E> bag = this;
        for (E e : iterable) {
            bag = bag.plus(e);
        }
        return bag;
    }

    @SuppressWarnings("unchecked")
    public MapPBag<E> minus(Object element) {
        int n = count(element);
        if (n == 0)
            return this;
        if (n == 1) // remove from map
            return new MapPBag<E>(map.minus(element), size - 1);
        // otherwise just decrement count:
        return new MapPBag<E>(map.plus((E) element, n - 1), size - 1);
    }

    @Override
    public MapPBag<E> minusAll(Collection<?> list) {
        return minusAll((Iterable<?>) list);
    }

    public MapPBag<E> minusAll(Iterable<?> iterable) {
        // removes _all_ elements found in list, i.e. counts are irrelevant:
        PMap<E, Integer> map = this.map.minusAll(iterable);
        return new MapPBag<E>(map, size(map)); // (completely recomputes size)
    }

    @SuppressWarnings("unchecked")
    private int count(Object o) {
        return contains(o) ? map.get((E) o) : 0;
    }

    private static int size(PMap<?, Integer> map) {
        int size = 0;
        for (Integer n : map.values()) {
            size += n;
        }
        return size;
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
