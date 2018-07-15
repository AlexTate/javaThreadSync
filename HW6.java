
public class HW6 {
	public static int bufferSize;
	public static int maxWait;		//Units: milliseconds
	public static int itemCount;
	public static int numProd;
	public static int numCons;
	
	public static void main(String[] args) {
		/* ARGUMENTS:
		 * 1. Buffer size
		 * 2. Maximum wait time
		 * 3. Number of items to be produced/consumed
		 * 4. Number of producers
		 * 5. Number of consumers
		 */
		
		try {
			bufferSize	= Integer.parseInt(args[0]);
			maxWait		= Integer.parseInt(args[1]);
			itemCount	= Integer.parseInt(args[2]);
			numProd		= Integer.parseInt(args[3]);
			numCons		= Integer.parseInt(args[4]);
		} catch (Exception e) {
			System.out.println("Please check that you've used the correct number of integer arguments.");
			System.exit(1);
		}
		
		BoundedBuffer buff = new BoundedBuffer(bufferSize);
		ProducerThread[] producers = new ProducerThread[numProd];
		ConsumerThread[] consumers = new ConsumerThread[numCons];
		
		//Instantiate producers
		for (int i = 0; i < numProd; ++i) {
			producers[i] = new ProducerThread(bufferSize, maxWait, itemCount);
			producers[i].assignId(i);	//Give it an identifier
		}
		
		//Instantiate consumers
		for (int i = 0; i < numCons; ++i) {
			consumers[i] = new ConsumerThread(bufferSize, maxWait);
			consumers[i].assignId(i);	//Give it an identifier
		}
		
		//Assign shared buffer too all instances of ConsumerThread and ProducerThread
		ConsumerThread.assignBuffer(buff);
		ProducerThread.assignBuffer(buff);
		
		//Start all producers
		for (ProducerThread producer : producers) {
			producer.start();
		}
		
		//Start all consumers
		for (ConsumerThread consumer : consumers) {
			consumer.start();
		}
		
		//Wait for all producer and consumer threads to finish
		try {
			for (Thread producer : producers) producer.join();
			for (Thread consumer : consumers) consumer.join();
		} catch (InterruptedException ie) {
			System.out.println("Thread interrupt handled in HW6.\n");
		}
		
		System.out.println("Program is now terminating.");
	}
	
}


