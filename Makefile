COMPILER= javac
JRE= java
FILES= HW6.java BoundedBuffer.java ConsumerThread.java ProducerThread.java
EXE= HW6
TAR= HW6.tar

all:
	$(COMPILER) $(FILES)
	
run:
	$(JRE) $(EXE) 10 100 20 5 5
	
clean:
	rm *.class
	
package:
	tar -cvf $(TAR) $(FILES) Makefile