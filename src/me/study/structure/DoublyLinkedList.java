package me.study.structure;

import java.util.Iterator;

public class DoublyLinkedList<E> implements ListI<E> {

    // 노드 정의
    // 내부 클래스로 외부에서 접근할 수 없다.
    class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;
        public Node(E obj){
            data=obj;
            next=null;
            prev=null; // 바로 전의 노드를 가리키는 포인터 추가
        }
    }

    private Node<E> head;
    private Node<E> tail;

    /*
    노드 개수를 세는 변수
    크기 변수를 만들어둠으로써, 리스트의 크기 확인이 필요한 경우, O(n)에서 O(1)로 시간이 단축된다.
    즉, 연결 리스트의 크기를 상수 시간으로 알 수 있게 해준다.
    */
    private int currentSize;

    // default 연결리스트 생성자
    public DoublyLinkedList() {
        head = tail = null;
        currentSize = 0;
    }

    /*
    * prev 포인터를 활용하므로, O(1) 시간 복잡도 소요
    * */
    @Override
    public E removeLast() {
        // 경계 조건 1.
        if (head == null) return null;
        // 2. && 3.
        if (head == tail) return removeFirst();

        E tmp = tail.data;

        tail.prev.next = null; // 삭제될 노드를 가리키는 이전 노드의 next 포인터 제거
        tail = tail.prev; // tail 포인터가 이전 노드를 가리키도록 변경, 마지막 노드는 가비지 컬렉션 대상이 된다.

        currentSize--;
        return tmp;
    }

    @Override
    public void addFirst(E obj) {
        Node<E> node = new Node<E>(obj);
        if (head==null) tail = node;

        node.next = head;
        head.prev = node; // 기존 첫 노드(head or node.next)의 prev가 삽입된 노드를 가리키도록 설정
        head = node; // head가 가리키는 위치를 새로 삽입된 노드로 이동
        currentSize++;
    }

    /*
    * 리스트 중간에 삽입하는 경우, O(n) 시간복잡도
   * */
    public void add(E obj, int index) {
        if (index == 0) {
            addFirst(obj); // 1. 2. 3.
            return;
        } else if (index==currentSize) {
            addLast(obj); // 4.
            return;
        } else if (index>currentSize) throw new IndexOutOfBoundsException();


        Node<E> node = new Node<E>(obj);

        // 삽입할 위치의 노드 찾기
        Node<E> tmp = head;
        for(int i = 0; i < index; i++) tmp = tmp.next;

        // 포인터 스왑
        node.next = tmp;
        node.prev = tmp.prev;
        node.prev.next = node;
        tmp.prev = node;
        currentSize++;
    }

    @Override
    public void addLast(E obj) {

    }
    @Override
    public E removeFirst() {
        return null;
    }
    @Override
    public E remove(E obj) {
        return null;
    }
    @Override
    public boolean contains(E obj) {
        return false;
    }
    @Override
    public E peekFirst() {
        return null;
    }
    @Override
    public E peekLast() {
        return null;
    }
    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
