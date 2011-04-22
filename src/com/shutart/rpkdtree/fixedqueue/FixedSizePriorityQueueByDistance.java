package com.shutart.rpkdtree.fixedqueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import com.shutart.rpkdtree.exceptions.MyException;
import com.shutart.rpkdtree.kdtree.INode;

public class FixedSizePriorityQueueByDistance {
	protected final INode myQueryNode;
	/**
	 * Priority Queue by Distance from myQueryNode. This queue contains no more than myNumberOfNeighbors elements.
	 * The head element is node, which have the LARGEST distance from queryNode.
	 */
	protected final PriorityQueue<NodeAndDistance> myNodesWithDistance;
	protected final int myNumberOfNeighbors;

	public FixedSizePriorityQueueByDistance(int numberOfNeighbors,INode queryNode) {
		myQueryNode = queryNode;
		myNumberOfNeighbors = numberOfNeighbors;
		myNodesWithDistance = new PriorityQueue<NodeAndDistance>(myNumberOfNeighbors+1, NodeAndDistance.comparator());
	}

	public boolean add(INode node) {
		boolean res = myNodesWithDistance.add(new NodeAndDistance(node, myQueryNode));
		if(myNodesWithDistance.size()>myNumberOfNeighbors){
			myNodesWithDistance.poll();
		}
		if(myNodesWithDistance.size()>myNumberOfNeighbors){
			throw new MyException();
		}
		return res;
	}

	public List<INode> getNearestNeighbors() {
		INode[] resArray = new INode[myNodesWithDistance.size()];
		for (int indexOfRes = resArray.length-1; indexOfRes > -1; indexOfRes--) {
			print(myNodesWithDistance.peek());
			resArray[indexOfRes]=myNodesWithDistance.poll().getNode();
		}
		System.out.println("      ");
		return Arrays.asList(resArray);
	}
	
	private void print(NodeAndDistance peek) {
		System.out.println("dist:" + peek.getDistance() + " Node:" + peek.getNode().getVector() );		
	}

	public double getPQD1() {
		return myNodesWithDistance.peek().getDistance();
	}

	public int size() {
		return myNodesWithDistance.size();
	}

}
