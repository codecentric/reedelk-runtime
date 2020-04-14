package com.reedelk.platform.execution.context;

import java.util.Set;

class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {

    SynchronizedSet(Set<E> s) {
        super(s);
    }

    SynchronizedSet(Set<E> s, Object mutex) {
        super(s, mutex);
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        synchronized (mutex) {return c.equals(o);}
    }
    public int hashCode() {
        synchronized (mutex) {return c.hashCode();}
    }
}
