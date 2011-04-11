package com.shutart.rpkdtree.kdtree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class KDTree implements IKDTree {
	private INode myRoot = null;
	public boolean isModifiedInLastTime = false;
	private int myDimension;

	public KDTree(int dimension) {
		myDimension = dimension;
	}

	@Override
	public INode insert(Vector vector) {
		isModifiedInLastTime = false;
		INode insertableNode = new Node(vector);
		if (myRoot == null) {
			insertableNode.initBoundsAndDiscrim(null, false);
			myRoot = insertableNode;
			return myRoot;
		}
		INode curNode = myRoot;
		while (true) {
			if(curNode.getVector().equals(vector)){
				return curNode;
			}
			boolean itIsLoSon = insertableNode.isLoSuccessorOf(curNode);
			INode successor = curNode.getSon(itIsLoSon);
			if(successor == null){
				isModifiedInLastTime = true;
				insertableNode.initBoundsAndDiscrim(curNode, itIsLoSon);
				curNode.setSon(itIsLoSon, insertableNode);
				return insertableNode;
			}else{
				curNode = successor;
			}
		}
	}

	@Override
	public List<Vector> nnsearch(int numberOfNeighbors, Vector queryVector) {
		assert queryVector.size()==myDimension;
		INode queryNode = new Node(queryVector);
		NNSearcherRecursive nnSearcher = new NNSearcherRecursive(numberOfNeighbors,queryNode);
		return nnSearcher.search();
	}
	/**
	 * This class for "nnsearch" method. This class do nn-search.
	 * @author Артем
	 *
	 */
	private final class NNSearcher{
		
	}
	/**
	 * This class for "nnsearch" method. This class do nn-search.
	 * @author Артем
	 *
	 */
	private final class NNSearcherRecursive{
		private double[] myMinBounds = new double[myDimension];
		private double[] myMaxBounds = new double[myDimension];
		//PQD and PQR
		private PriorityQueue<NodeAndDistanse> myNodesWithDistance;
		private final INode myQueryNode;
		
		
		public NNSearcherRecursive(int numberOfNeighbors, INode queryNode){
			myNodesWithDistance = new PriorityQueue<NodeAndDistanse>(numberOfNeighbors,NodeAndDistanse.comparator());
			myQueryNode = queryNode;
			for (int i = 0; i < myDimension; i++) {
				myMinBounds[i] = Double.NEGATIVE_INFINITY;
				myMaxBounds[i] = Double.POSITIVE_INFINITY;
			}						
		}

		public List<Vector> search() {
			recursiveSearch(myRoot);
			return getResult();
		}

		/**
		 * 
		 * @param node
		 * @return true - if Search Finished.
		 */
		private boolean recursiveSearch(INode node) {
			myNodesWithDistance.add(new NodeAndDistanse(node, node.distance(myQueryNode)));
			if(node.isLeaf()){
				if(ballWithinBounds()){
					return true;
				}else{
					return false;
				}
			}else{
				boolean qNodeIsLoSucces = myQueryNode.isLoSuccessorOf(node);
				if(qNodeIsLoSucces){
					if(goOnLeftFrom(node)||(boundsOverlapBall()&&goOnRightFrom(node))){
						return true;
					}
				}else{
					if(goOnRightFrom(node)||(boundsOverlapBall()&&goOnLeftFrom(node))){
						return true;
					}
				}
				return ballWithinBounds();
			}			
		}

		private boolean boundsOverlapBall() {
			// TODO Auto-generated method stub
			return false;
		}

		private boolean ballWithinBounds() {
			// TODO Auto-generated method stub
			return false;
		}

		private boolean goOnRightFrom(INode node) {
			double temp = myMinBounds[node.getDiscriminator()];
			myMinBounds[node.getDiscriminator()] = node.getKey(node.getDiscriminator());
			boolean res = recursiveSearch(node.getHiSon());
			myMinBounds[node.getDiscriminator()] = temp;
			return res;
		}

		private boolean goOnLeftFrom(INode node) {
			double temp = myMaxBounds[node.getDiscriminator()];
			myMaxBounds[node.getDiscriminator()] = node.getKey(node.getDiscriminator());
			boolean res = recursiveSearch(node.getLoSon());
			myMaxBounds[node.getDiscriminator()] = temp;
			return res;
		}

		private List<Vector> getResult() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
	/**
	 * This class for "nnsearch" method. This class contain Node with distance from queryVector.
	 * @author Артем
	 *
	 */
	private final static class NodeAndDistanse{
		private final INode myNode;
		private final double myDistance;
		
		public NodeAndDistanse(INode node, double distance){
			myNode = node;
			myDistance = distance;
		}
		public static List<Vector> getListBestNodes(int numberOfNeighbors,
				PriorityQueue<NodeAndDistanse> nodesWithDistance) {
			// TODO Auto-generated method stub
			return null;
		}
		
		public static Comparator<NodeAndDistanse> comparator(){
			return new Comparator<NodeAndDistanse>() {
				@Override
				public int compare(NodeAndDistanse o1, NodeAndDistanse o2) {
					return (int) (o1.myDistance-o2.myDistance);
				}
			};
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

}
