package com.shutart.rpkdtree.rpkdtrees.experimets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import com.shutart.rpkdtree.kdtree.IKDTree;
import com.shutart.rpkdtree.kdtree.KDTree;
import com.shutart.rpkdtree.kdtree.Vector;
import com.shutart.rpkdtree.kdtree.VectorI;
import com.shutart.rpkdtree.rpkdtrees.RPKDTrees;
import com.shutart.rpkdtree.rpkdtrees.RandomMatrix;
import com.shutart.rpkdtree.tests.linear.NNLinearSearcher;

public class Experiments {
	private static final String DATA_PATH = "datas\\";
	private static final String EXPERIMENTS_PATH = DATA_PATH + "experiments\\";
	public static final int dimension = 41;
	/**
	 * parameter number of trees from 1 to <tt>maxNumberOfTrees</tt>
	 */
	private static final int MAX_NUMBER_OF_TREES =  20;
	private static final int NUMBER_OF_NEIGHBORS = 10;//------------------------------------------------------------------
	//private static final int divisorForQVector = 500;//-------------
	private static final int NUMBER_OF_qVECTOR = 2;//---------------------------
	private static final int[] PROJECTED_DIMENSION = new int[] {1,3,5};
	private static String attributesForResFile;
	static{
		attributesForResFile = "d="+dimension+"_d1=";
		for (int i = 0; i < PROJECTED_DIMENSION.length; i++) {
			attributesForResFile+=PROJECTED_DIMENSION[i];
		}
		attributesForResFile+="_m=1-"+MAX_NUMBER_OF_TREES + "_nNeig="+NUMBER_OF_NEIGHBORS + "_NqVec"+NUMBER_OF_qVECTOR;
		System.out.println("attributesForResFile= " + attributesForResFile);
	}
	private static final String CUR_EXPERIMENTS_PATH = EXPERIMENTS_PATH + attributesForResFile + "\\";
	private static final String COMPLEXITY_OF_SEARCH_FILE_NAME = 
		CUR_EXPERIMENTS_PATH + "complexityOfSearch_" + attributesForResFile + ".csv";
	private static final String TIME_SPEED_OF_SEARCH_FILE_NAME = 
		CUR_EXPERIMENTS_PATH + "timeSpeedOfSearch_"+attributesForResFile + ".csv";
	private static final String PRECISION_FILE_NAME =
		CUR_EXPERIMENTS_PATH + "precision_"+attributesForResFile+".csv";

	//private static Vector queryVector = getQueryVector();
	
	//results
	//speed of search
	private static Integer[] complexity = new Integer [MAX_NUMBER_OF_TREES];
	private static Long[] timeComplexityInSec = new Long [MAX_NUMBER_OF_TREES];
	
	private static int complexityForLinearSearch;
	private static long timeComplexityInSecForLinearSearch;
	
	private static int complexityForKDTree;
	private static long timeComplexityInSecForKDTree;
	//average precision
	private static Double[] precision = new Double [MAX_NUMBER_OF_TREES];
	private static double precisionForLinearSearch;
	private static double precisionForKDTree;

	

	/**
	 * @param args
	 */
	public static void main(String[] args){
		clearResultsFiles();
		Set<Vector> corpus = getCorpus();
		Vector queryVector = getQueryVector(NUMBER_OF_qVECTOR,corpus);
		//print(corpus);
		
		Timer timer = new Timer();
		List<Vector> exactRes = linearSearchExper(corpus, queryVector, timer);
		List<Vector> resOfKDTree = kdtreeSearchExper(corpus, queryVector, timer, exactRes);
		compare(resOfKDTree, exactRes);
		for (int iProjDim = 0; iProjDim < PROJECTED_DIMENSION.length; iProjDim++) {
			for (int numOfTrees = 1; numOfTrees <= MAX_NUMBER_OF_TREES; numOfTrees++) {
				RPKDTrees tree = new RPKDTrees(dimension, PROJECTED_DIMENSION[iProjDim], numOfTrees);
				tree.indexing(corpus);
				timer.run();
				List<Vector> approxRes = tree.aproxNNsearch(NUMBER_OF_NEIGHBORS, queryVector);
				timer.stop();
				complexity[numOfTrees-1] = tree.getComplexityOfLastSearch();
				timeComplexityInSec[numOfTrees-1] = timer.getTime();	
				
				precision[numOfTrees-1] = Precision.averagePrecision(exactRes, approxRes);
				
				System.out.println("Precision = " + Precision.precision(exactRes, approxRes));
				System.out.println("ArgPrecision=" + precision[numOfTrees-1]);
			}			
			writeAllResultsInFiles();
		}
	}

	private static List<Vector> kdtreeSearchExper(Set<Vector> corpus,
			Vector queryVector, Timer timer, List<Vector> exactRes) {
		KDTree kdtree = new KDTree(dimension);
		for (Vector vector : corpus) {
			kdtree.insert(vector);
		}
		timer.run();
		List<Vector> resOfKDTree = kdtree.nnsearch(NUMBER_OF_NEIGHBORS, queryVector);
		timer.stop();
		complexityForKDTree= kdtree.getComplexityOfLastSearch();
		timeComplexityInSecForKDTree= timer.getTime();
		precisionForKDTree= Precision.averagePrecision(exactRes, resOfKDTree);
		writeResultsOfKDTreeInFile();
		return resOfKDTree;
	}

	private static List<Vector> linearSearchExper(Set<Vector> corpus,
			Vector queryVector, Timer timer) {
		timer.run();
		List<Vector> exactRes = new NNLinearSearcher(corpus).nnsearch(NUMBER_OF_NEIGHBORS, queryVector);
		timer.stop();
		complexityForLinearSearch = corpus.size();
		timeComplexityInSecForLinearSearch = timer.getTime();
		precisionForLinearSearch = Precision.averagePrecision(exactRes, exactRes);
		writeResultsOfLinearSearchInFile();
		return exactRes;
	}

	private static void writeResultsOfKDTreeInFile() {
		writeResultsInFile(complexityForKDTree,timeComplexityInSecForKDTree,precisionForKDTree,true);		
	}

	private static void writeResultsOfLinearSearchInFile() {
		writeResultsInFile(complexityForLinearSearch,timeComplexityInSecForLinearSearch,precisionForLinearSearch,false);
		
	}

	private static void writeResultsInFile(int complexityOfSearch, long timeComplexity,
			double precision, boolean append) {
		try {
			Writer outputSpeed = new BufferedWriter(new FileWriter(COMPLEXITY_OF_SEARCH_FILE_NAME,append));
			Writer outputTime = new BufferedWriter(new FileWriter(TIME_SPEED_OF_SEARCH_FILE_NAME,append));
			Writer outputPrecision = new BufferedWriter(new FileWriter(PRECISION_FILE_NAME,append));
			for (int i = 0; i < MAX_NUMBER_OF_TREES; i++) {
				outputSpeed.write(complexityOfSearch + ", ");
				outputTime.write(timeComplexity + ", ");
				outputPrecision.write(precision + ", ");
			}
			outputSpeed.write("\n");
			outputTime.write("\n ");
			outputPrecision.write("\n");
			outputSpeed.close();
			outputTime.close();
			outputPrecision.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeAllResultsInFiles() {
		writeResultsInFile(COMPLEXITY_OF_SEARCH_FILE_NAME,complexity);
		writeResultsInFile(TIME_SPEED_OF_SEARCH_FILE_NAME,timeComplexityInSec);
		writeResultsInFile(PRECISION_FILE_NAME,precision);	
	}

	private static void writeResultsInFile(String fileName, Object[] results) {
		try {
			Writer output = new BufferedWriter(new FileWriter(fileName,true));
			for (int i = 0; i < MAX_NUMBER_OF_TREES; i++) {
				output.write(results[i].toString()+", ");
			}
			output.write("\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void clearResultsFiles() {
		// TODO Auto-generated method stub
		
	}

	private static void print(Collection<Vector> list) {
		int number = 1;
		for (Vector vector : list) {
			System.out.println(number + ") " + vector);
			number++;
		}
		System.out.println("_______________________________");
	}

	private static Vector getQueryVector(int vecNum, Set<Vector> corpus) {
		int index = 0;
		for (Iterator iterator = corpus.iterator(); iterator.hasNext();) {
			Vector vector = (Vector) iterator.next();
			if(index==vecNum){
				iterator.remove();
				return vector;
			}
			index++;
		}
		return null;
	}

	private static Vector getQueryVector() {
		String queryVectorFileName = "queryVector" + "_d=" + dimension + ".txt";
		File queryVectorFile = new File(EXPERIMENTS_PATH + queryVectorFileName);
		if (!queryVectorFile.exists()) {
			return createQueryVectorFile(queryVectorFile, dimension);
		}
		return readQueryVectorFile(queryVectorFile, dimension);
	}

	private static Vector readQueryVectorFile(File queryVectorFile,int dimension) {
		try {
			Scanner sc = new Scanner(queryVectorFile);
			double[] keys = new double[dimension];
			int counter = 0;
			while (sc.hasNextLine()) {
				keys[counter] = new Double(sc.nextLine());
				counter++;
			}
			return new VectorI(keys);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Vector createQueryVectorFile(File fileName,
			int dimension) {
		try {
			Writer output = new BufferedWriter(new FileWriter(fileName));
			double[] keys = new double[dimension];
			Random random = new Random();
			for (int i = 0; i < keys.length; i++) {
				keys[i] = random.nextDouble();//divisorForQVector;
				//if(keys[i]>0.4){
					//keys[i]/=10;
				//}
				output.write(keys[i]+"\n");
			}
			output.close();
			return new VectorI(keys);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Set<Vector> getCorpus() {
		Set<Vector> corpus = new LinkedHashSet<Vector>();
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
				if(!vecIsZero(newVector)){
					corpus.add(newVector);	
				}else{
					System.out.println("zeroVector__________________");
				}
				//System.out.println(newVector);
				curNumberOfVec++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return corpus;
	}

	private static boolean vecIsZero(Vector vector) {
		for (int i = 0; i < vector.size(); i++) {
			if(vector.getKey(i)!=0){
				return false;
			}
		}
		return true;
	}
	private static void compare(List<Vector> res1, List<Vector> res2) {
		if(res1.size()==res2.size()){
			Iterator<Vector> approxIter = res1.iterator();
			for (Vector vector : res2) {
				Vector approxVect = approxIter.next();
				if(!vector.equals(approxVect )){
					assert false: approxVect + " != " + vector;
				}
			}
			return;
		}
		assert false: "approxRes.size() != exactRes.size()";
		
	}

}
