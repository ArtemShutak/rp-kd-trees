package com.shutart.rpkdtree.kdtree;

public interface Vector {
	@Override
	boolean equals(Object vector);

	int size();
	
	double getKey(int index);
	
	double distance(Vector vector);
	double coordinateDistance(int numberOfCoord, double value2);
	double f_i(int numberOfCoord, double value2);
}
