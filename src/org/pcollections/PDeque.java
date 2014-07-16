package org.pcollections;

import java.util.Deque;

/**
 * A persistent deque.
 * 
 * @author mtklein
 * @author Yu Kobayashi
 */
public interface PDeque<E> extends PCollection<E>, Deque<E> {
    /**
     * Complexity: O(1)
     *
     * @return the first element of this deque
     */
    E peek();

    /**
     * Complexity: O(1)
     *
     * @return the first element of this deque
     */
    E peekFirst();

    /**
     * Complexity: O(1)
     *
     * @return the last element of this deque
     */
    E peekLast();

    /**
     * Complexity: O(1)
     *
     * @return the first element of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E element();

    /**
     * Complexity: O(1)
     *
     * @return the first element of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E getFirst();

    /**
     * Complexity: O(1)
     *
     * @return the last element of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E getLast();

    /**
     * Complexity: O(1)
     *
     * @return the first element of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E first();

    /**
     * Complexity: O(1)
     *
     * @return the first element of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E head();

    /**
     * Complexity: O(1)
     *
     * @return the last element of this deque
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    E last();

    /**
     * Complexity: amortized O(1), worst-case O(n)
     *
     * @return a deque without its first element
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    PDeque<E> tail();

    /**
     * Complexity: amortized O(1), worst-case O(n)
     *
     * @return a deque without its last element
     * @throws java.util.NoSuchElementException if this deque is empty
     */
    PDeque<E> init();

    /**
     * Complexity: O(1)
     *
     * @param element an element to append
     * @return a deque which contains the element and all of the elements of this
     */
    PDeque<E> plus(E element);

    /**
     * Complexity: O(1)
     *
     * @param element an element to append
     * @return a deque which contains the element and all of the elements of this
     */
    PDeque<E> plusFirst(E element);

    /**
     * Complexity: O(1)
     *
     * @param element an element to append
     * @return a deque which contains the element and all of the elements of this
     */
    PDeque<E> plusLast(E element);

    /**
     * Complexity: O(iterable.size())
     *
     * @param iterable elements to append
     * @return a deque which contains all of the elements of iterable and this
     */
    PDeque<E> plusAll(Iterable<? extends E> iterable);

    /**
     * Complexity: O(iterable.size())
     *
     * @param iterable elements to append
     * @return a deque which contains all of the elements of iterable and this
     */
    PDeque<E> plusFirst(Iterable<? extends E> iterable);

    /**
     * Complexity: O(iterable.size())
     *
     * @param iterable elements to append
     * @return a deque which contains all of the elements of iterable and this
     */
    PDeque<E> plusLast(Iterable<? extends E> iterable);

    /**
     * Complexity: O(n)
     *
     * @param element an element to remove
     * @return this with a single instance of the element removed, if the element is in this deque
     */
    PDeque<E> minus(Object element);

    /**
     * Complexity: O(n + iterable.size())
     *
     * @param iterable elements to remove
     * @return this with all elements of the iterable completely removed
     */
    PDeque<E> minusAll(Iterable<?> iterable);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    boolean offer(E e);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    E poll();

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    E remove();

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    void addFirst(E e);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    void addLast(E e);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    boolean offerFirst(E e);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    boolean offerLast(E e);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    E removeFirst();

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    E removeLast();

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    E pollFirst();

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    E pollLast();

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    boolean removeFirstOccurrence(Object o);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    boolean removeLastOccurrence(Object o);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    void push(E e);

    /**
     * Always throws {@link UnsupportedOperationException}.
     */
    @Deprecated
    E pop();
}
