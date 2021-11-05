package me.study.structure;

public interface HashListI<E> extends Iterable<E>{
    void addFirst(E e);
    E remove(E e);
}
