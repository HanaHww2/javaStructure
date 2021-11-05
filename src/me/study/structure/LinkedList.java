package me.study.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements ListI<E>, HashListI<E> {

    // 노드 정의
    // 내부 클래스로 외부에서 접근할 수 없다.
    class Node<E> {
        E data;
        Node<E> next;
        public Node(E obj){
            data=obj;
            next=null;
        }
    }

    // Iterator 인터페이스를 구현한 내부 클래스 IteratorHelper
    class IteratorHelper implements Iterator<E> {
        Node<E> index;
        public IteratorHelper() { // 생성자
            index = head;
        }
        @Override
        public boolean hasNext() {
            return (index!=null);
        }
        @Override
        public E next() {
            if (!hasNext())  throw new NoSuchElementException();
            E val = index.data;
            index = index.next;
            return val;
        }
    }
    // Iterable 인터페이스(ListI, HashListI 인터페이스가 상속)의 구현 메소드
    @Override
    public Iterator<E> iterator() {
        return new IteratorHelper();
    }

    /*
    노드 개수를 세는 변수
    크기 변수를 만들어둠으로써, 리스트의 크기 확인이 필요한 경우, O(n)에서 O(1)로 시간이 단축된다.
    즉, 연결 리스트의 크기를 상수 시간으로 알 수 있게 해준다.
    */
    private int currentSize;
    private Node<E> head;
    private Node<E> tail;

    // default 연결리스트 생성자
    public LinkedList() {
        head = tail = null;
        currentSize = 0;
    }

    public int getCurrentSize() {
        return this.currentSize;
    }

    /*
    리스트의 처음에 새 노드 추가
    시간 복잡도는 상수 시간, O(1)이다.
    head가 기존에 가리키던 node 요소가 있었다면, 이를 새로운 노드가 가리키게 한 다음에 head가 새로운 노드를 가리키도록 한다.
    head가 먼저 새로운 node를 가리키게 한다면, 기존에 head가 가리키던 노드의 주소가 유실되고 기존 노드를 참조하는 곳이 사라져서
    이 기존 요소는 가비지 컬렉션의 대상이 될 것이다. 즉, 자료의 손실이 발생한다.
     */
    public void addFirst(E obj) {
        Node<E> node = new Node<E>(obj);
        if (head==null) tail = node;
        node.next = head;
        head = node;
        currentSize++;
    }

    /*
    * 임시 포인터를 이용해 리스트의 마지막에 새 노드 추가
    * 시간 복잡도는 O(n)이다.
    * head 가 null인 경우, 분기를 해서 NullPointerException을 회피한다.
    * 임시 포인터를 이용해 head가 가리키는 첫번째 노드부터 while 반복문을 이용해 그 다음 노드를 확인한다. 다음 노드가 없다면, 그 위치에
    * 새롭게 생성한 노드를 삽입한다.
    * */
    public void addLastWithoutTail(E obj) {
        Node<E> tmp = head;
        Node<E> node = new Node<E>(obj);
        if (head==null) {
            head = node;
            currentSize++;
            return;
        }
        while (tmp.next!=null) {
            tmp = tmp.next;
        }
        tmp.next = node;
    }
    /*
    * tail 포인터를 이용해 리스트 마지막에 새 노드 추가
    * 시간 복잡도는 O(1)이다.
    * tail 포인터를 글로벌 변수로 생성한다. 리스트에서 노드의 추가/삭제가 있을 때마다 포인터를 수정해야 하므로,
    * 이를 구현하기 위한 복잡도가 증가한다. 그러나, 여전히 tail 포인터를 활용하는 것이 보다 효율적이다.
    * */
    public void addLast(E obj) {
        Node<E> node = new Node<E>(obj);
        if (head==null) {
            head = tail = node;
            currentSize++;
            return;
        }
        tail = tail.next = node;
        currentSize++;
    }

    /*
    *
    * */
    public E removeFirst() {
        // 1. 자료구조가 비었을 경우, NullPointerException 방지
        if (head==null) return null;

        E tmp = head.data;

        //2. 요소가 한 개인 경우, if (head.next = null) or if (currentSize == 1) 등의 조건문으로 확인 후 처리
        if (head==tail) head = tail = null;
        else head = head.next; // 기존에 head 객체가 가리키던 노드는 가리키는 포인터가 사라져 가비지 컬렉션의 대상이 된다.

        currentSize--;
        return tmp; // 삭제 대상 노드 반환
    }

    /**/
    public E removeLast() {
        // 1.
        if (head == null) return null;
        // 2. && 3.
        if (head == tail) return removeFirst();

        // 2가지 임시 포인터 선언
        // 현재 위치 노드를 가리키는 포인터와 그 이전 노드를 가리키는 포인터
        Node <E> current = head, previous = null;

        // 4. 마지막 노드 탐색, (current.next!=null)로 대체 가능
        while (current!=tail) {
            previous = current;
            current = current.next;
        }
        previous.next = null; // 삭제될 노드를 가리키는 포인터 제거
        tail = previous; // tail 포인터가 가리키는 마지막 노드 변경

        currentSize--;
        return current.data;
    }

    public E remove(E obj) {
        Node <E> current = head, previous = null;

        while (current!=null) { // 1. 빈 자료구조일 경우 while 구문을 거치지 않고 null 리턴
            if (((Comparable<E>) obj).compareTo(current.data) == 0) {
                // 2. && 3.
                if (current == head) return removeFirst();
                // 4.
                if (current == tail) tail = previous; // return removeLast();

                // 5. 중간에 있는 데이터 다루기기
               previous.next = current.next;
                currentSize--;
                return current.data;
            }
            previous = current;
            current = current.next;
        }
        // 지우고자 하는 데이터가 없을 경우
        return null;
    }

    /*
    * Comparable 인터페이스를 사용하여 노드를 찾는다.
    * 객체의 타입에 따라 실제로 compareTo() 메소드의 구현이 필요할 것이다.
    * 객체의 주소값 비교가 아닌, 객체가 가진 id와 같은 고유한 값으로 비교할 수 있게 한다.
    * */
    public boolean contains(E obj) { // find(), has() 등으로 함수를 네이밍 할 수 있다.
        Node <E> current = head;

        // 1. 빈 자료구조일 경우 while 구문을 거치지 않고 false 리턴
        while (current!=null) {
            if (((Comparable<E>) obj).compareTo(current.data) == 0) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /*
    * 하나의 요소를 살펴보기 위해 쓰는 메소드
    * */
    public E peekFirst() {
        if (head == null) return null;
        return head.data;
    }
    /*
    * O(1)의 상수 시간 복잡도를 가진다.
    *  */
    public E peekLast() {
        if (tail==null) return null;
        return tail.data;
    }
    /*
    * 처음부터 끝 노드까지 탐색하므로, O(n)의 시간 복잡도를 가진다.
    * */
    public E peekLastWithoutTail() {
        if (head==null) return null;
        Node <E> tmp = head;

        while (tmp.next!=null) {
            tmp = tmp.next;
        }
        return tmp.data;
    }
}