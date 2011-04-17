package com.shutart.rpkdtree.tests.kdtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.shutart.rpkdtree.kdtree.FixedSizePriorityQueueByDistance;
import com.shutart.rpkdtree.kdtree.INode;
import com.shutart.rpkdtree.kdtree.Node;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.Vector;

public class NNSearcherFull {
	
	private final Collection<Vector> myVectors;
	private FixedSizePriorityQueueByDistance myNodesPriorQueue;

	public NNSearcherFull(Collection<Vector> index){
		myVectors = index;
	}
	
	List<INode> nnsearch(int numberOfNeighbors,Vector queryVector){
		myNodesPriorQueue = new FixedSizePriorityQueueByDistance(numberOfNeighbors, new Node(queryVector));
		for (Vector vector : myVectors) {
			myNodesPriorQueue.add(new Node(vector));
		}
		return myNodesPriorQueue.getNearestNeighbors();
		
	}
}
