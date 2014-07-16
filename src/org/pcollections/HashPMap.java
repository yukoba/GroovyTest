package org.pcollections;

import java.io.Serializable;
import java.util.*;

/**
 * An immutable and persistent map from non-null keys to nullable values.
 * <p/>
 * This map uses a given integer map to map hashcodes to lists of elements
 * with the same hashcode. Thus if all elements have the same hashcode, performance
 * is reduced to that of an association list.
 * <p/>
 * This implementation is thread-safe (assuming Java's AbstractMap and AbstractSet are thread-safe),
 * although its iterators may not be.
 *
 * @author harold
 * @author Yu Kobayashi
 */
public final class HashPMap<K, V> extends AbstractMap<K, V> implements PMap<K, V>, Serializable {
    private static final HashPMap<Object, Object> EMPTY = HashPMap.empty(IntTreePMap.<PSequence<Entry<Object, Object>>>empty());
    private static final long serialVersionUID = -3538508895284495077L;

    /**
     * @return an empty map
     */
    @SuppressWarnings("unchecked")
    public static <K, V> HashPMap<K, V> empty() {
        return (HashPMap<K, V>) EMPTY;
    }

    /**
     * @return empty().plus(key, value)
     */
    public static <K, V> HashPMap<K, V> singleton(K key, V value) {
        return HashPMap.<K, V>empty().plus(key, value);
    }

    /**
     * @return empty().plus(map)
     */
    public static <K, V> HashPMap<K, V> from(Map<? extends K, ? extends V> map) {
        return HashPMap.<K, V>empty().plusAll(map);
    }

    /**
     * @return a map backed by an empty version of intMap, i.e. backed by intMap.minusAll(intMap.keySet())
     */
    public static <K, V> HashPMap<K, V> empty(PMap<Integer, PSequence<Entry<K, V>>> intMap) {
        return new HashPMap<K, V>(intMap.minus(intMap.keySet()), 0);
    }

    private final PMap<Integer, PSequence<Entry<K, V>>> intMap;
    private final int size;

    // not externally instantiable (or subclassable):
    private HashPMap(PMap<Integer, PSequence<Entry<K, V>>> intMap, int size) {
        this.intMap = intMap;
        this.size = size;
    }

    // this cache variable is thread-safe since assignment in Java is atomic:
    private transient Set<Entry<K, V>> entrySet;

    public Set<Entry<K, V>> entrySet() {
        if (entrySet == null)
            entrySet = new AbstractSet<Entry<K, V>>() {
                // REQUIRED METHODS OF AbstractSet //
                @Override
                public int size() {
                    return size;
                }

                @Override
                public Iterator<Entry<K, V>> iterator() {
                    return new SequenceIterator<Entry<K, V>>(intMap.values().iterator());
                }

                // OVERRIDDEN METHODS OF AbstractSet //
                @Override
                public boolean contains(Object e) {
                    if (!(e instanceof Entry))
                        return false;
                    V value = get(((Entry<?, ?>) e).getKey());
                    return value != null && value.equals(((Entry<?, ?>) e).getValue());
                }
            };
        return entrySet;
    }


    public int size() {
        return size;
    }

    public boolean containsKey(Object key) {
        return keyIndexIn(getEntries(key.hashCode()), key) != -1;
    }

    public V get(Object key) {
        PSequence<Entry<K, V>> entries = getEntries(key.hashCode());
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public HashPMap<K, V> plusAll(Map<? extends K, ? extends V> map) {
        HashPMap<K, V> result = this;
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            result = result.plus(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public PMap<K, V> minusAll(Collection<?> keys) {
        return minusAll((Iterable<?>) keys);
    }

    public HashPMap<K, V> minusAll(Iterable<?> keys) {
        HashPMap<K, V> result = this;
        for (Object key : keys) {
            result = result.minus(key);
        }
        return result;
    }

    public HashPMap<K, V> plus(K key, V value) {
        PSequence<Entry<K, V>> entries = getEntries(key.hashCode());
        int size0 = entries.size();
        int i = keyIndexIn(entries, key);
        if (i != -1)
            entries = entries.minus(i);
        entries = entries.plus(new SimpleImmutableEntry<K, V>(key, value));
        return new HashPMap<K, V>(intMap.plus(key.hashCode(), entries), size - size0 + entries.size());
    }

    public HashPMap<K, V> minus(Object key) {
        PSequence<Entry<K, V>> entries = getEntries(key.hashCode());
        int i = keyIndexIn(entries, key);
        if (i == -1) // key not in this
            return this;
        entries = entries.minus(i);
        if (entries.size() == 0) // get rid of the entire hash entry
            return new HashPMap<K, V>(intMap.minus(key.hashCode()), size - 1);
        // otherwise replace hash entry with new smaller one:
        return new HashPMap<K, V>(intMap.plus(key.hashCode(), entries), size - 1);
    }

    private PSequence<Entry<K, V>> getEntries(int hash) {
        PSequence<Entry<K, V>> entries = intMap.get(hash);
        if (entries == null) return ConsPStack.empty();
        return entries;
    }

    private static <K, V> int keyIndexIn(PSequence<Entry<K, V>> entries, Object key) {
        int i = 0;
        for (Entry<K, V> entry : entries) {
            if (entry.getKey().equals(key))
                return i;
            i++;
        }
        return -1;
    }

    static class SequenceIterator<E> implements Iterator<E> {
        private final Iterator<PSequence<E>> i;
        private PSequence<E> seq = ConsPStack.empty();

        SequenceIterator(Iterator<PSequence<E>> i) {
            this.i = i;
        }

        public boolean hasNext() {
            return seq.size() > 0 || i.hasNext();
        }

        public E next() {
            if (seq.size() == 0)
                seq = i.next();
            final E result = seq.get(0);
            seq = seq.subList(1, seq.size());
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Deprecated
    public V putAt(K key, V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public V remove(Object k) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
