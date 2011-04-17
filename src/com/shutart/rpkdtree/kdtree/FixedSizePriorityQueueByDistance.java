package com.shutart.rpkdtree.kdtree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.SortedSet;

public class FixedSizePriorityQueueByDistance {
	private final INode myQueryNode;
	private final PriorityQueue<NodeAndDistanse> myNodesWithDistance;
	private final int myNumberOfNeighbors;

	public FixedSizePriorityQueueByDistance(int numberOfNeighbors,INode queryNode) {
		myQueryNode = queryNode;
		myNumberOfNeighbors = numberOfNeighbors;
		myNodesWithDistance = new PriorityQueue<NodeAndDistanse>(myNumberOfNeighbors, NodeAndDistanse.comparator());
	}

	public boolean add(INode node) {
		return myNodesWithDistance.add(new NodeAndDistanse(node, node.distance(myQueryNode)));
	}

	public List<INode> getNearestNeighbors() {
		List<INode> res = new ArrayList<INode>();
		int counter = 0;
		for (NodeAndDistanse nodeAndDist : myNodesWithDistance) {
			res.add(nodeAndDist.getNode());
			counter++;
			if(counter == myNumberOfNeighbors){
				return res;
			}
		}
		return res;
	}
	
	public double getPQD1() {
		return myNodesWithDistance.peek().getDistance();
	}
	
	public static class NodeAndDistanse {
		private final INode myNode;
		private final double myDistance;

		public NodeAndDistanse(INode node, double distance) {
			myNode = node;
			myDistance = distance;
		}

		public static Comparator<NodeAndDistanse> comparator() {
			return new Comparator<NodeAndDistanse>() {
				@Override
				public int compare(NodeAndDistanse o1, NodeAndDistanse o2) {
					return (int) (o1.myDistance - o2.myDistance);
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

}
