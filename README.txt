Consumer-producer solution with multithreaded Java classes

Files:
-----------------------------------------------------------------------
HW6.java - This is the "parent program". It takes command line arguments for:
				1. Buffer size
				2. Maximum wait time 
				3. Number of items to be produced/consumed
				4. Number of producers
				5. Number of consumers
			It then initializes the appropriate numer of consumer and
			producer threads, assigns a shared buffer to them, starts
			the threads and waits for them to complete execution.
			
BoundedBuffer.java - This is the class containing the shared buffer. Items
			can be added (put()) and removed (fetch()) from the buffer
			in such a way that the critical section is respected.
			It takes the following arguments:
				1. Size of buffer

ConsumerThread.java - This is the class which removes items from the queue.
			It coordinates synchronization with the critical section through
			use of wait() and notify().
			It takes the following arguments:
				1. The size of the shared buffer
				2. The maximum wait time (in milliseconds, per TA Matt's post
					on Piazza) used for wait() synchronization
			
			
ProducerThread.java - This is the class which adds items to the queue.
			It coordinates synchronization with the critical section through
			use of wait() and notify().
			It takes the following arguments:
				1. The size of the shared buffer
				2. The maximum wait time (in milliseconds, per TA Matt's post
					on Piazza) used for wait() synchronization
				3. The number of items to be produced