//package nz.ac.vuw.mcs.comp103.as08a;

/**
	* Class to facilitate taking timings within programs.
	* An object of the <code>StopWatch</code> class keeps a time total
	* in milliseconds. Each time the StopWatch is stopped with the
	* stop() method, the total is increased by the amount of time ellapsed
	* since it was last started. Thus a single stopwatch instance may be
	* used to acumlate the time spent in any particular method - simply
	* start the stopwatch before entry to the method of interest and stop 
	* it when the method exits. 
	*/
public class StopWatch {
	long total;
	boolean running;
	long started;

	/**
		* create a new <code>StopWatch</code> object.
		* totals time is initially 0 and status is set to not running.
		*/
	StopWatch() {
		total = 0;
		running = false;
		started = 0;
	}

	/**
		* start this stopwatch.
		* If this stopwatch is not already running, record the current time.
		* If the stopwatch is already running, do nothing.
		*/
	void start() {
		if (!running) {
			started = System.currentTimeMillis();
			running = true;
		}
	}

	/**
		* stop this stopwatch.
		* If the stopwatch is running, add the ellapsed time to the total.
		* and set the status to not running.
		* If the stopwatch is not running, do nothing.
		*/
	void stop() {
		if (running) {
			long stopped = System.currentTimeMillis();
			running = false;
			total += stopped - started;
		}
	}

	/**
		* fetch the total time recorded on this stopwatch
		* 
		* @return the total number of milliseconds this stop watch has been 
		*				 allowed to run for.
		*/
	long getTotal() {
		return (total);
	}

	/**
		* reset this stopwatch.
		* the total time is set to 0 and the status is set to not running.
		*/
	void reset() {
		total = 0;
		running = false;
	}
}
