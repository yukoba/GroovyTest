package org.pcollections;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.io.Serializable;
import java.util.*;

/**
 * @author mtklein
 * @author Yu Kobayashi
 */
public final class AmortizedPDeque<E> extends AbstractQueue<E> implements PDeque<E>, Serializable {
    private static final AmortizedPDeque<Object> EMPTY = new AmortizedPDeque<Object>();
    private static final long serialVersionUID = 5550057968536499274L;

    @SuppressWarnings("unchecked")
    public static <E> AmortizedPDeque<E> empty() {
        return (AmortizedPDeque<E>) EMPTY;
    }

    public static <E> AmortizedPDeque<E> singleton(E e) {
        return AmortizedPDeque.<E>empty().plus(e);
    }

    public static <E> AmortizedPDeque<E> from(Iterable<? extends E> iterable) {
        return AmortizedPDeque.<E>empty().plusAll(iterable);
    }

    private final ConsPStack<E> front;
    private final ConsPStack<E> back;

    private AmortizedPDeque() {
        front = ConsPStack.empty();
        back = ConsPStack.empty();
    }

    private AmortizedPDeque(AmortizedPDeque<E> queue, E e, boolean isInsertFront) {
        // Guarantee that there is always at least 1 element in front or back, which makes peek worst-case O(1).
        switch (queue.size()) {
            case 0:
                this.front = queue.front.plus(e);
                this.back = queue.back;
                break;

            case 1:
                // Must be front.size() == 1 && back.size() == 1 after this.
                if (queue.front.size() == 1) {
                    if (isInsertFront) {
                        this.front = queue.back.plus(e);
                        this.back = queue.front;
                    } else {
                        this.front = queue.front;
                        this.back = queue.back.plus(e);
                    }
                } else {
                    if (isInsertFront) {
                        this.front = queue.front.plus(e);
                        this.back = queue.back;
                    } else {
                        this.front = queue.back;
                        this.back = queue.front.plus(e);
                    }
                }
                break;

            default:
                if (isInsertFront) {
                    this.front = queue.front.plus(e);
                    this.back = queue.back;
                } else {
                    this.front = queue.front;
                    this.back = queue.back.plus(e);
                }
                break;
        }
    }

    private AmortizedPDeque(ConsPStack<E> front, ConsPStack<E> back) {
        this.front = front;
        this.back = back;
    }

    // Worst-case O(n)
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private AmortizedPDeque<E> queue = AmortizedPDeque.this;

            public boolean hasNext() {
                return queue.size() > 0;
            }

            public E next() {
                E e = queue.getFirst(); // Might throws NoSuchElementException
                queue = queue.tail();
                return e;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Worst-case O(n)
    public Iterator<E> descendingIterator() {
        return new Iterator<E>() {
            private AmortizedPDeque<E> queue = AmortizedPDeque.this;

            public boolean hasNext() {
                return queue.size() > 0;
            }

            public E next() {
                E e = queue.getLast(); // Might throws NoSuchElementException
                queue = queue.init();
                return e;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (E e : this) {
            hashCode += (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof PDeque))
            return false;
        PDeque queue = (PDeque) that;

        if (size() != queue.size())
            return false;

        if (size() == 0) {
            return queue.size() == 0;
        } else {
            Object obj1 = peek();
            Object obj2 = queue.peek();
            return (obj1 == null ? obj2 == null : obj1.equals(obj2)) && tail().equals(queue.tail());
        }
    }

    // Worst-case O(1)
    @Override
    public int size() {
        return front.size() + back.size();
    }

    // Worst-case O(1)
    public E peekFirst() {
        switch (size()) {
            case 0:
                return null;
            case 1:
                return front.size() > 0 ? front.get(0) : back.get(0);
            default:
                return front.get(0);
        }
    }

    // Worst-case O(1)
    public E getFirst() {
        if (size() == 0)
            throw new NoSuchElementException();
        return peekFirst();
    }

    public E peek() {
        return peekFirst();
    }

    public E element() {
        return getFirst();
    }

    public E head() {
        return getFirst();
    }

    public E first() {
        return getFirst();
    }

    // Worst-case O(1)
    public E peekLast() {
        switch (size()) {
            case 0:
                return null;
            case 1:
                return front.size() > 0 ? front.get(0) : back.get(0);
            default:
                return back.get(0);
        }
    }

    // Worst-case O(1)
    public E getLast() {
        if (size() == 0)
            throw new NoSuchElementException();
        return peekLast();
    }

    public E last() {
        return getLast();
    }

    // Amortized O(1), worst-case O(n)
    public AmortizedPDeque<E> tail() {
        switch (size()) {
            case 0:
                throw new NoSuchElementException();
            case 1:
                return empty();
        }

        switch (front.size()) {
            case 0:
                // Should never happen
                throw new RuntimeException("Deque is broken. front.size() = 0, back.size = " + back.size());

            case 1:
                // If there's one element on front, dump back onto front,
                // but now we've already removed the head.
                return new AmortizedPDeque<E>(
                        ConsPStack.from(back.subList(1)),
                        ConsPStack.singleton(back.get(0)));

            default:
                // If there's more than one on front, we pop one off.
                return new AmortizedPDeque<E>(front.subList(1), back);
        }
    }

    // Amortized O(1), worst-case O(n)
    public AmortizedPDeque<E> init() {
        switch (size()) {
            case 0:
                throw new NoSuchElementException();
            case 1:
                return empty();
        }

        switch (back.size()) {
            case 0:
                // Should never happen
                throw new RuntimeException("Deque is broken. front.size() = " + front.size() + ", back.size = 0");

            case 1:
                return new AmortizedPDeque<E>(
                        ConsPStack.singleton(front.get(0)),
                        ConsPStack.from(front.subList(1)));

            default:
                return new AmortizedPDeque<E>(front, back.subList(1));

        }
    }

    // Worst-case O(1)
    public AmortizedPDeque<E> plusFirst(E element) {
        return new AmortizedPDeque<E>(this, element, true);
    }

    // Worst-case O(k)
    public AmortizedPDeque<E> plusFirst(Iterable<? extends E> iterable) {
        AmortizedPDeque<E> result = this;
        for (E e : iterable) {
            result = result.plusFirst(e);
        }
        return result;
    }

    // Worst-case O(1)
    public AmortizedPDeque<E> plusLast(E element) {
        return new AmortizedPDeque<E>(this, element, false);
    }

    // Worst-case O(k)
    public AmortizedPDeque<E> plusLast(Iterable<? extends E> iterable) {
        AmortizedPDeque<E> result = this;
        for (E e : iterable) {
            result = result.plusLast(e);
        }
        return result;
    }

    // Worst-case O(1)
    public AmortizedPDeque<E> plus(E element) {
        return plusLast(element);
    }

    // Worst-case O(k)
    public AmortizedPDeque<E> plusAll(Iterable<? extends E> iterable) {
        return plusLast(iterable);
    }

    public AmortizedPDeque<E> plusAll(Collection<? extends E> iterable) {
        return plusAll((Iterable<? extends E>) iterable);
    }

    // These 2 methods not guaranteed to be fast.

    public AmortizedPDeque<E> minus(Object element) {
        ArrayList<E> list = new ArrayList<E>(this);
        list.remove(element);
        return from(list);
    }

    public AmortizedPDeque<E> minusAll(Iterable<?> iterable) {
        ArrayList<E> list = new ArrayList<E>(this);
        list.removeAll(DefaultGroovyMethods.asCollection(iterable));
        return from(list);
    }

    public AmortizedPDeque<E> minusAll(Collection<?> iterable) {
        return minusAll((Iterable<?>) iterable);
    }

    @Deprecated
    public boolean offer(E o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public E poll() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void addFirst(E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void addLast(E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean offerFirst(E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean offerLast(E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public E removeFirst() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public E removeLast() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean removeFirstOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public boolean removeLastOccurrence(Object o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void push(E e) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public E pop() {
        throw new UnsupportedOperationException();
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
