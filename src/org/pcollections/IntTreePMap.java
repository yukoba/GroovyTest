package org.pcollections;

import java.io.Serializable;
import java.util.*;

/**
 * An efficient immutable and persistent map from integer keys to non-null values.
 * <p/>
 * Iteration occurs in the integer order of the keys.
 * <p/>
 * This implementation is thread-safe (assuming Java's AbstractMap and AbstractSet are thread-safe),
 * although its iterators may not be.
 * <p/>
 * The balanced tree is based on the Glasgow Haskell Compiler's Data.Map implementation,
 * which in turn is based on "size balanced binary trees" as described by:
 * <p/>
 * Stephen Adams, "Efficient sets: a balancing act",
 * Journal of Functional Programming 3(4):553-562, October 1993,
 * http://www.swiss.ai.mit.edu/~adams/BB/.
 * <p/>
 * J. Nievergelt and E.M. Reingold, "Binary search trees of bounded balance",
 * SIAM journal of computing 2(1), March 1973.
 *
 * @author harold
 * @author Yu Kobayashi
 */
public final class IntTreePMap<V> extends AbstractMap<Integer, V> implements PMap<Integer, V>, Serializable {
    private static final IntTreePMap<Object> EMPTY = new IntTreePMap<Object>(IntTree.EMPTYNODE);
    private static final long serialVersionUID = -777938930078650943L;

    /**
     * @return an empty map
     */
    @SuppressWarnings("unchecked")
    public static <V> IntTreePMap<V> empty() {
        return (IntTreePMap<V>) EMPTY;
    }

    /**
     * @return empty().plus(key, value)
     */
    public static <V> IntTreePMap<V> singleton(Integer key, V value) {
        return IntTreePMap.<V>empty().plus(key, value);
    }

    /**
     * @return empty().plus(map)
     */
    @SuppressWarnings("unchecked")
    public static <V> IntTreePMap<V> from(Map<? extends Integer, ? extends V> map) {
        // but that's good enough for an immutable
        // (i.e. we can't mess someone else up by adding the wrong type to it)
        return IntTreePMap.<V>empty().plusAll(map);
    }

    private final IntTree<V> root;

    // not externally instantiable (or subclassable):
    private IntTreePMap(IntTree<V> root) {
        this.root = root;
    }

    private IntTreePMap<V> withRoot(IntTree<V> root) {
        if (root == this.root) return this;
        return new IntTreePMap<V>(root);
    }

    IntTreePMap<V> withKeysChangedAbove(int key, int delta) {
        // TODO check preconditions of changeKeysAbove()
        return withRoot(root.changeKeysAbove(key, delta));
    }

    IntTreePMap<V> withKeysChangedBelow(int key, int delta) {
        // TODO check preconditions of changeKeysAbove()
        return withRoot(root.changeKeysBelow(key, delta));
    }

    // this cache variable is thread-safe, since assignment in Java is atomic:
    private transient Set<Entry<Integer, V>> entrySet;

    public Set<Entry<Integer, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Entry<Integer, V>>() {
                @Override
                public int size() { // same as Map
                    return IntTreePMap.this.size();
                }

                @Override
                public Iterator<Entry<Integer, V>> iterator() {
                    return root.iterator();
                }

                @Override
                public boolean contains(Object e) {
                    if (!(e instanceof Entry))
                        return false;
                    V value = get(((Entry<?, ?>) e).getKey());
                    return value != null && value.equals(((Entry<?, ?>) e).getValue());
                }
            };
        }
        return entrySet;
    }

    public int size() {
        return root.size();
    }

    public boolean containsKey(Object key) {
        return key instanceof Integer && root.containsKey((Integer) key);
    }

    public V get(Object key) {
        return key instanceof Integer ? root.get((Integer) key) : null;
    }

    public IntTreePMap<V> plus(Integer key, V value) {
        return withRoot(root.plus(key, value));
    }

    public IntTreePMap<V> plusAll(final Map<? extends Integer, ? extends V> map) {
        IntTree<V> root = this.root;
        for (Entry<? extends Integer, ? extends V> entry : map.entrySet())
            root = root.plus(entry.getKey(), entry.getValue());
        return withRoot(root);
    }

    public IntTreePMap<V> minus(Object key) {
        return key instanceof Integer ? withRoot(root.minus((Integer) key)) : this;
    }

    public IntTreePMap<V> minusAll(Collection<?> keys) {
        return minusAll((Iterable<?>) keys);
    }

    public IntTreePMap<V> minusAll(Iterable<?> keys) {
        IntTree<V> root = this.root;
        for (Object key : keys) {
            if (key instanceof Integer) {
                root = root.minus((Integer) key);
            }
        }
        return withRoot(root);
    }

    @Deprecated
    public V putAt(Integer key, V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public V put(Integer k, V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public V remove(Object k) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void putAll(Map<? extends Integer, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
