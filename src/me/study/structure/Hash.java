package me.study.structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

// 테스트 수행 전이며, 미완성, 실습용 코드
public class Hash<K, V> implements HashI<K,V> {

    /*
    * 해시 요소의 비교를 위해 Comparable을 구현한다.
    * */
    class HashElement<K, V> implements Comparable<HashElement<K,V>> {
        K key;
        V value;
        public HashElement(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(HashElement<K, V> e) {
            return (((Comparable<K>) this.key).compareTo(e.key));
        }
    }

    /*
    * Iterator 인터페이스를 구현한 내부 클래스 IteratorHelper
    * 키의 순서는 보장되지 않는다. 입력될 때부터 해싱 인덱스를 활용했기에, 순서가 없다.
    * 시간 복잡도는 O(n)이다.
    * */
    class IteratorHelper<T> implements Iterator<T> {
        int position; // 탐색 중, 현재 위치를 알기 위해 선언
        T[] keys;

        public IteratorHelper() { // 생성자
            position = 0;
            keys = (T[]) new Object[numElements];

            int p = 0;
            for (int i=0; i<tableSize; i++) {
                HashListI<HashElement<K, V>> list = hashArray[i];
                for (HashElement<K, V> he : list) {
                    keys[p++] = (T) he.key;
                }
            }
        }
        @Override
        public boolean hasNext() {
            return position < keys.length;
        }
        @Override
        public T next() {
            if (!hasNext())  throw new NoSuchElementException(); // or return null;
            return keys[position++];
        }
    }
    // Iterable 인터페이스(ListI, HashListI 인터페이스가 상속)의 구현 메소드
    @Override
    public Iterator<K> iterator() {
        return new IteratorHelper();
    }

    int numElements, tableSize;
    double maxLoadFactor; // 최대 적재율, 테이블 크기 조정 기준
    HashListI<HashElement<K,V>>[] hashArray;
    //다형성을 위해 인터페이스 활용, LinkedList<HashElement<K,V>>[] hashArray;

    public Hash(int tableSize) {
        this.tableSize = tableSize;
        maxLoadFactor = 0.75; // 자바 api 에서도 .75를 기준으로 한다. 일반적으로 해시에 적절한 기준이다.
        numElements = 0;

        // 배열을 만들 때, 바로 제네릭 타입을 사용하지 못한다. (타입 소거)
        // 제네릭 타입의 연결리스트 배열을 만들기 위해, 객체로 배열을 만들고 형변환을 실행한다.
        this.hashArray = (LinkedList<HashElement<K,V>>[]) new LinkedList[tableSize];
        for (int i = 0; i < tableSize; i++) {
            // 배열 전체에 미리 연결리스트 객체를 선언, 할당해둔다.
            // 요소를 삽입, 삭제, 조회할 때 리스트가 생성되어 있는지 매번 확인할 필요가 없어진다.
            hashArray[i] = new LinkedList<HashElement<K,V>>();
        }

    }

    public boolean add(K key, V value) {
        if ( loadFactor() > maxLoadFactor ) resize(tableSize*2); // 필요시 리사이징
        HashElement<K, V> he = new HashElement<>(key, value); // 삽입할 요소 생성

        // 해싱을 통해 인덱스 생성
        int hashval = key.hashCode();
        hashval = hashval & 0x7fffffff; // 음수일 경우, 2의 보수를 이용해 양수로 변환
        hashval = hashval % tableSize; // 테이블 크기 내 인덱스 값 계산

        // 연결리스트에 요소 추가, 보통 한 두개의 요소가 포함될 것이므로 삽입 순서는 앞, 뒤 관계 없이 쓸 수 있다.
        hashArray[hashval].addFirst(he);
        numElements++;
        return true;
    }

    public boolean remove(K key) {
        HashElement<K, V> he = new HashElement<>(key, null); // 삭제할 요소 생성

        // 해싱을 통해 인덱스 확인
        // 해싱 -> 양수로 변환 -> 테이블 크기 내 인덱스 값 계산
        int hashval = key.hashCode() & 0x7fffffff % tableSize;

        // 연결리스트에서 요소 삭제
        // 내부적으로 hashElement의 compareTo() 메소드를 이용해 값을 비교하고 삭제될 것
        hashArray[hashval].remove(he);
        numElements--;
        return true;
    }

    public V getValue(K key) {
        // 해싱을 통해 인덱스 확인
        // 해싱 -> 양수로 변환 -> 테이블 크기 내 인덱스 값 계산
        int hashval = key.hashCode() & 0x7fffffff % tableSize;

        // 연결 리스트 내에 같은 키를 가진 요소 확인인
       for (HashElement<K, V> he : hashArray[hashval]) { // Iterable & Iterator
            if (((Comparable<K>) key).compareTo(he.key) == 0) return he.value; // Comparable
        }
        return null;
    }

    /*
    * 해시 테이블을 리사이즈 하기 위해서는
    * 새로운 연결 리스트 배열을 만들고, 해시의 모든 연결 리스트에 있는 요소의 키와 값을 각각 찾아서 옮겨야 한다.
    * 복잡도, 비용이 크다. 체이닝의 단점이다.
    * */
    private void resize(int newSize) {
        // 리사이즈 된 배열 생성
        HashListI<HashElement<K,V>>[] newArray = (LinkedList<HashElement<K,V>>[]) new LinkedList[newSize];

        // 배열 전체에 미리 연결리스트 객체를 선언, 할당해준다.
        for (int i = 0; i < newSize; i++) newArray[i] = new LinkedList<HashElement<K,V>>();

        // 모든 요소를 조회하여, 새로운 체인 해시 내 연결리스트에 담는다.
        for (K key : this) {
            V value = getValue(key);
            HashElement<K, V> he = new HashElement<>(key, value);
            int hashval = key.hashCode() & 0x7fffffff % newSize;
            newArray[hashval].addFirst(he);
        }
        hashArray = newArray;
        tableSize = newSize;
    }

    private int loadFactor() {
        return 0;
    }


    public int hashCode(String s) {
        int g = 31; //임의의 값
        int hash = 0;

        for (int i = 0, len = s.length(); i < len; i++) {
            hash = g * hash + s.charAt(i);
        }
        return hash;
    }
}
