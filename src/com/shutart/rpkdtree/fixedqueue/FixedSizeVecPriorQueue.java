package com.shutart.rpkdtree.fixedqueue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;

import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;

public class FixedSizeVecPriorQueue {
	/**
	 * Priority Queue by Distance from MainVector. This queue contains no more than myNumberOfNeighbors elements.
	 * The head element is vector, which have the LARGEST distance from mainVector.
	 */
	private final PriorityQueue<VecAndDist> myVectorsWithDistance;
	private final int myNumberOfNeighbors;
	private final Vector myQueryVector;
	
	public FixedSizeVecPriorQueue(int numberOfNeighbors, Vector queryVector){
		myQueryVector = queryVector;
		myNumberOfNeighbors = numberOfNeighbors;
		myVectorsWithDistance = new PriorityQueue<VecAndDist>(myNumberOfNeighbors+1, VecAndDist.comparator());
	}
	
	public void add(Vector vector){
		assert vector.size()==myQueryVector.size();
		myVectorsWithDistance.add(new VecAndDist(vector, vector.distance(myQueryVector)));
		if(myVectorsWithDistance.size()>myNumberOfNeighbors){
			myVectorsWithDistance.poll();
		}
	}
	
	public boolean contains(Vector vector){
		return myVectorsWithDistance.contains(new VecAndDist(vector, vector.distance(myQueryVector)));
	}

	public List<Vector> getNearestNeighbors(){
		Vector[] resArray = new VectorI[myVectorsWithDistance.size()];
		for (int indexOfRes = resArray.length-1; indexOfRes > -1; indexOfRes--) {
			print(myVectorsWithDistance.peek());
			resArray[indexOfRes]=myVectorsWithDistance.poll().getVector();
		}
		System.out.println("      ");
		return Arrays.asList(resArray);
	}
	
	private void print(VecAndDist peek) {
		System.out.println("dist:" + peek.getDistance() + " Node:" + peek.getVector() );		
	}
	
	public double getPQD1() {
		return myVectorsWithDistance.peek().getDistance();
	}

	public int size() {
		return myVectorsWithDistance.size();
	}

	public void addAll(Collection<Vector> foundVectors) {
		for (Vector vector : foundVectors) {
			this.add(vector);
		}		
	}

}
