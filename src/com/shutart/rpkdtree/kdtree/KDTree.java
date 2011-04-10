package com.shutart.rpkdtree.kdtree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class KDTree implements IKDTree {
	private INode myRoot = null;
	public boolean isModifiedInLastTime = false;
	private int myDimension;

	public KDTree(int dimension) {
		this.myDimension = dimension;
	}

	@Override
	public INode insert(Vector vector) {
		assert myDimension == vector.size();
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

	/**
	 * 
	 *  This is realisation of algorithm Nearest Neighbor Search from paper "AN ALGORITHM FOR FINDING BEST MATCHES
	 *  IN LOGARITHMIC EXPECTED TIME" (authors: Jerome H. Friedman, Jon Louis Bentley, Raphael Ari Finkel)
	 */
	@Override
	public List<Vector> nnsearch(int numberOfNeighbors, Vector queryVector) {
		assert queryVector.size()==myDimension;
		//PQD and PQR
		NNSearcher nnSearcher = new NNSearcher();
		INode queryNode = new Node(queryVector);
		return nnSearcher.search(numberOfNeighbors,queryNode);
	}
	/**
	 * This class for "nnsearch" method. This class do nn-search.
	 * @author Артем
	 *
	 */
	private final class NNSearcher{
		private double[] myMinBounds = new double[myDimension];
		private double[] myMaxBounds = new double[myDimension];
		private PriorityQueue<NodeAndDistanse> myNodesWithDistance;
		private Stack<NodeAndBool> myParentsPath = new Stack<NodeAndBool>();
		
		public NNSearcher(){
			for (int i = 0; i < myDimension; i++) {
				myMinBounds[i] = Double.NEGATIVE_INFINITY;
				myMaxBounds[i] = Double.POSITIVE_INFINITY;
			}			
			
		}

		public List<Vector> search(int numberOfNeighbors, INode queryNode) {
			myNodesWithDistance = new PriorityQueue<NodeAndDistanse>(numberOfNeighbors,NodeAndDistanse.comparator());
			INode curNode = myRoot;
			boolean closerSonForCurNodeExamined = false;
			boolean searchFinished = false;
			while(!searchFinished){
				myNodesWithDistance.add(new NodeAndDistanse(curNode, curNode.distance(queryNode)));
				if(curNode.isLeaf()){
					if(ballWithinBounds() || myParentsPath.empty()){
						searchFinished = true;
					}else{
						curNode = myParentsPath.pop().node;
					}
				}else{
					//myFathersStack.push(new NodeAndBool(curNode));
					boolean qNodeIsLoSucces = queryNode.isLoSuccessorOf(curNode);
					if(!closerSonForCurNodeExamined){
						myParentsPath.push(new NodeAndBool(curNode));
						curNode = curNode.getSon(qNodeIsLoSucces);
					}else{
						if(boundsOverlapBall()){
							myParentsPath.push(new NodeAndBool(curNode))
							curNode = curNode.getSon(!qNodeIsLoSucces);
						}
						
					}
				}
			}
			return NodeAndDistanse.getListBestNodes(numberOfNeighbors,myNodesWithDistance);
		}
		
		private class NodeAndBool{
			final INode  node;
			final boolean closerSonForThisNodeExamined;
			
			public NodeAndBool(INode node, boolean closerSonForThisNodeExamined) {
				this.node = node;
				this.closerSonForThisNodeExamined = closerSonForThisNodeExamined;
			}			
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
