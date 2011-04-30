package com.shutart.rpkdtree.rpkdtrees;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.shutart.rpkdtree.kdtree.VectorI;

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
	private final Map<Vector, List<Vector>>[] projVec2SourceVec;

	public RPKDTrees(int dimension, int projectedDimension,int numberOfTrees){
		assert dimension > projectedDimension;
		this.myDimension = dimension;
		this.myProjectedDimension = projectedDimension;
		myRandomMatrixes = RandomMatrix.getMatrixes(dimension,projectedDimension,numberOfTrees);
		
		myProjTrees = new KDTree[numberOfTrees];
		projVec2SourceVec = new Map[numberOfTrees];		
		for (int i = 0; i < numberOfTrees; i++) {
			myProjTrees[i] = new KDTree(myProjectedDimension);
			projVec2SourceVec[i] = new HashMap<Vector, List<Vector>>();
			//myRandomMatrixes[i] = new RandomMatrix(myDimension, myProjectedDimension);
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
			for (int spaceNum = 0; spaceNum < getNumberOfTrees(); spaceNum++) {
				Vector projVec = myRandomMatrixes[spaceNum].multiply(vector);
				List<Vector> sorceVecs = projVec2SourceVec[spaceNum].get(projVec);
				if(sorceVecs==null){
					sorceVecs = new ArrayList<Vector>(1);
					projVec2SourceVec[spaceNum].put(projVec, sorceVecs);
				}
				sorceVecs.add(vector);
				//assert sorceVecs.size()==1 : "Matrix is bad";
				myProjTrees[spaceNum].insert(projVec );
			}
		}
	}
	
	private int getNumberOfTrees() {
		return myProjTrees.length;
	}

	public List<Vector> aproxNNsearch (int numberOfNeighbors, Vector queryVector){
		assert queryVector.size() == myDimension;
		FixedSizeVecPriorQueue vecQueue = new MyFixedSizeVecPriorQueue(numberOfNeighbors, queryVector);
		for (int spaceNum = 0; spaceNum < getNumberOfTrees(); spaceNum++) {
			List<Vector> nearestNeighbors = 
				myProjTrees[spaceNum].nnsearch(numberOfNeighbors, myRandomMatrixes[spaceNum].multiply(queryVector));
			for (Vector vector : nearestNeighbors) {
				List<Vector> foundVectors = projVec2SourceVec[spaceNum].get(vector);
				vecQueue.addAll(foundVectors);
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
