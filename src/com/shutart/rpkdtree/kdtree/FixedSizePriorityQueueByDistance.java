package com.shutart.rpkdtree.kdtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SortedSet;

import com.shutart.rpkdtree.exceptions.MyException;

public class FixedSizePriorityQueueByDistance {
	private final INode myQueryNode;
	/**
	 * Priority Queue by Distance from myQueryNode. This queue contains no more than myNumberOfNeighbors elements.
	 * The head element is node, which have the LARGEST distance from queryNode.
	 */
	private final PriorityQueue<NodeAndDistance> myNodesWithDistance;
	private final int myNumberOfNeighbors;

	public FixedSizePriorityQueueByDistance(int numberOfNeighbors,INode queryNode) {
		myQueryNode = queryNode;
		myNumberOfNeighbors = numberOfNeighbors;
		myNodesWithDistance = new PriorityQueue<NodeAndDistance>(myNumberOfNeighbors+1, NodeAndDistance.comparator());
	}

	public boolean add(INode node) {
		boolean res = myNodesWithDistance.add(new NodeAndDistance(node, node.distance(myQueryNode)));
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
	
	public static class NodeAndDistance {
		private final INode myNode;
		private final double myDistance;

		public NodeAndDistance(INode node, double distance) {
			myNode = node;
			myDistance = distance;
		}

		public static Comparator<NodeAndDistance> comparator() {
			return new Comparator<NodeAndDistance>() {
				@Override
				public int compare(NodeAndDistance o1, NodeAndDistance o2) {
					return (int) (o2.myDistance - o1.myDistance);
				}
			};
		}

		public INode getNode() {
			return myNode;
		}

		public double getDistance() {
			return myDistance;
		}

	}

	public int size() {
		return myNodesWithDistance.size();
	}

}
