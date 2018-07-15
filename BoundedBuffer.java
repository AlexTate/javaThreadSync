import java.util.Arrays;

//Circular buffer
//FIFO
//Access is synchronized

public class BoundedBuffer {
	
	int bufferSize;
	int[] buffer;
	int head = 0;	//Describes the current index of the start of the FIFO queue
	int depth = 0;	//Describes the length of the queue
	
	BoundedBuffer(int size){
		bufferSize = size;
		buffer = new int[bufferSize];
		Arrays.fill(buffer, -1);	//Fills unused indexes with illegal value. Useful only for print()
	}
	
	public synchronized int[] fetch() throws InterruptedException {
		int[] result = new int[2];
		//result[0] = position
		//result[1] = value
		
		if (depth == 0){
			//Queue is empty, return illegal position
			result[0] = -1;				
		} else {
			result[0] = head;					//Copy the position of the value fetched (front of queue)
			result[1] = buffer[head];				//Copy value
			buffer[head] = -1;					//Assign an illegal value to the fetched position (for debugging)
			if (++head == bufferSize) head = 0;			//Advance head past fetched value, maintain circular queue
			--depth;						//Decrease queue depth to account for removed element
		}
		
		return result;							//Return position of fetched value, and the value
	}
	
	public synchronized int put(int inVal) throws InterruptedException {
		int placedLoc;
		
		if (depth == bufferSize) {
			//Queue is full, return illegal position
			placedLoc = -1;				
		} else {
			int idx = (head + depth) % bufferSize;			//Get next open index in circular FIFO queue
			buffer[idx] = inVal;					//Assign value to index
			++depth;						//Increment queue depth to account for new value
			
			placedLoc = idx;
		}
		
		return placedLoc;
	}
	
	//Print the contents of the buffer somewhat nicely for debugging
	public void print() {
		for (int i = 0; i < buffer.length; i++) {
			System.out.printf(" | %2d", buffer[i]);
		}
		
		System.out.println(" |");
	}
}
