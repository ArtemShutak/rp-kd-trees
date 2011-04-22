package com.shutart.rpkdtree.fixedqueue;

import com.shutart.rpkdtree.kdtree.INode;

public class NodeAndMainDist extends NodeAndDistance {

	protected NodeAndMainDist(INode node, double distance) {
		super(node, distance);
	}

	public NodeAndMainDist(INode node, INode queryNode) {
		this(node, queryNode.getMainVector().distance(node.getVector()));
	}

}
