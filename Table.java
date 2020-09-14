import java.time.Duration;
import java.time.Instant;

public class Table {

	public static void main(String[] args) {

		int m =3000;

		FibonacciHeap tree = new FibonacciHeap();
		
		Instant start = Instant.now();
		
		for(int i = m; i>=1;i--) {
	        tree.insert(i);
		}
		for(int j = 0; j<=m/2;j++) {
	        tree.deleteMin();
		}
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");

		System.out.println("tree after insert and deletion : ");
		//tree.print();
		System.out.println("totalCuts : " + FibonacciHeap.totalCuts());
		System.out.println("totalLinks : " + FibonacciHeap.totalLinks());
		System.out.println("potential : " + tree.potential());
	}

}
