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

import org.pcollections.OrderedPSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Yu Kobayashi
 * @since 2.4.0
 */
class ImmutableListSetImpl<E> implements ImmutableListSet<E>, Serializable {
    private static final ImmutableListSetImpl<Object> EMPTY = new ImmutableListSetImpl<Object>(OrderedPSet.empty());
    private static final long serialVersionUID = -3773996693856253314L;

    private final OrderedPSet<E> set;

    private ImmutableListSetImpl(OrderedPSet<E> set) {
        this.set = set;
    }

    @SuppressWarnings("unchecked")
    static <E> ImmutableListSetImpl<E> empty() {
        return (ImmutableListSetImpl<E>) EMPTY;
    }

    static <E> ImmutableListSetImpl<E> from(Iterable<? extends E> iterable) {
        return (ImmutableListSetImpl<E>) empty().plus(iterable);
    }

    @Override
    public ImmutableListSet<E> plus(E element) {
        return new ImmutableListSetImpl<E>(set.plus(element));
    }

    @Override
    public ImmutableListSet<E> plus(Iterable<? extends E> iterable) {
        return new ImmutableListSetImpl<E>(set.plusAll(iterable));
    }

    @Override
    public ImmutableListSet<E> minus(Object element) {
        return new ImmutableListSetImpl<E>(set.minus(element));
    }

    @Override
    public ImmutableListSet<E> minus(Iterable<?> iterable) {
        return new ImmutableListSetImpl<E>(set.minusAll(iterable));
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(E o) {
        return set.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return set.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public E get(int index) {
        return set.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return set.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return set.lastIndexOf(o);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return set.equals(obj);
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
