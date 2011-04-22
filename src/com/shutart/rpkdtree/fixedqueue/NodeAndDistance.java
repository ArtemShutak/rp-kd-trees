package com.shutart.rpkdtree.fixedqueue;

import java.util.Comparator;

import com.shutart.rpkdtree.kdtree.INode;

public class NodeAndDistance{
	private final INode myNode;
	private final double myDistance;

	protected NodeAndDistance(INode node, double distance) {
		myNode = node;
		myDistance = distance;
	}
	
	public NodeAndDistance(INode node, INode queryNode) {
		this(node,node.distance(queryNode));
	}


	public INode getNode() {
		return myNode;
	}

	public double getDistance() {
		return myDistance;
	}

	public static Comparator<NodeAndDistance> comparator() {
		return new Comparator<NodeAndDistance>() {
			@Override
			public int compare(NodeAndDistance o1, NodeAndDistance o2) {
				return (int) (o2.myDistance - o1.myDistance);
			}
		};
	}

}
