package com.shutart.rpkdtree.rpkdtrees.experimets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;
import com.shutart.rpkdtree.rpkdtrees.RPKDTrees;
import com.shutart.rpkdtree.rpkdtrees.RandomMatrix;
import com.shutart.rpkdtree.tests.linear.NNLinearSearcher;

public class Experiments {

	private static final String DATA_PATH = "datas\\";
	private static final String EXPERIMENTS_PATH = DATA_PATH + "experiments\\";
	private static int dimension = 41;
	private static int[] projectidDimensions = new int[] {2,3,4,5};
	/**
	 * parameter number of trees from 1 to <tt>maxNumberOfTrees</tt>
	 */
	private static int maxNumberOfTrees =  20;
	private static int numberOfNeighbors = 3;
	private static VectorI queryVector = getQueryVector();
	
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
		print(corpus);
		
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

	private static void print(Collection<Vector> list) {
		int number = 1;
		for (Vector vector : list) {
			System.out.println(number + ") " + vector);
			number++;
		}
		System.out.println("_______________________________");
	}

	private static VectorI getQueryVector() {
		// TODO Auto-generated method stub
		return null;
	}

	private static Set<Vector> getCorpus() {
		Set<Vector> corpus = new HashSet<Vector>();
		try {
			File corpusFile = new File(EXPERIMENTS_PATH + "ColorHist.csv");
			Scanner sc = new Scanner(corpusFile);
			int curNumberOfVec = 1;
			
			int nextNumbOfVec = -1;
			int numberOfAttribute = -1;
			double value = -1;
			for(StringTokenizer sTokenizer = new StringTokenizer(sc.nextLine(), ", :;");sTokenizer.hasMoreTokens();){
				nextNumbOfVec = new Integer(sTokenizer.nextToken());
				numberOfAttribute = new Integer(sTokenizer.nextToken());
				value = new Double(sTokenizer.nextToken());
			}
			while(sc.hasNextLine()){
				double[] keys = new double[dimension];
				while(true){
					keys[numberOfAttribute-1] = value;
					if(sc.hasNextLine()){
						for(StringTokenizer sTokenizer = new StringTokenizer(sc.nextLine(), ", :;");sTokenizer.hasMoreTokens();){
							nextNumbOfVec = new Integer(sTokenizer.nextToken());
							numberOfAttribute = new Integer(sTokenizer.nextToken());
							value = new Double(sTokenizer.nextToken());
						}
						if(nextNumbOfVec!=curNumberOfVec){
							break;
						}
					}else{
						break;
					}
				}
				Vector newVector = new VectorI(keys);
				corpus.add(newVector);	
				curNumberOfVec++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return corpus;
	}

}
