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

import org.pcollections.AmortizedPDeque;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Yu Kobayashi
 * @since 2.4.0
 */
class ImmutableDequeImpl<E> implements ImmutableDeque<E>, Serializable {
    private static final ImmutableDequeImpl<Object> EMPTY = new ImmutableDequeImpl<Object>(AmortizedPDeque.empty());
    private static final long serialVersionUID = 8383495243255576114L;

    private final AmortizedPDeque<E> deque;

    private ImmutableDequeImpl(AmortizedPDeque<E> deque) {
        this.deque = deque;
    }

    @SuppressWarnings("unchecked")
    static <E> ImmutableDequeImpl<E> empty() {
        return (ImmutableDequeImpl<E>) EMPTY;
    }

    static <E> ImmutableDequeImpl<E> from(Iterable<? extends E> iterable) {
        return (ImmutableDequeImpl<E>) empty().plus(iterable);
    }

    @Override
    public E peek() {
        return deque.peek();
    }

    @Override
    public E peekFirst() {
        return deque.peekFirst();
    }

    @Override
    public E peekLast() {
        return deque.peekLast();
    }

    @Override
    public E element() {
        return deque.element();
    }

    @Override
    public E getFirst() {
        return deque.getFirst();
    }

    @Override
    public E getLast() {
        return deque.getLast();
    }

    @Override
    public E first() {
        return deque.first();
    }

    @Override
    public E head() {
        return deque.head();
    }

    @Override
    public E last() {
        return deque.last();
    }

    @Override
    public ImmutableDeque<E> tail() {
        return new ImmutableDequeImpl<E>(deque.tail());
    }

    @Override
    public ImmutableDeque<E> init() {
        return new ImmutableDequeImpl<E>(deque.init());
    }

    @Override
    public ImmutableDeque<E> plus(E element) {
        return new ImmutableDequeImpl<E>(deque.plus(element));
    }

    @Override
    public ImmutableDeque<E> plusFirst(E element) {
        return new ImmutableDequeImpl<E>(deque.plusFirst(element));
    }

    @Override
    public ImmutableDeque<E> plusLast(E element) {
        return new ImmutableDequeImpl<E>(deque.plusLast(element));
    }

    @Override
    public ImmutableDeque<E> plus(Iterable<? extends E> iterable) {
        return new ImmutableDequeImpl<E>(deque.plusAll(iterable));
    }

    @Override
    public ImmutableDeque<E> plusFirst(Iterable<? extends E> iterable) {
        return new ImmutableDequeImpl<E>(deque.plusFirst(iterable));
    }

    @Override
    public ImmutableDeque<E> plusLast(Iterable<? extends E> iterable) {
        return new ImmutableDequeImpl<E>(deque.plusLast(iterable));
    }

    @Override
    public ImmutableDeque<E> minus(Object element) {
        return new ImmutableDequeImpl<E>(deque.minus(element));
    }

    @Override
    public ImmutableDeque<E> minus(Iterable<?> iterable) {
        return new ImmutableDequeImpl<E>(deque.minusAll(iterable));
    }

    @Override
    public int size() {
        return deque.size();
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return deque.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return deque.iterator();
    }

    @Override
    public Object[] toArray() {
        return deque.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return deque.toArray(a);
    }

    @Override
    public boolean add(E o) {
        return deque.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return deque.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return deque.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return deque.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return deque.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return deque.retainAll(c);
    }

    @Override
    public void clear() {
        deque.clear();
    }

    @Override
    public boolean offer(E e) {
        return deque.offer(e);
    }

    @Override
    public E poll() {
        return deque.poll();
    }

    @Override
    public E remove() {
        return deque.remove();
    }

    @Override
    public void addFirst(E e) {
        deque.addFirst(e);
    }

    @Override
    public void addLast(E e) {
        deque.addLast(e);
    }

    @Override
    public boolean offerFirst(E e) {
        return deque.offerFirst(e);
    }

    @Override
    public boolean offerLast(E e) {
        return deque.offerLast(e);
    }

    @Override
    public E removeFirst() {
        return deque.removeFirst();
    }

    @Override
    public E removeLast() {
        return deque.removeLast();
    }

    @Override
    public E pollFirst() {
        return deque.pollFirst();
    }

    @Override
    public E pollLast() {
        return deque.pollLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return deque.removeFirstOccurrence(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return deque.removeLastOccurrence(o);
    }

    @Override
    public void push(E e) {
        deque.push(e);
    }

    @Override
    public E pop() {
        return deque.pop();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return deque.descendingIterator();
    }

    @Override
    public int hashCode() {
        return deque.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return deque.equals(obj);
    }

    @Override
    public String toString() {
        return deque.toString();
    }
}
