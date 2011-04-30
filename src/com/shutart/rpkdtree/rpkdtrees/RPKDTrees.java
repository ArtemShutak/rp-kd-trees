package com.shutart.rpkdtree.rpkdtrees;

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
	private final Map<VectorAndSpaceNum, Vector> projVec2SourceVec = new HashMap<VectorAndSpaceNum, Vector>();

	public RPKDTrees(int dimension, int projectidDimension,int numberOfTrees){
		assert dimension > projectidDimension;
		this.myDimension = dimension;
		this.myProjectedDimension = projectidDimension;
		myProjTrees = new KDTree[numberOfTrees];
		
		myRandomMatrixes = RandomMatrix.getMatrixes(dimension,projectidDimension,numberOfTrees);
		for (int i = 0; i < numberOfTrees; i++) {
			myProjTrees[i] = new KDTree(myProjectedDimension);
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
				projVec2SourceVec.put(new VectorAndSpaceNum(projVec, spaceNum), vector);
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
				Vector newVector = projVec2SourceVec.get(new VectorAndSpaceNum(vector, spaceNum));
				vecQueue.add(newVector);
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
	
	private class VectorAndSpaceNum{

		private final int spaceNum;
		private final Vector vec;

		public VectorAndSpaceNum(Vector projVec, int spaceNum){
			this.vec = projVec;
			this.spaceNum = spaceNum;
		}

		public int getSpaceNum() {
			return spaceNum;
		}

		public Vector getVector() {
			return vec;
		}
		
		@Override
		public boolean equals(Object vectorAndNum) {
			if(! (vectorAndNum instanceof VectorAndSpaceNum)){
				throw new IllegalArgumentException();
			}
			return spaceNum==((VectorAndSpaceNum)vectorAndNum).spaceNum &&
					vec.equals(((VectorAndSpaceNum)vectorAndNum).vec);
		}
		
	}

}
