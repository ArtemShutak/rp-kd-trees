package com.shutart.rpkdtree.kdtree;

import java.util.Arrays;

public class VectorI implements Vector {
	private final double[] myKeys;

	public VectorI(double... keys) {
		myKeys = keys;
	}

	@Override
	public boolean equals(Object vector) {
		if(! (vector instanceof Vector)){
			throw new IllegalArgumentException();
		}
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

	 
	 
	@Override
	public double distance(Vector vector) {
		double dist = 0;
		for (int i = 0; i < this.size(); i++) {
			dist+= f_i(i, vector.getKey(i));
			//dist+= Math.pow(this.getKey(i)-node.getKey(i), 2);
		}
		return VectorI.dissim(dist) ;
	}
	
	/**
	 * This is a F method from paper. And dissimilarity measure (in paper egn 14)
	 * @param sum
	 * @return
	 */
	public static double dissim(double sum) {
		return Math.sqrt(sum);
	}
	@Override
	public double coordinateDistance(int numberOfCoord, double value2) {
		return Math.abs(this.getKey(numberOfCoord)-value2);//==dissim(f_i(...))
	}

	/**
	 * This is a f_i method from paper
	 * @param numberOfCoord - is i
	 * @param value1
	 * @param value2
	 * @return
	 */
	public static double f_i(int numberOfCoord, double value1, double value2) {
		return Math.pow(value1-value2, 2);
	}

	@Override
	public double f_i(int numberOfCoord, double value2) {
		return f_i(numberOfCoord, this.getKey(numberOfCoord), value2);
	}
	public int hashCode(){
		int hash = 0;
		for (int i = 0; i < myKeys.length; i++) {
			hash+=((Double)myKeys[i]).hashCode();
		}
		return hash;
	}

}
