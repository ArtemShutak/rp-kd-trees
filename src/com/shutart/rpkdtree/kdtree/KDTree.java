package com.shutart.rpkdtree.kdtree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import com.shutart.rpkdtree.fixedqueue.FixedSizePriorityQueueByDistance;

public class KDTree implements IKDTree {
	private INode myRoot = null;
	public boolean isModifiedInLastTime = false;
	private int myDimension;
	private int myComplexityOfLastSearch;

	public KDTree(int dimension) {
		this.myDimension = dimension;
	}

	@Override
	public Vector insert(Vector vector) {
		assert myDimension == vector.size();
		isModifiedInLastTime = false;
		INode insertableNode = new Node(vector);
		return insert(insertableNode);
	}

	
	private Vector insert(INode insertableNode) {
		if (myRoot == null) {
			insertableNode.initBoundsAndDiscrim(null, false);
			myRoot = insertableNode;
			return myRoot.getVector();
		}
		INode curNode = myRoot;
		while (true) {
			if(curNode.getVector().equals(insertableNode.getVector())){
				return curNode.getVector();
			}
			boolean itIsLoSon = insertableNode.isLoSuccessorOf(curNode);
			INode successor = curNode.getSon(itIsLoSon);
			if(successor == null){
				isModifiedInLastTime = true;
				insertableNode.initBoundsAndDiscrim(curNode, itIsLoSon);
				curNode.setSon(itIsLoSon, insertableNode);
				return insertableNode.getVector();
			}else{
				curNode = successor;
			}
		}
	}

	/**
	 * 
	 *  This is realisation of algorithm Nearest Neighbor Search from paper "AN ALGORITHM FOR FINDING BEST MATCHES
	 *  IN LOGARITHMIC EXPECTED TIME" (authors: Jerome H. Friedman, Jon Louis Bentley, Raphael Ari Finkel)
	 */
	@Override
	public List<Vector> nnsearch(final int numberOfNeighbors, final Vector queryVector) {
		assert queryVector.size()==myDimension;
		final INode queryNode = new Node(queryVector);
		return nnsearch(numberOfNeighbors, queryNode);
	}
	
	
	private List<Vector> nnsearch(final int numberOfNeighbors, final INode queryNode) {
		NNSearcherRecursive nnSearcher = new NNSearcherRecursive(numberOfNeighbors,queryNode);
		return nnSearcher.search();
	}
	
	/**
	 * This class for "nnsearch" method. This class do recursive nn-search.
	 * @author Артем
	 *
	 */
	private final class NNSearcherRecursive{
		private final double[] myMinBounds = new double[myDimension];
		private final double[] myMaxBounds = new double[myDimension];
		private final INode myQueryNode;
		private int myNumberOfNeighbors;
		//PQD and PQR
		private FixedSizePriorityQueueByDistance myNodesPriorQueue;
		
		//complexity of search
		private int counterOfNode = 0;
		
		public NNSearcherRecursive(final int numberOfNeighbors, final INode queryNode){
			myNumberOfNeighbors = numberOfNeighbors;
			myQueryNode = queryNode;
			for (int i = 0; i < myDimension; i++) {
				myMinBounds[i] = Double.NEGATIVE_INFINITY;
				myMaxBounds[i] = Double.POSITIVE_INFINITY;
			}				
			myNodesPriorQueue = new FixedSizePriorityQueueByDistance(myNumberOfNeighbors, queryNode);
		}

		public List<Vector> search() {
			recursiveSearch(myRoot);
			myComplexityOfLastSearch+=counterOfNode;
			System.out.println("testCounterOfNode = " + counterOfNode);
			return myNodesPriorQueue.getNearestNeighbors();
		}

		/**
		 * 
		 * @param node
		 * @return true - if Search Finished.
		 */
		private boolean recursiveSearch(final INode node) {
			counterOfNode++;
			myNodesPriorQueue.add(node);
			if(node.isLeaf()){
				return ballWithinBounds();
			}else{
				boolean qNodeIsLoSucces = myQueryNode.isLoSuccessorOf(node);
				boolean searchComplite;
				if(node.getSon(qNodeIsLoSucces)!=null){
					if(qNodeIsLoSucces){
						double temp = myMaxBounds[node.getDiscriminator()];
						myMaxBounds[node.getDiscriminator()] = node.getKey(node.getDiscriminator());
						searchComplite = recursiveSearch(node.getLoSon());
						myMaxBounds[node.getDiscriminator()] = temp;
					}else{
						double temp = myMinBounds[node.getDiscriminator()];
						myMinBounds[node.getDiscriminator()] = node.getKey(node.getDiscriminator());
						searchComplite = recursiveSearch(node.getHiSon());
						myMinBounds[node.getDiscriminator()] = temp;
					}
					if(searchComplite)
						return true;
				}
				
				if (node.getSon(!qNodeIsLoSucces) != null) {
					if (qNodeIsLoSucces) {
						double temp = myMinBounds[node.getDiscriminator()];
						myMinBounds[node.getDiscriminator()] = node.getKey(node.getDiscriminator());
						searchComplite = boundsOverlapBall()&& recursiveSearch(node.getHiSon());
						myMinBounds[node.getDiscriminator()] = temp;
					} else {
						double temp = myMaxBounds[node.getDiscriminator()];
						myMaxBounds[node.getDiscriminator()] = node.getKey(node.getDiscriminator());
						searchComplite = boundsOverlapBall()&& recursiveSearch(node.getLoSon());
						myMaxBounds[node.getDiscriminator()] = temp;
					}
					if (searchComplite)
						return true;
				}
				return ballWithinBounds();
			}			
		}

		/**
		 * 
		 * @return отвечает на вопрос "надо еще скать".
		 */
		private boolean boundsOverlapBall() {
			if(myNodesPriorQueue.size()<myNumberOfNeighbors){
				return true;
			}
			double sum = 0;
			for (int d = 0; d < myDimension; d++) {
				if (myQueryNode.getKey(d) < myMinBounds[d]) {
					sum += myQueryNode.f_i(d, myMinBounds[d]);
					if (VectorI.dissim(sum) > getPQD1())
						return false;
				} else if (myQueryNode.getKey(d) > myMaxBounds[d]) {
					sum += myQueryNode.f_i(d, myMaxBounds[d]);
					if (VectorI.dissim(sum) > getPQD1())
						return false;
				}
				
			}
			return true;
		}
		
		/**
		 * 
		 * @return отвечает на вопрос "уже хватит искать?"
		 */
		private boolean ballWithinBounds() {
			if (myNodesPriorQueue.size() < myNumberOfNeighbors) {
				return false;
			}
			for (int d = 0; d < myDimension; d++) {
				if (myQueryNode.coordinateDistance(d, myMinBounds[d]) <= getPQD1()
					|| myQueryNode.coordinateDistance(d, myMaxBounds[d]) <= getPQD1())
					return false;
			}
			return true;
		}

		private double getPQD1() {
			return myNodesPriorQueue.getPQD1();
		}
		
		
	}
	
	public String toString(){
		if(myRoot!=null){
			INode saveRoot = myRoot;
			myRoot = saveRoot.getLoSon();
			String losonTreeString = this.toString();
			myRoot = saveRoot.getHiSon();
			String hisonTreeString = this.toString();
			myRoot = saveRoot;
			StringBuilder sb = new StringBuilder();
			sb.append(myRoot.toString()+ "\n");
			if(losonTreeString!=null){
				sb.append("_LOSON_"+ myRoot.getVector() +"\n{\n" + losonTreeString + "}\n");
			}
			if(hisonTreeString!=null){
				sb.append("_HISON_"+ myRoot.getVector() +"\n{\n" + hisonTreeString + "}\n");
			}
			return sb.toString();
		}
		return null;
	}

	public int getComplexityOfLastSearch() {
		return myComplexityOfLastSearch;
	}

}
