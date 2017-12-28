import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
   private class Node {
      Item item;
      Node prev;
      Node next;
      Node(Item item) {
         this.item = item;
         this.prev = null;
         this.next = null;
      }
   }
   private Node head, tail;
   private int size;
   public Deque() {
      head = new Node(null);
      tail = new Node(null);
      head.next = tail;
      tail.prev = head;
      size = 0;
   }
   public boolean isEmpty() {
      return size == 0;
   }
   public int size() {
      return size;
   }
   public void addFirst(Item item) {
      if (item == null) {
         throw new IllegalArgumentException("Element e cannot be null.");
      }
      Node node = new Node(item);
      node.prev = head;
      node.next = head.next;
      head.next.prev = node;
      head.next = node;
      size++;
   }
   public void addLast(Item item) {
      if (item == null) {
         throw new IllegalArgumentException("Element e cannot be null.");
      }
      Node node = new Node(item);
      node.next = tail;
      node.prev = tail.prev;
      tail.prev.next = node;
      tail.prev = node;
      size++;
   }
   public Item removeFirst() {
      if (size == 0) {
         throw new NoSuchElementException("Deque is empty.");
      }
      Item item = head.next.item;
      head.next = head.next.next;
      head.next.prev = head;
      size--;
      return item;
   }
   public Item removeLast() {
      if (size == 0) {
         throw new NoSuchElementException("Deque is empty.");
      }
      Item item = tail.prev.item;
      tail.prev = tail.prev.prev;
      tail.prev.next = tail;
      size--;
      return item;
   }
   @Override
   public Iterator<Item> iterator() {
      return new HeadFirstIterator();
   }

   private class HeadFirstIterator implements Iterator<Item> {
      private Node curr = head;

      @Override
      public boolean hasNext() {
         return curr.next != tail;
      }

      @Override
      public Item next() {
         if (!hasNext()) {
            throw new NoSuchElementException("No more item.");
         }
         curr = curr.next;
         return curr.item;
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException("Remove unsupported.");
      }
   }
}