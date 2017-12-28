import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
   private Item[] queue;
   private int last;
   private int n;

   public RandomizedQueue() {
      queue = (Item []) new Object[1];
      last = 0;
      n = 0;
   }

   public boolean isEmpty() {
      return n == 0;
   }

   public int size() {
      return n;
   }

   private void resize(int length) {
      Item[] newQueue = (Item []) new Object[length];
      int count = 0;
      for (int i = 0; i < last; i++) {
         newQueue[count++] = queue[i];
      }
      queue = newQueue;
      last = n;
   }

   public void enqueue(Item item) {
      if (item == null) throw new IllegalArgumentException();

      if (last == queue.length) resize(queue.length * 2);
      queue[last++] = item;
      n++;
   }

   public Item dequeue() {
      Item item;
      if (isEmpty()) throw new NoSuchElementException();
      if (size() == queue.length / 4) resize(queue.length / 2);
      int choice = StdRandom.uniform(0, n);
      item = queue[choice];
      queue[choice] = queue[last - 1];
      queue[--last] = null;
      n--;
      return item;
   }

   public Item sample() {
      if (isEmpty()) throw new NoSuchElementException();
      int choice = StdRandom.uniform(0, n);
      return queue[choice];
   }

   private class RandomizedQueueIterator implements Iterator<Item> {
      private int index;
      private Item[] copyQueue;

      public RandomizedQueueIterator() {
         copyQueue = (Item []) new Object[n];
         int count = 0;
         for (int i = 0; i < last; i++) {
            copyQueue[count++] = queue[i];
         }
         index = 0;
         StdRandom.shuffle(copyQueue);
      }

      @Override
      public boolean hasNext() {
         return index != n;
      }

      @Override
      public Item next() {
         if (!hasNext()) throw new NoSuchElementException();
         return copyQueue[index++];
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   public Iterator<Item> iterator() {
      return new RandomizedQueueIterator();
   }
}
