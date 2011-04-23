package com.shutart.rpkdtree.rpkdtrees;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.shutart.rpkdtree.fixedqueue.FixedSizePriorityQueueByMainDist;
import com.shutart.rpkdtree.kdtree.INode;
import com.shutart.rpkdtree.kdtree.KDTree;
import com.shutart.rpkdtree.kdtree.Node;
import com.shutart.rpkdtree.kdtree.Vector;

/**
 * Look at paper "Randomly Projected KD-Trees 
 * with Distance Metric Learning for Image Retrieval"
 * @author Артем
 *
 */
public class RPKDTrees {
	private final int myDimension;
	private final int myProjectedDimension;
	private final KDTree[] myProjTrees;
	private final RandomMatrix[] myRandomMatrixes;

	public RPKDTrees(int dimension, int projectidDimension,int numberOfTrees){
		assert dimension > projectidDimension;
		this.myDimension = dimension;
		this.myProjectedDimension = projectidDimension;
		myProjTrees = new KDTree[numberOfTrees];
		myRandomMatrixes = new RandomMatrix[numberOfTrees];
		for (int i = 0; i < numberOfTrees; i++) {
			myProjTrees[i] = new KDTree(myProjectedDimension);
			myRandomMatrixes[i] = new RandomMatrix(myDimension, myProjectedDimension);
		}
	}
	
	/**
	 * 
	 * @param corpus - a set of data points
	 * @param numberOfTrees - number of kd-trees used
	 */
	public void indexing(Set<Vector> corpus) {
		for (Vector vector : corpus) {
			assert vector.size() == myDimension;
			for (int i = 0; i < getNumberOfTrees(); i++) {
				myProjTrees[i].insert(myRandomMatrixes[i].multiply(vector));
			}
		}
	}
	
	private int getNumberOfTrees() {
		return myProjTrees.length;
	}

	public List<INode> aproxNNsearch (int numberOfNeighbors, Vector queryVector){
		assert queryVector.size() == myDimension;
		FixedSizePriorityQueueByMainDist queue = new FixedSizePriorityQueueByMainDist
												(numberOfNeighbors, new Node(null, queryVector));
		for (int i = 0; i < getNumberOfTrees(); i++) {
			List<INode> nearestNeighbors = myProjTrees[i].nnsearch(numberOfNeighbors, queryVector);
			queue.addAll(nearestNeighbors);
		}
		return queue.getNearestNeighbors();
	}

}
