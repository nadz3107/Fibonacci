
public class Main {

	public static void main(String[] args) {
		
		  FibonacciHeap t = new FibonacciHeap();
	        t.insert(5);
	        t.insert(1);
	        t.insert(3);
	        t.insert(8);
	        t.insert(9);
	        t.insert(10);
	        t.print();
	        System.out.println("Min:" + t.findMin().key);
	        t.deleteMin();
	        t.print();
		
		testMinimum();
        testDelete();
        testDecreaseKey();
        System.out.println("Tests passed");

	}
	
	  private static void testMinimum() {
	        FibonacciHeap t = new FibonacciHeap();
	        t.insert(5);
	        t.insert(1);
	        t.insert(3);
	        t.insert(8);
	        t.insert(7);
	        t.insert(4);
	        t.insert(9);
	        t.insert(2);
	        t.insert(6);
	        for (int i = 1; i <= 9; i++) {
	            assert(t.findMin().getKey() == i);
	            t.deleteMin();
	        }
	        assert(t.empty());
	        System.out.println("testMinimum passed");
	    }

	    private static void testDelete() {
	        FibonacciHeap t = new FibonacciHeap();
	        FibonacciHeap.HeapNode node5 = t.insert(5);
	        FibonacciHeap.HeapNode node1 = t.insert(1);
	        FibonacciHeap.HeapNode node3 = t.insert(3);
	        FibonacciHeap.HeapNode node4 = t.insert(4);
	        FibonacciHeap.HeapNode node2 = t.insert(2);

	        t.delete(node4);
	        t.print();
	        t.delete(node1);
	        t.print();
	        assert(t.findMin() == node2);

	        t.delete(node2);
	        assert(t.findMin() == node3);

	        t.delete(node3);
	        assert(t.findMin() == node5);
	        System.out.println("testDelete passed");
	    }

	    private static void testDecreaseKey() {
	        FibonacciHeap t = new FibonacciHeap();
	        FibonacciHeap.HeapNode node5 = t.insert(5);
	        FibonacciHeap.HeapNode node3 = t.insert(3);
	        FibonacciHeap.HeapNode node4 = t.insert(4);
	        FibonacciHeap.HeapNode node2 = t.insert(2);

	        t.decreaseKey(node5, 1);
	        assert(t.findMin() == node5);
	        t.decreaseKey(node5, 0);
	        assert(t.findMin() == node5);
	        t.decreaseKey(node3, 2);
	        assert(t.findMin() == node3);
	        System.out.println("testDecreaseKey passed");
	    }

}
