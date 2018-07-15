import java.lang.Thread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/* ARGUMENTS:
 * 1. Buffer size
 * 2. Maximum wait time
 * 3. Number of items to be produced 
 */

public class ProducerThread extends Thread {
	
	private int id;						
	static int bufferSize;				
	static int maxWait;
	static int itemCount;								//Value is decremented appropriately, shared among all instances of ProducerThread
	static int total;									//Duplication of above value, since ProducerThreads collectively decrement itemCount
	static BoundedBuffer buff;
	static Random rand = new Random();
	
	//================================================
	//=== CONSTRUCTOR ================================
	//================================================
	
	public ProducerThread(int inBuff, int inWait, int inCount) {
		bufferSize	= inBuff;
		maxWait		= inWait;
		itemCount	= inCount;
		total		= itemCount;						//Make a copy for completion message
	}
	
	//================================================
	//=== PUBLIC METHODS =============================
	//================================================
	
	@Override
	public void run() {
		try {
						
		while(itemCount > 0) {
			synchronized(buff) {
				
				int value = genVal();					//Generate a random value [0, 100)
				int location = buff.put(value);			//Places value at end of queue and returns its index in the buffer
				
				if (location == -1) {					//Buffer was full
					
					System.out.printf("Producer %2d: Unable to insert, buffer full, at Time: %s\n", id, timeStamp());
					buff.wait(genWait());				//Wait [1, maxWait] 
					
				} else {
					
					--itemCount;						//Decrement remaining items, shared across all instances (therefore needed before waking other Producers)
					buff.notifyAll();					//Wake all waiting threads now that we're out of critical section
					//buff.print();						//Print contents of buffer, nicely (for debugging)
					
					System.out.printf("Producer %2d: %-7s %2d %-14s %2d at Time: %s\n",
										id, "Placed", value, "at Location:", location, timeStamp());					
					
					if (itemCount == 0) {				//All items have been produced
						System.out.printf("Producers: Finished producing %d items\n", total);	//Will only print once
						return;
					}					
				}
			}
			
			Thread.sleep(genWait());					//Wait a moment to simulate uncertainty in timing
		}
			
		} catch (InterruptedException ie) {
            System.out.printf("\tInterrupted! Producer ID reporting: %d\n", this.id);
		}
	}
	
	public void assignId(int newId) {
		this.id = newId;
	}
	
	public static void assignBuffer(BoundedBuffer in) {
		buff = in;
	}
	
	public static String timeStamp() {
		DateFormat stamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");
		return stamp.format(new Date());
	}
	
	//=== PRIVATE METHODS ================================
	
	private static int genWait() {
		return rand.nextInt(maxWait) + 1;	//Range [1, maxWait]
	}
	
	private static int genVal() {
		return rand.nextInt(100);			//Range [0, 100)
	}
}
