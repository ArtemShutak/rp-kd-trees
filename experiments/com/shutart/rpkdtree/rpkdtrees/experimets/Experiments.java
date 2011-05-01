package com.shutart.rpkdtree.rpkdtrees.experimets;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;
import com.shutart.rpkdtree.rpkdtrees.RPKDTrees;
import com.shutart.rpkdtree.tests.linear.NNLinearSearcher;

public class Experiments {

	private static int dimension;
	private static int[] projectidDimensions = new int[] {5,10,15};
	private static int maxNumberOfTrees =  20;
	private static int numberOfNeighbors;
	private static VectorI queryVector;
	
	//results
	//speed of search
	private static int[] complexity = new int [maxNumberOfTrees];
	private static long[] timeComplexityInSec = new long [maxNumberOfTrees];
	private static long timeComplexityInSecForLinearSearch;
	//average precision
	private static int[] precision = new int [maxNumberOfTrees];
	private static long precisionForLinearSearch;

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Set<Vector> corpus = getCorpus();
		
		Timer timer = new Timer();
		for (int iProjDim = 0; iProjDim < projectidDimensions.length; iProjDim++) {
			timer.run();
			List<Vector> exactRes = new NNLinearSearcher(corpus).nnsearch(numberOfNeighbors, queryVector);
			timer.stop();
			timeComplexityInSecForLinearSearch = timer.getTimeInSec();
			for (int numOfTrees = 0; numOfTrees < maxNumberOfTrees; numOfTrees++) {
				RPKDTrees tree = new RPKDTrees(dimension, projectidDimensions[iProjDim], numOfTrees);
				tree.indexing(corpus);
				timer.run();
				List<Vector> approxRes = tree.aproxNNsearch(numberOfNeighbors, queryVector);
				timer.stop();
				complexity[numOfTrees] = tree.getComplexityOfLastSearch();
				timeComplexityInSec[numOfTrees] = timer.getTimeInSec();	
				
				calculatePrecision(approxRes, exactRes);
				
			}			
			writeResults();
		}
		//print(approxRes);
		
		//print(exactRes);
		
		//compare(approxRes, exactRes);

	}

	private static void calculatePrecision(List<Vector> approxRes,
			List<Vector> exactRes) {
		throw new RuntimeException();
	}

	private static void writeResults() {
		// TODO Auto-generated method stub
		
	}

	private static void compare(List<Vector> approxRes, List<Vector> exactRes) {
		if(approxRes.size()==exactRes.size()){
			Iterator<Vector> approxIter = approxRes.iterator();
			for (Vector vector : exactRes) {
				Vector approxVect = approxIter.next();
				if(!vector.equals(approxVect )){
					assert false: approxVect + " != " + vector;
				}
			}
			return;
		}
		assert false: "approxRes.size() != exactRes.size()";
		
	}

	private static void print(List<Vector> list) {
		for (Vector vector : list) {
			System.out.print(vector + "   ");
		}
		System.out.println("\n_______________________________");
	}

	private static Set<Vector> getCorpus() {
		return createCorpus();
	}

	private static Set<Vector> createCorpus() {
		Set<Vector> corpus = new LinkedHashSet<Vector>();
		corpus.add(new VectorI(10,15,87,14));
		corpus.add(new VectorI(2,14,37,82));
		corpus.add(new VectorI(18,13,34,632));
		corpus.add(new VectorI(8,13,4,632));
		corpus.add(new VectorI(218,13,34,33));
		corpus.add(new VectorI(38,34,44,62));
		return corpus;
	}


}
