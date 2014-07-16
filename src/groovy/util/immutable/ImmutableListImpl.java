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

import org.pcollections.TreePVector;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * @author Yu Kobayashi
 * @since 2.4.0
 */
class ImmutableListImpl<E> implements ImmutableList<E>, Serializable {
    private static final ImmutableListImpl<Object> EMPTY = new ImmutableListImpl<Object>(TreePVector.empty());
    private static final long serialVersionUID = -7182079639404183366L;

    private final TreePVector<E> list;

    private ImmutableListImpl(TreePVector<E> list) {
        this.list = list;
    }

    @SuppressWarnings("unchecked")
    static <E> ImmutableListImpl<E> empty() {
        return (ImmutableListImpl<E>) EMPTY;
    }

    static <E> ImmutableListImpl<E> from(Iterable<? extends E> iterable) {
        return (ImmutableListImpl<E>) empty().plus(iterable);
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        return list.set(index, element);
    }

    @Override
    public ImmutableList<E> plus(E element) {
        return new ImmutableListImpl<E>(list.plus(element));
    }

    @Override
    public ImmutableList<E> plus(Iterable<? extends E> iterable) {
        return new ImmutableListImpl<E>(list.plusAll(iterable));
    }

    @Override
    public ImmutableList<E> plusAt(int index, E element) {
        return new ImmutableListImpl<E>(list.plus(index, element));
    }

    @Override
    public ImmutableList<E> plusAt(int index, Iterable<? extends E> iterable) {
        return new ImmutableListImpl<E>(list.plusAll(index, iterable));
    }

    @Override
    public ImmutableList<E> replaceAt(int index, E element) {
        return new ImmutableListImpl<E>(list.with(index, element));
    }

    @Override
    public ImmutableList<E> minus(Object element) {
        return new ImmutableListImpl<E>(list.minus(element));
    }

    @Override
    public ImmutableList<E> minus(Iterable<?> iterable) {
        return new ImmutableListImpl<E>(list.minusAll(iterable));
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(E o) {
        return list.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return list.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public ImmutableList<E> minusAt(int index) {
        return new ImmutableListImpl<E>(list.minus(index));
    }

    @Override
    public ImmutableList<E> subList(int start, int end) {
        return new ImmutableListImpl<E>(list.subList(start, end));
    }

    @Override
    public ImmutableList<E> subList(int start) {
        return new ImmutableListImpl<E>(list.subList(start));
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return list.addAll(index, c);
    }

    @Override
    public void add(int index, E e) {
        list.add(index, e);
    }

    @Override
    public E remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return list.equals(obj);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
