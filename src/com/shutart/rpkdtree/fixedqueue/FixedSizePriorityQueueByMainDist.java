package com.shutart.rpkdtree.fixedqueue;

import java.util.Collection;
import java.util.Collections;

import com.shutart.rpkdtree.exceptions.MyException;
import com.shutart.rpkdtree.kdtree.INode;

public class FixedSizePriorityQueueByMainDist extends FixedSizePriorityQueueByDistance {

	public FixedSizePriorityQueueByMainDist(int numberOfNeighbors, INode queryNode) {
		super(numberOfNeighbors, queryNode);
		// TODO Auto-generated constructor stub
	}
	
	public boolean add(INode node) {
		boolean res = myNodesWithDistance.add(new NodeAndMainDist(node, myQueryNode));
		if(myNodesWithDistance.size()>myNumberOfNeighbors){
			myNodesWithDistance.poll();
		}
		return res;
	}
	
	public void addAll(Collection<INode> nodes){
		for (INode iNode : nodes) {
			this.add(iNode);
		}
	}

}
