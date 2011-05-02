/**
 * 
 */
package com.shutart.rpkdtree.rpkdtrees.experimets;

/**
 * @author Артем
 *
 */
public class Timer {
	private long myStartTime;
	private long myStopTime;

	public void run() {
		myStartTime = System.currentTimeMillis();		
	}

	public void stop() {
		myStopTime = System.currentTimeMillis();
	}

	public long getTime() {
		return (myStopTime-myStartTime);
	}
	
	
	

}
