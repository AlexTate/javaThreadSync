import java.util.Date;
import java.util.Random;
import java.lang.Thread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsumerThread extends Thread {
	
	private static Random rand = new Random();			//For generating random wait times between consumes
	public static BoundedBuffer buff;				//The bounded buffer (circular FIFIO)
	public static int bufferSize;					//Size of buffer (surprise)
	public static int maxWait;					//Maximum time to wait between consumption attempts
	private int waitCount = 0;					//For tracking 10x max wait limitation
	private int id;
	
	
	//==================================================
	//=== CONSTRUCTOR ==================================
	//==================================================
	
	/* ARGUMENTS:
	 * 1. Buffer size
	 * 2. Maximum wait time
	 */

	public ConsumerThread(int inBuff, int inMax) {
		bufferSize	= inBuff;
		maxWait		= inMax;
	}
	
	//==================================================
	//=== PUBLIC METHODS ===============================
	//==================================================
	
	@Override
	public void run() {		
		try {			
		
		while (waitCount < 10) {					//Loop until 10x maxWait
			synchronized(buff) {
				
				int[] result = buff.fetch();			//result[0] = position, result[1] = fetched value				
				if (result[0] == -1) {				//The buffer was empty
					
					//Prints only once per successive failure
					if (waitCount == 0) System.out.printf("Consumer %2d: Unable to consume, buffer empty, at Time: %s\n", id, timeStamp());
					
					buff.wait(genWait());			//Wait [1,maxWait] ms
					waitCount++;
									
				} else {
					
					System.out.printf("Consumer %2d: Removed %2d from Location: %2d at Time: %s\n", 
										id, result[1], result[0], timeStamp());	
					
					buff.notifyAll();			//Wake all other waiting threads now that we're out of critical section
					waitCount = 0;				//Fetch was successful, reset the 10-wait counter
					//buff.print();				//Print contents of buffer, nicely (useful for debugging)
					
				}
			}
			
			Thread.sleep(genWait());				//Wait a moment to simulate uncertainty in timing
		}
		
		System.out.printf("Consumer %2d: terminated after 10 times its maximum waiting time at Time: %s\n", id, timeStamp());
		return;
			
		} catch (InterruptedException ie) {
            		System.out.printf("\tInterrupted! Consumer ID reporting: %d", this.id);
		}
	}
	
	public static void assignBuffer(BoundedBuffer in) {
		buff = in;
	}
	
	public void assignId(int newId) {
		this.id = newId;
	}
	
	public static String timeStamp() {
		DateFormat stamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSS");
		return stamp.format(new Date());
	}
	
	//==================================================
	//=== PRIVATE METHODS ==============================
	//==================================================
	
	private static int genWait() {
		return rand.nextInt(maxWait) + 1;				//Range [1, maxWait], Units: milliseconds
	}

}
