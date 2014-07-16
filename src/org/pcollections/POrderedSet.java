package org.pcollections;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Like {@link org.pcollections.PSet} but preserves insertion order. Persistent equivalent of
 * {@link java.util.LinkedHashSet}.
 * 
 * @author Tassilo Horn &lt;horn@uni-koblenz.de&gt;
 * @author Yu Kobayashi
 * 
 * @param <E>
 */
public interface POrderedSet<E> extends PSet<E> {

	public POrderedSet<E> plus(E e);

	public POrderedSet<E> plusAll(Iterable<? extends E> list);

	public POrderedSet<E> plusAll(Collection<? extends E> list);

	public POrderedSet<E> minus(Object e);

	public POrderedSet<E> minusAll(Iterable<?> list);

	public POrderedSet<E> minusAll(Collection<?> list);

	E get(int index);

	int indexOf(Object o);
}
