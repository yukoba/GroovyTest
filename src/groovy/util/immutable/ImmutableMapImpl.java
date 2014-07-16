/*
 * Copyright 2003-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package groovy.util.immutable;

import org.pcollections.HashPMap;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Yu Kobayashi
 * @since 2.4.0
 */
public class ImmutableMapImpl<K, V> implements ImmutableMap<K, V>, Serializable {
    private static final ImmutableMapImpl<Object, Object> EMPTY = new ImmutableMapImpl<Object, Object>(HashPMap.empty());
    private static final long serialVersionUID = -3266597752208198094L;

    private final HashPMap<K, V> map;

    private ImmutableMapImpl(HashPMap<K, V> map) {
        this.map = map;
    }

    @SuppressWarnings("unchecked")
    static <K, V> ImmutableMapImpl<K, V> empty() {
        return (ImmutableMapImpl<K, V>) EMPTY;
    }

    static <K, V> ImmutableMapImpl<K, V> from(Map<? extends K, ? extends V> map) {
        return (ImmutableMapImpl<K, V>) empty().plus(map);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public ImmutableMap<K, V> plus(K key, V value) {
        return new ImmutableMapImpl<K, V>(map.plus(key, value));
    }

    @Override
    public ImmutableMap<K, V> plus(Map<? extends K, ? extends V> m) {
        return new ImmutableMapImpl<K, V>(map.plusAll(m));
    }

    @Override
    public ImmutableMap<K, V> minus(Object key) {
        return new ImmutableMapImpl<K, V>(map.minus(key));
    }

    @Override
    public ImmutableMap<K, V> minus(Iterable<?> keys) {
        return new ImmutableMapImpl<K, V>(map.minusAll(keys));
    }

    @Override
    public V putAt(K k, V v) {
        return map.putAt(k, v);
    }

    @Override
    public V put(K k, V v) {
        return map.put(k, v);
    }

    @Override
    public V remove(Object k) {
        return map.remove(k);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
