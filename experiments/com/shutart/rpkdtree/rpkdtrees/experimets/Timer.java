/**
 * 
 */
package com.shutart.rpkdtree.rpkdtrees.experimets;

/**
 * @author �����
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

	public long getTimeInSec() {
		return (myStopTime-myStartTime)/1000;
	}
	
	
	

}
