package me.study.structure;

public class Tree<E> {

    Node<E> root;
    int currentSize;

    class Node<E> {
        E data;
        Node <E> left, right; // 노드의 포인터, 더하여 parent 포인터를 추가할 수 있다.

        public Node(E obj) {
            this.data = obj;
            left=right=null;
        }
    }

    /*
    * 외부에 공개하는 add 메소드, 오버로딩 방식
    * 직접적으로 트리 노드에 접근하여 수정하는 메소드는 내부에 감춰둔다.
    * */
    public void add(E obj){

        if (root == null) { // 트리가 비어있을 경우
            root = new Node<E>(obj);
        } else {
            add(obj, root);
        }
        currentSize++;
    }

    /*
    * 트리에 요소를 추가하는 add 메소드
    * 트리 내부를 탐색하며 삽입할 위치를 찾아야 하므로, 재귀함수로 구현한다.
    * 더하여, 트리 내부를 탐색하는 로직은 트리 클래스 내 모든 메소드들에서 동일하게 만든다. (일관성 유지)
    * */
    private void add (E obj, Node<E> node) {
        if (((Comparable<E>) obj).compareTo(node.data) > 0) {
            // if obj > node.data, go to the right
            if (node.right == null) {
                node.right = new Node<E>(obj);
                return;
            }
            add(obj, node.right);
        }
        // if obj <= node.data, go to the left
        if(node.left == null) {
            node.left = new Node<E>(obj);
            return;
        }
        add(obj, node.left);
    }

    /*
    * 사용자들이 접근할 수 있도록 오버로딩한 public method
    * */
    public boolean contains (E obj){
        return contains(obj, root);
    }

    /*
    * 외부에서 실제 로직과 노드에 접근하지 못하도록 은닉된 private method
    * */
    private boolean contains (E obj, Node<E> node){
        // 트리의 끝에 도달한 경우
        if (node==null) return false;
        // node의 data가 찾는 값과 일치하는 경우
        if (((Comparable<E>) obj).compareTo(node.data) == 0) return true;

        // node의 data가 찾는 값보다 작은 경우, go to the right
        if (((Comparable<E>) obj).compareTo(node.data) > 0) return contains(obj, node.right);
        // else, go to the left
        return contains(obj, node.left);
    }
}
