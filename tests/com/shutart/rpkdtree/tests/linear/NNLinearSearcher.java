package com.shutart.rpkdtree.tests.linear;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.shutart.rpkdtree.fixedqueue.FixedSizePriorityQueueByDistance;
import com.shutart.rpkdtree.fixedqueue.VecAndDist;
import com.shutart.rpkdtree.fixedqueue.FixedSizeVecPriorQueue;
import com.shutart.rpkdtree.kdtree.INode;
import com.shutart.rpkdtree.kdtree.Node;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.Vector;

public class NNLinearSearcher {
	
	private final Collection<Vector> myVectors;
	private FixedSizeVecPriorQueue myVectorsPriorQueue;

	public NNLinearSearcher(Collection<Vector> corpus){
		myVectors = corpus;
	}
	
	public List<Vector> nnsearch(int numberOfNeighbors,Vector queryVector){
		myVectorsPriorQueue = new FixedSizeVecPriorQueue(numberOfNeighbors, queryVector);
		for (Vector vector : myVectors) {
			myVectorsPriorQueue.add(vector);
		}
		return myVectorsPriorQueue.getNearestNeighbors();
		
	}
}
