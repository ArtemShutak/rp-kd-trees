package com.shutart.rpkdtree.kdtree;

import java.util.Arrays;

public class VectorI implements Vector {
	private final double[] myKeys;

	public VectorI(double[] keys) {
		myKeys = keys;
	}

	@Override
	public boolean equals(Vector vector) {
		return Arrays.equals(myKeys, ((VectorI)vector).myKeys);
	}

	@Override
	public int size() {
		return myKeys.length;
	}

	@Override
	public double getKey(int index) {
		return myKeys[index];
	}
	
	 public String toString(){
		 return Arrays.toString(myKeys);
	 }

}
