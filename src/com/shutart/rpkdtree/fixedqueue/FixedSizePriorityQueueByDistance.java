package com.shutart.rpkdtree.fixedqueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.shutart.rpkdtree.exceptions.MyException;
import com.shutart.rpkdtree.kdtree.INode;
import com.shutart.rpkdtree.kdtree.Vector;

public class FixedSizePriorityQueueByDistance extends FixedSizeVecPriorQueue {	
	
	public FixedSizePriorityQueueByDistance(int numberOfNeighbors,INode queryNode) {
		super(numberOfNeighbors, queryNode.getVector());
	}

	public void add(INode node) {
		add(node.getVector());
	}

}
