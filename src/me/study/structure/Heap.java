package me.study.structure;

public class Heap<E> {

    int lastPosition; // 힙의 마지막 노드 위치
    int size;
    E[] array = (E[]) new Object[size];

    // 생성자 구현 필요

    // 새로운 노드 추가
    public void add(E obj) {
        array[++lastPosition] = obj;
        trickleUp(lastPosition);
    }

    /*
    * 스왑 알고리즘 중 간단하며 효율적인 방식으로 구현하였다.
    * */
    public void swap(int from, int to) {
        E tmp = array[from];
        array[from] = array[to];
        array[to] = tmp;
    }

    /*
    * 재귀적 형태로 trickleUp을 활용해 힙의 노드 추가 기능를 제공할 수 있다.
    * trickleUp의 탈출 조건은 루트 노드에 도달한 경우와
    * 추가된 요소가 부모 요소보다 작은 경우의 2가지가 존재한다.
    * */
    public void trickleUp(int position) {
        // 루트 노드에 도달한 경우, escape!
        if (position == 0) return;
        int parent = (int) Math.floor((position-1)/2); // 부모 노드 위치

        // 추가된 요소가 부모보다 큰 경우, 값을 스왑하고, 재귀적으로 trickleUp(노드 위치 변경)을 수행한다.
        if (((Comparable <E>) array[position]).compareTo(array[parent]) > 0) {
            swap(position, parent);
            trickleUp(parent);
        }
    }

    /*
    * 루트 노드 삭제
    * */
    public E remove() {
        E tmp = array[0];
        // 루트와 마지막 노드를 바꾼 다음, lastPosition의 크기를 감소시킨다.
        swap(0, lastPosition--);
        trickleDown(0);
        return tmp;
    }

    /*
    * 재귀적 형태로 trickleDown을 활용해 힙의 노드 삭제 기능을 제공할 수 있다.
    * */
    public void trickleDown(int parent) {
        int left = 2 * parent + 1;
        int right = 2 * parent + 2;

        /*
        * Edge Case 1.
        * 자식 노드가 마지막 노드이며, 자식 노드가 더 큰 경우
        * */
        if (left==lastPosition && (( (Comparable<E>) array[parent]).compareTo(array[left])<0)) {
            swap(parent, left);
            return;
        }
        if (right==lastPosition && (((Comparable<E>)array[parent]).compareTo(array[right])<0)) {
            swap(parent, right);
            return;
        }

        /*
         * Edge Case 2.
         * 위 조건에 해당하지 않고(자식 노드가 마지막 노드이나, 부모 노드가 더 큰 경우),
         * 자식 노드 위치가 마지막 노드 위치보다 큰 값을 가지는 (자식 노드가 더 이상 존재하지 않는는 경우
         * */
        if (left >= lastPosition || right >= lastPosition) return;

        /*
         * 이하 더 이상 자식 노드가 마지막 노드보다 크거나 같은 값이 아닐 것이므로,
         * 왼쪽 노드와 오른쪽 노드 중 큰 값을 가진 자식이 부모 노드보다 크다면, 부모 노드와 스왑한다.
         * 이를 재귀적으로 수행한다.
         * */
        if ( ((Comparable<E>) array[left]).compareTo(array[right]) > 0 && ((Comparable<E>) array[left]).compareTo(array[parent]) > 0 ) {
            swap(parent, left);
            trickleDown(left);
        }
        else if ( ((Comparable<E>) array[right]).compareTo(array[parent]) > 0 ) {
            swap(parent, right);
            trickleDown(right);
        }
    }
}
