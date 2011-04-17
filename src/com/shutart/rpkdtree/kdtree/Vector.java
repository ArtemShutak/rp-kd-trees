package com.shutart.rpkdtree.kdtree;

public interface Vector {
	@Override
	boolean equals(Object vector);

	int size();
	
	double getKey(int index);
}
