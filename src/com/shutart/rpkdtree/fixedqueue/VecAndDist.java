package com.shutart.rpkdtree.fixedqueue;

import java.util.Comparator;

import com.shutart.rpkdtree.kdtree.Vector;


public class VecAndDist {
	private final Vector myVector;
	private final double myDistance;

	protected VecAndDist(Vector vector, double distance) {
		myVector = vector;
		myDistance = distance;
	}

	public Vector getVector() {
		return myVector;
	}
	
	@Override
	public boolean equals(Object vecAndDist){
		if(!(vecAndDist instanceof VecAndDist)){
			throw new IllegalArgumentException();
		}
		return myVector.equals(((VecAndDist)vecAndDist).getVector());		
	}

	public double getDistance() {
		return myDistance;
	}

	public static Comparator<VecAndDist> comparator() {
		return new Comparator<VecAndDist>() {
			@Override
			public int compare(VecAndDist o1, VecAndDist o2) {
				return (int) (o2.myDistance - o1.myDistance);
			}
		};
	}

}
