package me.study.structure;

public interface ListI<E> extends Iterable<E> {
    void addFirst(E i);
    void addLast(E obj);
    E removeFirst();
    E removeLast();
    E remove(E obj);
    boolean contains(E obj);
    E peekFirst();
    E peekLast();
    //boolean isEmpty();
    //boolean isFull();
    //void makeEmpty();
}
