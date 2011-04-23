package com.shutart.rpkdtree.rpkdtrees;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.shutart.rpkdtree.fixedqueue.FixedSizeVecPriorQueue;
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
	private final Map<Vector, Vector> projVec2SourceVec = new HashMap<Vector, Vector>();

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
				Vector projVec = myRandomMatrixes[i].multiply(vector);
				projVec2SourceVec.put(projVec, vector);
				myProjTrees[i].insert(projVec );
			}
		}
	}
	
	private int getNumberOfTrees() {
		return myProjTrees.length;
	}

	public List<Vector> aproxNNsearch (int numberOfNeighbors, Vector queryVector){
		assert queryVector.size() == myDimension;
		FixedSizeVecPriorQueue vecQueue = new MyFixedSizeVecPriorQueue(numberOfNeighbors, queryVector);
		for (int i = 0; i < getNumberOfTrees(); i++) {
			List<Vector> nearestNeighbors = 
				myProjTrees[i].nnsearch(numberOfNeighbors, myRandomMatrixes[i].multiply(queryVector));
			for (Vector vector : nearestNeighbors) {
				vecQueue.add(projVec2SourceVec.get(vector));
			}
		}
		return vecQueue.getNearestNeighbors();
	}
	
	private class MyFixedSizeVecPriorQueue extends FixedSizeVecPriorQueue{

		public MyFixedSizeVecPriorQueue(int numberOfNeighbors, Vector queryVector) {
			super(numberOfNeighbors, queryVector);
		}
		
		@Override
		public void add(Vector vector){
			if(!this.contains(vector)){
				super.add(vector);
			}
		}
		
	}

}
