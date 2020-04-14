package com.reedelk.platform.execution.context;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

class SynchronizedCollection<E> implements Collection<E>, Serializable {

    final Collection<E> c;  // Backing Collection
    final Object mutex;     // Object on which to synchronize

    SynchronizedCollection(Collection<E> c) {
        this.c = Objects.requireNonNull(c);
        mutex = this;
    }

    SynchronizedCollection(Collection<E> c, Object mutex) {
        this.c = Objects.requireNonNull(c);
        this.mutex = Objects.requireNonNull(mutex);
    }

    public int size() {
        synchronized (mutex) {
            return c.size();
        }
    }

    public boolean isEmpty() {
        synchronized (mutex) {
            return c.isEmpty();
        }
    }

    public boolean contains(Object o) {
        synchronized (mutex) {
            return c.contains(o);
        }
    }

    public Object[] toArray() {
        synchronized (mutex) {
            return c.toArray();
        }
    }

    public <T> T[] toArray(T[] a) {
        synchronized (mutex) {
            return c.toArray(a);
        }
    }

    public Iterator<E> iterator() {
        return c.iterator(); // Must be manually synched by user!
    }

    public boolean add(E e) {
        synchronized (mutex) {
            return c.add(e);
        }
    }

    public boolean remove(Object o) {
        synchronized (mutex) {
            return c.remove(o);
        }
    }

    public boolean containsAll(Collection<?> coll) {
        synchronized (mutex) {
            return c.containsAll(coll);
        }
    }

    public boolean addAll(Collection<? extends E> coll) {
        synchronized (mutex) {
            return c.addAll(coll);
        }
    }

    public boolean removeAll(Collection<?> coll) {
        synchronized (mutex) {
            return c.removeAll(coll);
        }
    }

    public boolean retainAll(Collection<?> coll) {
        synchronized (mutex) {
            return c.retainAll(coll);
        }
    }

    public void clear() {
        synchronized (mutex) {
            c.clear();
        }
    }

    public String toString() {
        synchronized (mutex) {
            return c.toString();
        }
    }

    // Override default methods in Collection
    @Override
    public void forEach(Consumer<? super E> consumer) {
        synchronized (mutex) {
            c.forEach(consumer);
        }
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        synchronized (mutex) {
            return c.removeIf(filter);
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        return c.spliterator(); // Must be manually synched by user!
    }

    @Override
    public Stream<E> stream() {
        return c.stream(); // Must be manually synched by user!
    }

    @Override
    public Stream<E> parallelStream() {
        return c.parallelStream(); // Must be manually synched by user!
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        synchronized (mutex) {
            s.defaultWriteObject();
        }
    }
}
